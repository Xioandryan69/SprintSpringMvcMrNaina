<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Connexion</title>
</head>
<body>
    <h1>Connexion au système</h1>
    <form action="${pageContext.request.contextPath}/login-submit" method="post">
        <p><label>Nom d'utilisateur : <input type="text" name="username" required></label></p>
        <p>
            <label>Rôle : 
                <select name="role">
                    <option value="ADMIN">ADMIN</option>
                    <option value="SUPERVISEUR">SUPERVISEUR</option>
                    <option value="USER">USER</option>
                </select>
            </label>
        </p>
        <button type="submit">Se connecter</button>
    </form>
</body>
</html>