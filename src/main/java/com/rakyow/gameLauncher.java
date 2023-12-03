package com.rakyow;

import com.rakyow.punto.Game;

import java.util.Scanner;

public class GameLauncher {

    Scanner sc;

    public GameLauncher(Scanner sc) {
        this.sc = sc;
    }

    public void createGame() {
        Game game = new Game(sc);
        game.start();
    }
}