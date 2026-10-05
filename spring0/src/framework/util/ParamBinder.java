package framework.util;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * Sprint 7 : binding formulaire -> framework -> instance.
 *
 * Pour chaque paramètre de la méthode du contrôleur, on cherche dans la requête
 * le champ de formulaire qui porte le MÊME NOM, puis on convertit la valeur
 * (toujours reçue en String) vers le type du paramètre.
 *
 * Types gérés pour l'instant : String, types primitifs et leurs wrappers.
 * Les objets (classes personnalisées) ne sont pas encore gérés.
 *
 * IMPORTANT : les contrôleurs doivent être compilés avec "javac -parameters",
 * sinon Java ne garde pas les vrais noms (arg0, arg1...).
 */
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
            String raw = request.getParameter(name); // null si le champ n'existe pas dans la requête
            args[i] = convert(raw, p.getType(), name);
        }
        return args;
    }

    private static Object convert(String raw, Class<?> type, String name) throws Exception {
        // String : on renvoie la valeur telle quelle (null si absente)
        if (type == String.class) {
            return raw;
        }

        // Champ absent ou vide : 0 / false pour un primitif, null pour un wrapper
        if (raw == null || raw.trim().isEmpty()) {
            return type.isPrimitive() ? defaultValue(type) : null;
        }

        String value = raw.trim();

        try {
            if (type == int.class || type == Integer.class) return Integer.valueOf(value);
            if (type == long.class || type == Long.class) return Long.valueOf(value);
            if (type == double.class || type == Double.class) return Double.valueOf(value);
            if (type == float.class || type == Float.class) return Float.valueOf(value);
            if (type == short.class || type == Short.class) return Short.valueOf(value);
            if (type == byte.class || type == Byte.class) return Byte.valueOf(value);
            if (type == char.class || type == Character.class) return Character.valueOf(value.charAt(0));
            if (type == boolean.class || type == Boolean.class) {
                // une checkbox cochée envoie "on" par défaut
                return value.equalsIgnoreCase("true") || value.equalsIgnoreCase("on")
                        || value.equals("1") || value.equalsIgnoreCase("yes");
            }
        } catch (NumberFormatException e) {
            throw new Exception("Paramètre '" + name + "' : la valeur \"" + value
                    + "\" n'est pas valide pour le type " + type.getSimpleName());
        }

        throw new Exception("Paramètre '" + name + "' : type non géré (" + type.getName()
                + "). Les objets seront traités dans un prochain sprint.");
    }

    // Valeur par défaut d'un primitif (int -> 0, boolean -> false, ...)
    private static Object defaultValue(Class<?> primitiveType) {
        return Array.get(Array.newInstance(primitiveType, 1), 0);
    }
}