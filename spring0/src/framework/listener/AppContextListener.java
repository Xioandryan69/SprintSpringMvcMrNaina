package framework.listener;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.util.HashMap;
import java.util.List;

import framework.annotation.Controller;
import framework.util.Mapping;
import framework.util.UrlMethod;
import framework.util.Utils;

@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        String basePackages = servletContext.getInitParameter("base-package");

        HashMap<UrlMethod, Mapping> urlMappingmethod = new HashMap<>();

        try {
            if (basePackages != null) {
                List<Class<?>> controllers = Utils.findClassesMethodByAnnotation(
                        Controller.class,
                        urlMappingmethod,
                        basePackages.split(","));

                servletContext.setAttribute("urlMapping", urlMappingmethod);
                servletContext.setAttribute("controllers", controllers);

                System.out.println("[Framework] Scan des contrôleurs réussi au démarrage !");
            }
        } catch (Exception e) {
            System.err.println("[Framework] Erreur lors du scan au démarrage : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Rien à nettoyer de particulier ici
    }
}