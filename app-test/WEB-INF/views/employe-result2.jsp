<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Vector, java.util.Map" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Résultat Vector & Map</title>
</head>
<body>
    <h1>Résultat de la requête (Vector & Map)</h1>
    
    <h3>Éléments reçus dans le Vector :</h3>
    <ul>
    <%
        Vector<String> vec = (Vector<String>) request.getAttribute("competencesVector");
        if (vec != null) {
            for (String s : vec) {
    %>
        <li><%= s %></li>
    <%      }
        }
    %>
    </ul>

    <h3>Éléments reçus dans la Map :</h3>
    <ul>
    <%
        Map<String, Object> map = (Map<String, Object>) request.getAttribute("metaData");
        if (map != null) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
    %>
        <li><strong><%= entry.getKey() %> :</strong> 
            <% 
                if (entry.getValue() instanceof String[]) {
                    out.print(java.util.Arrays.toString((String[]) entry.getValue()));
                } else {
                    out.print(entry.getValue());
                }
            %>
        </li>
    <%      }
        }
    %>
    </ul>

    <p><a href="${pageContext.request.contextPath}/employe-form-vector-map">Retour au formulaire</a></p>
</body>
</html>