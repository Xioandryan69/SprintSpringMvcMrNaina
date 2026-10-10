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

    <!-- Section Liste dynamique pour les compétences -->
        <h3>Compétences</h3>
        <div id="conteneur-competences">
            <!-- Une première ligne par défaut -->
            <div class="competence-ligne">
                <input type="text" name="competences" placeholder="Ex: Java">
                <button type="button" onclick="this.parentElement.remove()">Supprimer</button>
            </div>
        </div>
        
        <button type="button" id="ajouter" style="margin-top: 10px;">+ Ajouter une compétence</button>

        <br><br>
        <button type="submit" style="padding: 10px 20px; background-color: #4CAF50; color: white; border: none; cursor: pointer;">
            Enregistrer l'employé
        </button>

        <br>
        <button type="submit" style="padding: 10px 20px;">Enregistrer l'employé</button>
    </form>
</body>

    <script>
        const conteneur = document.getElementById('conteneur-competences');
        const btnAjouter = document.getElementById('ajouter');

        btnAjouter.addEventListener('click', () => {
            // 1. Créer un conteneur pour la ligne (input + bouton supprimer)
            const ligne = document.createElement('div');
            ligne.className = 'competence-ligne';
            ligne.style.margin = '8px 0';

            // 2. Créer l'input avec le même name="competences" pour que le ParamBinder les regroupe dans la List
            const input = document.createElement('input');
            input.type = 'text';
            input.name = 'competences';
            input.placeholder = 'Nouvelle compétence';
            input.style.marginRight = '10px';

            // 3. Créer le bouton Supprimer
            const btnSupprimer = document.createElement('button');
            btnSupprimer.type = 'button';
            btnSupprimer.textContent = 'Supprimer';

            // 4. Ajouter l'action de suppression au clic
            btnSupprimer.addEventListener('click', () => {
                ligne.remove(); // Supprime la ligne entière du DOM
            });

            // 5. Assembler les éléments
            ligne.appendChild(input);
            ligne.appendChild(btnSupprimer);
            conteneur.appendChild(ligne);
        });
    </script>
</html>