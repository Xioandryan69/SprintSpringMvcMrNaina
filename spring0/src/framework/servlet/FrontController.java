package framework.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletContext;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import framework.annotation.Auth;
import framework.annotation.Controller;
import framework.annotation.Get;
import framework.annotation.WebApi;
import framework.util.Mapping;
import framework.util.Utils;
import framework.util.UrlMethod;
import java.util.HashMap;

import framework.util.ModelView;
import framework.util.ParamBinder;
import framework.util.MySession;
import java.lang.reflect.InvocationTargetException;
import jakarta.servlet.RequestDispatcher;
import java.lang.reflect.Field;
import java.util.StringJoiner;

public class FrontController extends HttpServlet {

    private List<Class<?>> controllers = new ArrayList<>();
    private HashMap<UrlMethod, Mapping> urlMappingmethod = new HashMap<>();

    private String toJsonNative(Object obj) {
        if (obj == null) {
            return "null";
        }
        
        if (obj instanceof CharSequence || obj instanceof Character) {
            return "\"" + obj.toString().replace("\"", "\\\"") + "\"";
        }
        
        if (obj instanceof Number || obj instanceof Boolean) {
            return obj.toString();
        }
        
        if (obj instanceof Iterable) {
            StringJoiner joiner = new StringJoiner(",", "[", "]");
            for (Object item : (Iterable<?>) obj) {
                joiner.add(toJsonNative(item));
            }
            return joiner.toString();
        }
        
        if (obj instanceof Map) {
            StringJoiner joiner = new StringJoiner(",", "{", "}");
            for (Map.Entry<?, ?> entry : ((Map<?, ?>) obj).entrySet()) {
                String key = "\"" + entry.getKey().toString().replace("\"", "\\\"") + "\"";
                String value = toJsonNative(entry.getValue());
                joiner.add(key + ":" + value);
            }
            return joiner.toString();
        }
        
        try {
            StringJoiner joiner = new StringJoiner(",", "{", "}");
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                String key = "\"" + field.getName() + "\"";
                String value = toJsonNative(field.get(obj));
                joiner.add(key + ":" + value);
            }
            return joiner.toString();
        } catch (Exception e) {
            return "{}";
        }
    }

    private Method findMethod(Class<?> clazz, String name) throws NoSuchMethodException {
        for (Method candidate : clazz.getDeclaredMethods()) {
            if (candidate.getName().equals(name)) {
                return candidate;
            }
        }
        throw new NoSuchMethodException("Méthode introuvable : " + clazz.getName() + "." + name + "()");
    }

    @Override
    public void init() throws ServletException {
        ServletContext context = getServletContext();
        try {
            urlMappingmethod = (HashMap<UrlMethod, Mapping>) context.getAttribute("urlMappingMethod");
            controllers = (List<Class<?>>) context.getAttribute("controllers");
            String uploadDir = getInitParameter("upload-directory");
            if (uploadDir != null) {
                context.setAttribute("uploadDirectory", uploadDir);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        handle(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        handle(request, response);
    }

    private void handle(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        request.setCharacterEncoding("UTF-8");

        String pathInfo = request.getRequestURI().substring(request.getContextPath().length());
        String httpMethod = request.getMethod();
        UrlMethod requestKey = new UrlMethod(pathInfo, httpMethod);

        if (urlMappingmethod.containsKey(requestKey)) {
            Mapping m = urlMappingmethod.get(requestKey);

            try {
                Class<?> clazz = Class.forName(m.getClassName());
                Object controllerInstance = clazz.getDeclaredConstructor().newInstance();
                Method method = findMethod(clazz, m.getMethod());// --- SPRINT 10 & 10 BIS : VÉRIFICATION DE L'AUTHENTIFICATION ET DES RÔLES ---
                Auth auth = method.getAnnotation(Auth.class);
                if (auth == null && clazz.isAnnotationPresent(Auth.class)) {
                    auth = clazz.getAnnotation(Auth.class);
                }

                if (auth != null) {
                    jakarta.servlet.http.HttpSession httpSession = request.getSession();
                    String sessionKey = auth.key();
                    Object sessionValue = httpSession.getAttribute(sessionKey);

                    // 1. Sprint 10 : Vérification si l'utilisateur est connecté
                    if (sessionValue == null) {
                        throw new RuntimeException("Accès refusé : Vous devez être authentifié (connecté) pour accéder à cette ressource.");
                    }

                    // 2. Sprint 10 Bis : Vérification des rôles spécifiques
                    String[] requiredRoles = auth.value();
                    if (requiredRoles.length > 0) {
                        boolean authorized = false;
                        String userRole = sessionValue.toString(); 

                        for (String role : requiredRoles) {
                            if (role.trim().equalsIgnoreCase(userRole.trim())) {
                                authorized = true;
                                break;
                            }
                        }

                        if (!authorized) {
                            throw new RuntimeException("Accès interdit : Vous n'avez pas le rôle requis pour exécuter cette méthode.");
                        }
                    }
                }



                // 1. Construction des arguments (le ParamBinder injecte MySession initialisé avec la HttpSession)
                Object[] args = ParamBinder.buildArgs(method, request);

                Object returnValue = method.invoke(controllerInstance, args);

                for (Object arg : args) {
                    if (arg instanceof MySession) {
                        ((MySession) arg).updateHttpSession();
                    }
                }

                if (method.isAnnotationPresent(WebApi.class)) {
                    response.setContentType("application/json; charset=UTF-8");
                    PrintWriter out = response.getWriter();

                    if (returnValue != null) {
                        if (returnValue instanceof ModelView) {
                            ModelView mv = (ModelView) returnValue;
                            out.print(toJsonNative(mv.getData()));
                        } else {
                            out.print(toJsonNative(returnValue));
                        }
                    } else {
                        out.print("{}");
                    }
                    out.flush();
                    return;
                }

                if (returnValue instanceof ModelView) {
                    ModelView mv = (ModelView) returnValue;

                    for (Map.Entry<String, Object> entry : mv.getData().entrySet()) {
                        request.setAttribute(entry.getKey(), entry.getValue());
                    }

                    String viewPath = "/WEB-INF/views/" + mv.getUrl();
                    RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
                    dispatcher.forward(request, response);
                    return;
                }

                response.setContentType("text/html; charset=UTF-8");
                PrintWriter out = response.getWriter();
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head><title>spring0</title></head>");
                out.println("<body>");
                out.println("<h1>Bonjour depuis spring0 & 1 !</h1>");
                out.println("<p>Methodes !: <strong>" + request.getMethod() + "</strong></p>");
                out.println("<p> Path !: <strong>" + request.getServletPath() + "</strong></p>");
                out.println("<h2>Contrôleurs détectés</h2>");
                out.println("<ul>");

                for (Class<?> controller : controllers) {
                    out.println("<li>" + controller.getName() + "</li>");
                }

                out.println("</ul>");
                out.println("<p>Vous avez demandé : " + request.getRequestURI() + "</p>");
                out.println("<h2>Scan des méthodes terminé !</h2>");
                out.println("<p>URL demandée : <strong>" + pathInfo + "</strong></p>");
                out.println("<h2>Vérification du Mapping :</h2>");
                out.println("<p style='color: green;'><strong>Match trouvé et exécuté avec succès !</strong></p>");
                out.println("<ul>");
                out.println("<li>Contrôleur : " + m.getClassName() + "</li>");
                out.println("<li>Méthode associée : " + m.getMethod() + "()</li>");
                out.println("</ul>");
                out.println("</body>");
                out.println("</html>");

            } catch (Exception e) {
                response.setContentType("text/html; charset=UTF-8");
                PrintWriter out = response.getWriter();
                Throwable cause = (e instanceof InvocationTargetException && e.getCause() != null) ? e.getCause() : e;
                out.println("<p style='color: red;'>Erreur lors de l'exécution : " + cause.getMessage() + "</p>");
                cause.printStackTrace(out);
            }
        } else {
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head><title>spring0 - Débogage</title></head>");
            out.println("<body>");
            out.println("<h1>Bonjour depuis spring0 & 1 !</h1>");
            out.println("<p>Méthode HTTP : <strong>" + httpMethod + "</strong></p>");
            out.println("<p>Path demandé : <strong>" + pathInfo + "</strong></p>");
            out.println("<p style='color: red;'>Aucune méthode associée à l'URL " + pathInfo + " avec la méthode " + httpMethod + "</p>");
            out.println("<h2>Contrôleurs détectés</h2><ul>");
            if (controllers != null) {
                for (Class<?> controller : controllers) {
                    out.println("<li>" + controller.getName() + "</li>");
                }
            }
            out.println("</ul>");
            out.println("<h2>Contenu complet de la HashMap (urlMapping)</h2>");
            out.println("<table border='1' cellpadding='5'>");
            out.println("<tr><th>Clé (UrlMethod)</th><th>Classe associée</th><th>Méthode associée</th></tr>");
            if (urlMappingmethod != null) {
                for (Map.Entry<UrlMethod, Mapping> entry : urlMappingmethod.entrySet()) {
                    out.println("<tr>");
                    out.println("<td>" + entry.getKey().toString() + "</td>");
                    out.println("<td>" + entry.getValue().getClassName() + "</td>");
                    out.println("<td>" + entry.getValue().getMethod() + "()</td>");
                    out.println("</tr>");
                }
            }
            out.println("</table></body></html>");
            out.println("</body>");
            out.println("</html>");
        }
    }
}