<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Formulaire - Ajout d'Employé (Objet)</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 30px; }
        .form-group { margin-bottom: 15px; }
        label { display: inline-block; width: 150px; font-weight: bold; }
        input[type="text"], input[type="number"], input[type="date"] { padding: 5px; width: 250px; }
    </style>
</head>
<body>
    <h1>Formulaire d'enregistrement (Objet & Liste)</h1>

    <form action="${pageContext.request.contextPath}/employe-save-objet" method="post">
        
        <!-- Attributs de la classe Employe -->
        <div class="form-group">
            <label>Nom :</label>
            <input type="text" name="nom" required>
        </div>
        <div class="form-group">
            <label>Prénom :</label>
            <input type="text" name="prenom" required>
        </div>
        <div class="form-group">
            <label>Email :</label>
            <input type="text" name="email">
        </div>
        <div class="form-group">
            <label>Poste :</label>
            <input type="text" name="poste">
        </div>
        <div class="form-group">
            <label>Salaire :</label>
            <input type="number" step="0.01" name="salaire">
        </div>
        <div class="form-group">
            <label>Date d'embauche :</label>
            <input type="date" name="dateEmbauche">
        </div>

        <hr style="width: 40%; text-align: left;">

        <!-- Paramètre List<String> competences (plusieurs inputs avec le même nom 'competences') -->
        <h3>Compétences</h3>
        <div class="form-group">
            <label>Compétence 1 :</label>
            <input type="text" name="competences">
        </div>
        <div class="form-group">
            <label>Compétence 2 :</label>
            <input type="text" name="competences">
        </div>
        <div class="form-group">
            <label>Compétence 3 :</label>
            <input type="text" name="competences">
        </div>

        <br>
        <button type="submit" style="padding: 10px 20px;">Enregistrer l'employé</button>
    </form>
</body>
</html>