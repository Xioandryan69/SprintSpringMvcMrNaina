<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Sprint 8 - Upload de fichier</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 30px; }
        .form-group { margin-bottom: 15px; }
        label { display: inline-block; width: 150px; font-weight: bold; }
    </style>
</head>
<body>
    <h1>Sprint 8 - Formulaire d'upload</h1>

    <form action="${pageContext.request.contextPath}/upload-save" method="post" enctype="multipart/form-data">
        <div class="form-group">
            <label>Description :</label>
            <input type="text" name="description" placeholder="Ex: Photo de profil">
        </div>
        <div class="form-group">
            <label>Fichier :</label>
            <input type="file" name="monFichier">
        </div>
        <button type="submit" style="padding: 10px 20px; background-color: #4CAF50; color: white; border: none; cursor: pointer;">
            Uploader le fichier
        </button>
    </form>
</body>
</html>