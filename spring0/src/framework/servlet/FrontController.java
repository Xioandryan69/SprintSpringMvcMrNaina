package framework.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;
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
import java.util.HashMap;

//@WebServlet("/*")

public class FrontController extends HttpServlet {

    private List<Class<?>> controllers = new ArrayList<>();
    private HashMap<String, Mapping> urlMapping = new HashMap<>();

    @Override
    public void init() throws ServletException {
        String basePackages = this.getInitParameter("base-package");
        try {
            controllers = Utils.findClassesByAnnotation(Controller.class,
                    basePackages.split(","));

            for (Class<?> controllerClass : controllers) {
                Method[] methods = controllerClass.getDeclaredMethods();
                for (Method method : methods) {
                    if (method.isAnnotationPresent(Get.class)) {
                        Get getAnnotation = method.getAnnotation(Get.class);
                        String url = getAnnotation.value();

                        Mapping mapping = new Mapping(controllerClass.getName(), method.getName());

                        urlMapping.put(url, mapping);
                    }
                }
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
        response.setContentType("text/html; charset=UTF-8");

        PrintWriter out = response.getWriter();
        // Récupérer le chemin tapé (Ex: /SprintSpringMvcMrNaina/employe-list -> on
        // extrait juste la fin)
        String pathInfo = request.getRequestURI().substring(request.getContextPath().length());

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


        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head><title>Spring0 - Étape Méthodes</title></head>");
        out.println("<body>");
        out.println("<h1>Scan des méthodes terminé !</h1>");
        out.println("<p>URL demandée : <strong>" + pathInfo + "</strong></p>");

        // Vérification si l'URL existe dans notre urlMapping
        out.println("<h2>Vérification du Mapping :</h2>");
        if (urlMapping.containsKey(pathInfo)) {
            Mapping m = urlMapping.get(pathInfo);
            out.println("<p style='color: green;'><strong>Match trouvé !</strong></p>");
            out.println("<ul>");
            out.println("<li>Contrôleur : " + m.getClassName() + "</li>");
            out.println("<li>Méthode associée : " + m.getMethod() + "()</li>");
            out.println("</ul>");
        } else {
            out.println("<p style='color: red;'>Aucune méthode associée à cette URL dans la HashMap.</p>");
        }

        // Affichage complet de la HashMap pour débogage
        out.println("<h2>Contenu complet de la HashMap (urlMapping)</h2>");
        out.println("<table border='1' cellpadding='5'>");
        out.println("<tr><th>URL / Clé</th><th>Classe associée</th><th>Méthode associée</th></tr>");
        for (Map.Entry<String, Mapping> entry : urlMapping.entrySet()) {
            out.println("<tr>");
            out.println("<td>" + entry.getKey() + "</td>");
            out.println("<td>" + entry.getValue().getClassName() + "</td>");
            out.println("<td>" + entry.getValue().getMethod() + "()</td>");
            out.println("</tr>");
        }
        out.println("</table>");

        out.println("</body>");
        out.println("</html>");
    }
}
