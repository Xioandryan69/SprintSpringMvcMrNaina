<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Résultat - Employé & Objet</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 30px; }
        ul { background: #f9f9f9; padding: 15px; border: 1px solid #ddd; width: 50%; }
        li { margin-bottom: 8px; }
    </style>
</head>
<body>
    <h1>Détails de l'employé enregistré (Objet)</h1>

    <h3>Informations personnelles :</h3>
    <ul>
        <li><strong>Nom :</strong> ${employe.nom}</li>
        <li><strong>Prénom :</strong> ${employe.prenom}</li>
        <li><strong>Email :</strong> ${employe.email}</li>
        <li><strong>Poste :</strong> ${employe.poste}</li>
        <li><strong>Salaire :</strong> ${employe.salaire}</li>
        <li><strong>Date d'embauche :</strong> ${employe.dateEmbauche}</li>
    </ul>

    <h3>Compétences :</h3>
    <ul>
        <% 
            List<String> comps = (List<String>) request.getAttribute("competences");
            if (comps != null && !comps.isEmpty()) {
                for (String c : comps) {
        %>
            <li><%= c %></li>
        <% 
                }
            } else { 
        %>
            <li>Aucune compétence sélectionnée</li>
        <% } %>
    </ul>

    <br>
    <p><a href="${pageContext.request.contextPath}/employe-form-objet">Retour au formulaire</a></p>
</body>
</html>