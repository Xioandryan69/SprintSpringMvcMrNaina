<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Espace Sécurisé</title>
</head>
<body>
    <h1>Espace Sécurisé</h1>
    <p style="color: green;">${message}</p>
    <p>Utilisateur connecté : <strong>${sessionScope.userConnected}</strong></p>
    <br>
    <p><a href="${pageContext.request.contextPath}/session-remove">Se déconnecter</a></p>
    <p><a href="${pageContext.request.contextPath}/employe-form">Retour au formulaire</a></p>
</body>
</html>