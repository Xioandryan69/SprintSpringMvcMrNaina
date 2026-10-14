-- 1. Création de l'utilisateur
CREATE USER 'admin'@'localhost' IDENTIFIED BY 'admin123';

-- 2. Attribution de tous les privilèges sur toutes les bases et tables
GRANT ALL PRIVILEGES ON *.* TO 'admin'@'localhost' WITH GRANT OPTION;

-- 3. Rechargement des privilèges pour appliquer les modifications
FLUSH PRIVILEGES;