<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Employé enregistré</title>
</head>
<body>
    <h1>Employé enregistré</h1>
    <p>Nom : ${nom}</p>
    <p>Âge : ${age}</p>
    <p>Salaire : ${salaire}</p>
    <p>Actif : ${actif}</p>
    <p><a href="${pageContext.request.contextPath}/employe-form">Retour au formulaire</a></p>
</body>
</html>
