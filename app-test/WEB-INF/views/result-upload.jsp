<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Sprint 8 - Résultat de l'Upload</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 30px; }
        ul { background: #f9f9f9; padding: 15px; border: 1px solid #ddd; width: 50%; }
        li { margin-bottom: 8px; }
    </style>
</head>
<body>
    <h1>Fichier uploadé avec succès !</h1>

    <h3>Détails :</h3>
    <ul>
        <li><strong>Description :</strong> ${description}</li>
        <li><strong>Nom du fichier :</strong> ${nomFichier}</li>
        <li><strong>Taille du fichier :</strong> ${tailleFichier} octets</li>
    </ul>

    <br>
    <p><a href="${pageContext.request.contextPath}/upload-form">Retour au formulaire d'upload</a></p>
</body>
</html>