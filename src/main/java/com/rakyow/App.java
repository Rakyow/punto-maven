package com.rakyow;

import java.util.Scanner;

/**
 * This class is the main class of the App.
 */
public class App {

    public static void main( String[] args ) {
        Scanner sc = new Scanner(System.in);
        int choice = 0;
        while(choice != 3){

            System.out.println("1. Play");
            System.out.println("2. Rules");
            System.out.println("3. Quit");

            choice = sc.nextInt();
            switch(choice){
                case 1:
                    play(sc);
                    break;
                case 2:
                    rules();
                    break;
                case 3:
                    break;
                default:
                    System.out.println("Please enter a valid number");
                    break;
            }
        }
    }

    /**
     * This method is used to launch the game.
     * @param sc Scanner used to get the user input.
     */
    public static void play(Scanner sc){
        GameLauncher gameLauncher = new GameLauncher(sc);
        gameLauncher.createGame(); 
    }

    /**
     * This method is used to display the rules.
     */
    public static void rules(){
        System.out.println("Rules");
    }
}
