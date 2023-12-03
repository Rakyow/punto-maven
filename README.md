# Punto - Jeu de cartes en Java

Punto est un jeu de cartes en Java, implémenté en utilisant Maven comme système de gestion de projet.

## Instructions d'utilisation

### Installation

1. **Prérequis :**
   - Assurez-vous d'avoir Java JDK 11 ou supérieur installé sur votre machine.
   - Vérifiez que Maven est installé en exécutant `mvn -version` dans votre terminal.

2. **Clonage du dépôt :**
   - Clonez ce dépôt en utilisant `git clone https://github.com/Rakyow/punto-maven.git`.

3. **Création des bases :**
   - Pensez bien à lancer les serveurs MongoDB et MySQL
   - Renseignez le bon URL pour MongoDB et MySQL ainsi que les bons identifiants pour MySQL

4. **Compilation du projet :**
   - Allez dans le répertoire du projet.
   - Exécutez `mvn clean install` pour compiler le projet.

5. **Lancement du jeu :**
   - Exécutez `java -jar target/punto-1.0.jar` pour démarrer le jeu.

## Structure du projet

Le projet est organisé comme suit :

- `/src`: Contient le code source Java du jeu.
  - `/com/rakyow`: Packages principaux du jeu.
  - `/punto`: Contient les classes principales du jeu.
  - `/main.java`: Classe principale pour lancer le jeu.

## Auteur

Ce jeu a été développé par Stévan JEANNE.

