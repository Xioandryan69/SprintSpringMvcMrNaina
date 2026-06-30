package framework.util;

import framework.annotation.Get;
import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Utils {

    public static List<Class<?>> findClassesByAnnotation(Class<? extends Annotation> annotation, String... basePackages)
            throws Exception {
        List<Class<?>> output = new ArrayList<>();
        for (String basePackage : basePackages) {
            List<Class<?>> classes = findClasses(basePackage);
            for (Class<?> clazz : classes) {
                if (clazz.getAnnotation(annotation) != null && !output.contains(clazz)) {
                    output.add(clazz);
                }
            }
        }

        return output;
    }

    // correction Mr Naina
    public static List<Class<?>> findClassesByAnnotation(Class<? extends Annotation> annotation,
            HashMap<String, Mapping> urlMapping, String... basePackages)
            throws Exception {
        List<Class<?>> output = new ArrayList<>();
        for (String basePackage : basePackages) {
            List<Class<?>> classes = findClasses(basePackage);
            for (Class<?> clazz : classes) {
                if (clazz.getAnnotation(annotation) != null && !output.contains(clazz)) {
                    output.add(clazz);
                }

                Method[] methods = clazz.getDeclaredMethods();
                for (Method method : methods) {
                    if (method.isAnnotationPresent(Get.class)) {
                        Get getAnnotation = method.getAnnotation(Get.class);
                        String url = getAnnotation.value();

                        Mapping mapping = new Mapping(clazz.getName(), method.getName());

                        urlMapping.put(url, mapping);
                    }
                }
            }
        }
        return output;
    }

    public static List<Class<?>> findClasses(String packageName) throws Exception {

        String packagePath = packageName.replace('.', '/');
        List<Class<?>> classes = new ArrayList<>();
        URL resource = Thread.currentThread()
                .getContextClassLoader()
                .getResource(packagePath);
        if (resource == null) {
            throw new Exception(
                    "Package introuvable : "
                            + packageName);
        }
        File packageDir = new File(resource.toURI());
        File[] files = packageDir.listFiles();

        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".class")) {
                String className = packageName + "." + file.getName().replace(".class", "");
                classes.add(Class.forName(className));
            } else if (file.isDirectory()) {
                classes.addAll(findClasses(packageName + "." + file.getName()));
            }
        }

        return classes;
    }

    public static List<Class<?>> findClassesMethodByAnnotation(Class<? extends Annotation> annotation,
            HashMap<UrlMethod, Mapping> urlMapping, String... basePackages) throws Exception {
        List<Class<?>> output = new ArrayList<>();
        for (String basePackage : basePackages) {
            List<Class<?>> classes = findClasses(basePackage);
            for (Class<?> clazz : classes) {
                if (clazz.getAnnotation(annotation) != null && !output.contains(clazz)) {
                    output.add(clazz);
                }

                Method[] methods = clazz.getDeclaredMethods();
                for (Method method : methods) {
                    if (method.isAnnotationPresent(Get.class)) {
                        Get getAnnotation = method.getAnnotation(Get.class);
                        String url = getAnnotation.value();

                        // creer Url Methodes
                        UrlMethod urlKey = new UrlMethod(url, "GET");
                        // Detections doublon s Url
                        if (urlMapping.containsKey(urlKey)) {
                            Mapping duplicate = urlMapping.get(urlKey);
                            throw new Exception("Erreur de mapping d'URL en double détectée : L'URL '" + url
                                    + "' (GET) est déjà associée à " + duplicate.getClassName() + "."
                                    + duplicate.getMethod()
                                    + "(). Impossible de la lier également à " + clazz.getName() + "."
                                    + method.getName() + "().");
                        }

                        Mapping mapping = new Mapping(clazz.getName(), method.getName());

                        urlMapping.put(urlKey, mapping);
                    }
                }
            }
        }
        return output;
    }
}
