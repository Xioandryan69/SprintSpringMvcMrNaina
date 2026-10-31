package framework.util;

import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDate;
import java.lang.reflect.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Vector;
import java.util.Map;
import java.util.HashMap;

public class ParamBinder {

    public static Object[] buildArgs(Method method, HttpServletRequest request) throws Exception {
        Parameter[] params = method.getParameters();
        Object[] args = new Object[params.length];

        for (int i = 0; i < params.length; i++) {
            Parameter p = params[i];

            if (!p.isNamePresent()) {
                throw new Exception("Impossible de lire le nom du paramètre " + i + " de "
                        + method.getName() + "(). Compilez les contrôleurs avec : javac -parameters");
            }

            String name = p.getName();
            Class<?> type = p.getType();

            if (HttpServletRequest.class.isAssignableFrom(type)) {
                args[i] = request;
                continue;
            }
          
            if (MySession.class.isAssignableFrom(type)) {
                args[i] = new MySession(request.getSession());
                continue;
            }
            
            if (type == FileUpload.class) {
                args[i] = bindFile(request, name);
                continue;
            }

            if (java.util.List.class.isAssignableFrom(type) || java.util.Vector.class.isAssignableFrom(type)) {
                args[i] = bindList(p, request, name);
                continue;
            }

            if (java.util.Map.class.isAssignableFrom(type)) {
                args[i] = bindMap(request);
                continue;
            }

            if (isPrimitiveOrWrapper(type) || type == String.class) {
                String raw = request.getParameter(name);
                args[i] = convert(raw, type, name);
                continue;
            }
            args[i] = bindObject(type, request, name);
        }
        return args;
    }

    private static boolean isPrimitiveOrWrapper(Class<?> type) {
        return type.isPrimitive() || type == Integer.class || type == Long.class
                || type == Double.class || type == Float.class || type == Boolean.class
                || type == Short.class || type == Byte.class || type == Character.class;
    }

    private static Object bindObject(
            Class<?> clazz,
            HttpServletRequest request,
            String prefix) throws Exception {

        Constructor<?> constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);

        Object instance = constructor.newInstance();

        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);

            String fieldName = field.getName();

            String paramName = (prefix != null && !prefix.isEmpty())
                    ? prefix + "." + fieldName
                    : fieldName;

            String raw = request.getParameter(paramName);

            if (raw == null) {
                raw = request.getParameter(fieldName);
            }

            Class<?> fieldType = field.getType();

            if (isPrimitiveOrWrapper(fieldType)
                    || fieldType == String.class
                    || fieldType == LocalDate.class) {

                Object value = convert(raw, fieldType, fieldName);
                field.set(instance, value);

            } else {
                Object nestedObject = bindObject(fieldType, request, fieldName);

                field.set(instance, nestedObject);
            }
        }

        return instance;
    }

    private static Object bindList(Parameter p, HttpServletRequest request, String name) throws Exception {
        String[] values = request.getParameterValues(name);
        List<Object> list = new ArrayList<>();
        Vector<Object> vector = new Vector<>();
        boolean isVector = Vector.class.isAssignableFrom(p.getType());

        if (values != null) {
            // Récupérer le type générique (ex: List<Integer> -> Integer.class)
            Class<?> genericType = String.class;
            Type genericParamType = p.getParameterizedType();
            if (genericParamType instanceof ParameterizedType) {
                ParameterizedType pt = (ParameterizedType) genericParamType;
                Type actualType = pt.getActualTypeArguments()[0];
                if (actualType instanceof Class) {
                    genericType = (Class<?>) actualType;
                }
            }

            for (String val : values) {
                Object converted = convert(val, genericType, name);
                if (isVector) {
                    vector.add(converted);
                } else {
                    list.add(converted);
                }
            }
        }
        return isVector ? vector : list;
    }

    private static Map<String, Object> bindMap(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        Map<String, String[]> parameterMap = request.getParameterMap();
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            String[] vals = entry.getValue();
            if (vals.length == 1) {
                map.put(entry.getKey(), vals[0]);
            } else {
                map.put(entry.getKey(), vals);
            }
        }
        return map;
    }

    private static Object convert(String raw, Class<?> type, String name) throws Exception {
        if (type == String.class) {
            return raw;
        }

        // Champ absent ou vide : 0 / false pour un primitif, null pour un wrapper
        if (raw == null || raw.trim().isEmpty()) {
            return type.isPrimitive() ? defaultValue(type) : null;
        }

        String value = raw.trim();

        try {
            if (type == int.class || type == Integer.class)
                return Integer.valueOf(value);
            if (type == long.class || type == Long.class)
                return Long.valueOf(value);
            if (type == double.class || type == Double.class)
                return Double.valueOf(value);
            if (type == float.class || type == Float.class)
                return Float.valueOf(value);
            if (type == short.class || type == Short.class)
                return Short.valueOf(value);
            if (type == byte.class || type == Byte.class)
                return Byte.valueOf(value);
            if (type == char.class || type == Character.class)
                return Character.valueOf(value.charAt(0));
            if (type == boolean.class || type == Boolean.class) {
                // une checkbox cochée envoie "on" par défaut
                return value.equalsIgnoreCase("true") || value.equalsIgnoreCase("on")
                        || value.equals("1") || value.equalsIgnoreCase("yes");
            }
            if (type == LocalDate.class) {
                return LocalDate.parse(value);
            }
        } catch (NumberFormatException e) {
            throw new Exception("Paramètre '" + name + "' : la valeur \"" + value
                    + "\" n'est pas valide pour le type " + type.getSimpleName());
        }

        throw new Exception("Paramètre '" + name + "' : type non géré (" + type.getName()
                + "). Les objets seront traités dans un prochain sprint.");
    }

    private static Object defaultValue(Class<?> primitiveType) {
        return Array.get(Array.newInstance(primitiveType, 1), 0);
    }

    private static FileUpload bindFile(HttpServletRequest request, String name) throws Exception {
    jakarta.servlet.http.Part filePart = request.getPart(name);
    if (filePart == null || filePart.getSize() == 0) {
        return null;
    }

    String fileName = extractFileName(filePart);
    byte[] bytes = filePart.getInputStream().readAllBytes();
    long size = filePart.getSize();

    String uploadDir = (String) request.getServletContext().getAttribute("uploadDirectory");
    
    String savedPath = null;
    if (uploadDir != null) {
      
        java.io.File dir = new java.io.File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        savedPath = uploadDir + java.io.File.separator + fileName;
        java.nio.file.Files.write(java.nio.file.Paths.get(savedPath), bytes);
    }
    return new FileUpload(fileName, savedPath, bytes, size);

}

    private static String extractFileName(jakarta.servlet.http.Part part) {
        String contentDisp = part.getHeader("content-disposition");
        for (String s : contentDisp.split(";")) {
            if (s.trim().startsWith("filename")) {
                return s.substring(s.indexOf("=") + 2, s.length() - 1);
            }
        }
        return "file";
    }
}