<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Test Vector et Map</title>
</head>
<body>
    <h1>Test de Binding : Vector et Map</h1>
    <form action="${pageContext.request.contextPath}/employe-save-vector-map" method="post">
        <p>
            <label>Compétence Vector 1 : <input type="text" name="competencesVector" value="Java Avancé"></label>
        </p>
        <p>
            <label>Compétence Vector 2 : <input type="text" name="competencesVector" value="Architecture MVC"></label>
        </p>
        <p>
            <label>Paramètre libre Map (ex: projet) : <input type="text" name="projet" value="Sprint Framework"></label>
        </p>
        <button type="submit">Envoyer</button>
    </form>
</body>
</html>