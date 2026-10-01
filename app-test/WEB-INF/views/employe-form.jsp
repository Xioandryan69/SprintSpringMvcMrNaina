<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Sprint 7 - Binding</title>
</head>
<body>
    <h1>Sprint 7 - Formulaire</h1>

    <form action="${pageContext.request.contextPath}/employe-save" method="post">
        <p><label>Nom : <input type="text" name="nom"></label></p>
        <p><label>Age : <input type="number" name="age"></label></p>
        <p><label>Salaire : <input type="number" step="0.01" name="salaire"></label></p>
        <p><label>Actif : <input type="checkbox" name="actif"></label></p>
        <button type="submit">Enregistrer</button>
    </form>
</body>
</html>
