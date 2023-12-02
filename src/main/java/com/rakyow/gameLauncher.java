package com.rakyow;

import com.rakyow.database.ConnexionSQLite;
import com.rakyow.punto.Game;

public class gameLauncher {
    public static void main(String[] args) {
        // Créez une nouvelle partie avec 2 joueurs
        Game game = new Game(2);
        ConnexionSQLite connexionSQLite = new ConnexionSQLite();
    }
}