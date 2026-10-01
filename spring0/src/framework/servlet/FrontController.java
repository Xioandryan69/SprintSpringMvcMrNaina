package framework.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletContext;
//import jakarta.servlet.annotation.WebServlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import framework.annotation.Controller;
import framework.annotation.Get;
import framework.annotation.WebApi;
import framework.util.Mapping;
import framework.util.Utils;
import framework.util.UrlMethod;
import java.util.HashMap;

import framework.util.ModelView;
import framework.util.ParamBinder;
import java.lang.reflect.InvocationTargetException;
import jakarta.servlet.RequestDispatcher;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.StringJoiner;



//@WebServlet("/*")

public class FrontController extends HttpServlet {

    private List<Class<?>> controllers = new ArrayList<>();
    // private HashMap<String, Mapping> urlMapping = new HashMap<>();
    private HashMap<UrlMethod, Mapping> urlMappingmethod = new HashMap<>();


    private String toJsonNative(Object obj) {
        if (obj == null) {
            return "null";
        }
        
        // 1. Gestion des chaînes et caractères
        if (obj instanceof CharSequence || obj instanceof Character) {
            return "\"" + obj.toString().replace("\"", "\\\"") + "\"";
        }
        
        // 2. Gestion des nombres et booléens (pas de guillemets)
        if (obj instanceof Number || obj instanceof Boolean) {
            return obj.toString();
        }
        
        // 3. Gestion des Listes / Tableaux / Collections (Iterable)
        if (obj instanceof Iterable) {
            StringJoiner joiner = new StringJoiner(",", "[", "]");
            for (Object item : (Iterable<?>) obj) {
                joiner.add(toJsonNative(item));
            }
            return joiner.toString();
        }
        
        // 4. Gestion des Maps (dictionnaires)
        if (obj instanceof Map) {
            StringJoiner joiner = new StringJoiner(",", "{", "}");
            for (Map.Entry<?, ?> entry : ((Map<?, ?>) obj).entrySet()) {
                String key = "\"" + entry.getKey().toString().replace("\"", "\\\"") + "\"";
                String value = toJsonNative(entry.getValue());
                joiner.add(key + ":" + value);
            }
            return joiner.toString();
        }
        
        // 5. Gestion des DTOs / Objets personnalisés via Réflexion
        try {
            StringJoiner joiner = new StringJoiner(",", "{", "}");
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true); // Permet de lire les attributs privés
                String key = "\"" + field.getName() + "\"";
                String value = toJsonNative(field.get(obj));
                joiner.add(key + ":" + value);
            }
            return joiner.toString();
        } catch (Exception e) {
            return "{}"; // Repli en cas d'erreur de lecture
        }
    }



    // Sprint 7 : Mapping ne garde que le nom de la méthode, or getDeclaredMethod(nom)
    // sans types de paramètres échoue dès que la méthode a des paramètres (ex: save(...))
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
        // String basePackages = this.getInitParameter("base-package");
        ServletContext context = getServletContext();
        try {
            // controllers = Utils.findClassesMethodByAnnotation(Controller.class,
            // urlMappingmethod,
            // basePackages.split(","));

            urlMappingmethod = (HashMap<UrlMethod, Mapping>) context.getAttribute("urlMappingMethod");
            controllers = (List<Class<?>>) context.getAttribute("controllers");

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

        // Sprint 7 : à faire AVANT tout request.getParameter(...) pour les accents (POST)
        request.setCharacterEncoding("UTF-8");

       // PrintWriter out = response.getWriter();
        // Récupérer le chemin tapé (Ex: /SprintSpringMvcMrNaina/employe-list -> on
        // extrait juste la fin)
        String pathInfo = request.getRequestURI().substring(request.getContextPath().length());
        String httpMethod = request.getMethod();
        UrlMethod requestKey = new UrlMethod(pathInfo, httpMethod);

        if (urlMappingmethod.containsKey(requestKey)) { // Utilise automatiquement le hashCode et equals de UrlMethod
            Mapping m = urlMappingmethod.get(requestKey);

            try {
                Class<?> clazz = Class.forName(m.getClassName());
                Object controllerInstance = clazz.getDeclaredConstructor().newInstance();
                // Sprint 7 : la méthode peut avoir des paramètres, on la retrouve par son nom
                Method method = findMethod(clazz, m.getMethod());

                // Invocation de la méthode du contrôleur
                // method.invoke(controllerInstance);

                // Sprint 7 : formulaire -> framework -> paramètres de la méthode
                Object[] args = ParamBinder.buildArgs(method, request);
                Object returnValue = method.invoke(controllerInstance, args);
                if (method.isAnnotationPresent(WebApi.class)) {
                    response.setContentType("application/json; charset=UTF-8");
                    PrintWriter out = response.getWriter();

                    if (returnValue != null) {
                        // Si la méthode renvoie un ModelView, on extrait ses données
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

                // Sprint 2

                out.println("<h2>Scan des méthodes terminé !</h2>");
                out.println("<p>URL demandée : <strong>" + pathInfo + "</strong></p>");

                // Vérification si l'URL existe dans notre urlMapping
                out.println("<h2>Vérification du Mapping :</h2>");
                /*
                 * if (urlMapping.containsKey(pathInfo)) {
                 * Mapping m = urlMapping.get(pathInfo);
                 * 
                 * 
                 * out.println("<p style='color: green;'><strong>Match trouvé !</strong></p>");
                 * out.println("<ul>");
                 * out.println("<li>Contrôleur : " + m.getClassName() + "</li>");
                 * out.println("<li>Méthode associée : " + m.getMethod() + "()</li>");
                 * out.println("</ul>");
                 * 
                 * } else {
                 * out.
                 * println("<p style='color: red;'>Aucune méthode associée à cette URL dans la HashMap.</p>"
                 * );
                 * }
                 * 
                 * // Affichage complet de la HashMap pour débogage
                 * out.println("<h2>Contenu complet de la HashMap (urlMapping)</h2>");
                 * out.println("<table border='1' cellpadding='5'>");
                 * out.
                 * println("<tr><th>URL / Clé</th><th>Classe associée</th><th>Méthode associée</th></tr>"
                 * );
                 * for (Map.Entry<String, Mapping> entry : urlMapping.entrySet()) {
                 * out.println("<tr>");
                 * out.println("<td>" + entry.getKey() + "</td>");
                 * out.println("<td>" + entry.getValue().getClassName() + "</td>");
                 * out.println("<td>" + entry.getValue().getMethod() + "()</td>");
                 * out.println("</tr>");
                 * }
                 * out.println("</table>");
                 */

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
                // invoke() enveloppe les exceptions du contrôleur dans InvocationTargetException
                Throwable cause = (e instanceof InvocationTargetException && e.getCause() != null) ? e.getCause() : e;
                out.println("<p style='color: red;'>Erreur lors de l'exécution : " + cause.getMessage() + "</p>");
                cause.printStackTrace(out);
            }
        }  else {
            // Affichage de la page de débogage uniquement si aucune route ne matche
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