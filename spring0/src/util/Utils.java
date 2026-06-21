package util;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
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
}
