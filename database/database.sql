-- 1. Création de la base de données
CREATE DATABASE IF NOT EXISTS entreprise_db
    CHARACTER SET utf8mb4 
    COLLATE utf8mb4_unicode_ci;

-- 2. Sélection de la base de données
USE entreprise_db;

-- 3. Création de la table 'employe'
CREATE TABLE IF NOT EXISTS employe (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE,
    poste VARCHAR(100),
    salaire DOUBLE,
    date_embauche DATE DEFAULT (CURRENT_DATE)
);

-- 4. Insertion de données de test
INSERT INTO employe (nom, prenom, email, poste, salaire) VALUES
('Rabe', 'Soa', 'soa.rabe@example.com', 'Développeur Java', 2500000.00),
('Rakoto', 'Jean', 'jean.rakoto@example.com', 'Chef de Projet', 3500000.00),
('Andria', 'Mina', 'mina.andria@example.com', 'Designer UX', 2000000.00);