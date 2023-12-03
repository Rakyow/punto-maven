package com.rakyow;

import com.rakyow.punto.Game;

import java.util.Scanner;

/**
 * This class is used to launch the game.
 */
public class GameLauncher {

    Scanner sc; // Scanner used to get the user input.

    /**
     * This constructor is used to create a GameLauncher.
     * @param sc Scanner used to get the user input.
     */
    public GameLauncher(Scanner sc) {
        this.sc = sc;
    }

    /**
     * This method is used to create a Game.
     */
    public void createGame() {
        Game game = new Game(sc);
        game.start();
    }
}