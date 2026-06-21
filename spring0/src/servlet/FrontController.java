package servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import annotation.Controller;
import util.Utils;

@WebServlet("/*")

public class FrontController extends HttpServlet {

    private List<Class<?>> controllers = new ArrayList<>();

    @Override
    public void init() throws ServletException {
        String basePackages = this.getInitParameter("base-package");
        try {
            controllers = Utils.findClassesByAnnotation(Controller.class,
                    basePackages.split(","));

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

        out.println("</body>");
        out.println("</html>");
    }
}
