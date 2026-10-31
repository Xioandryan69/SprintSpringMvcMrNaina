<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Administration</title>
</head>
<body>
    <h1>${titre}</h1>
    <p style="color: blue;">Bienvenue sur le tableau de bord réservé aux administrateurs et superviseurs.</p>
    <p>Rôle en session : <strong>${sessionScope.userRole}</strong></p>
    <br>
    <p><a href="${pageContext.request.contextPath}/session-remove">Se déconnecter</a></p>
    <p><a href="${pageContext.request.contextPath}/employe-form">Retour au formulaire</a></p>
</body>
</html>