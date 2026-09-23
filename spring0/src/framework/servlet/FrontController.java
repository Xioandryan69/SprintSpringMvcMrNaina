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
import framework.util.Mapping;
import framework.util.Utils;
import framework.util.UrlMethod;
import java.util.HashMap;

import framework.util.ModelView;
import jakarta.servlet.RequestDispatcher;

//@WebServlet("/*")

public class FrontController extends HttpServlet {

    private List<Class<?>> controllers = new ArrayList<>();
    // private HashMap<String, Mapping> urlMapping = new HashMap<>();
    private HashMap<UrlMethod, Mapping> urlMappingmethod = new HashMap<>();

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
                Method method = clazz.getDeclaredMethod(m.getMethod());

                // Invocation de la méthode du contrôleur
                // method.invoke(controllerInstance);

                Object returnValue = method.invoke(controllerInstance);

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
                out.println("<p style='color: red;'>Erreur lors de l'exécution : " + e.getMessage() + "</p>");
                e.printStackTrace(out);
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
