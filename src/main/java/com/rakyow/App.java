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

            PuntoTitle();

            System.out.println("1. Play");
            System.out.println("2. Rules");
            System.out.println("3. Quit");

            System.out.print("\nEnter your choice : ");
            while (true) {
                if (sc.hasNextInt()) {
                    choice = sc.nextInt();
                    if (choice > 0 && choice < 4) {
                        break; 
                    } else {
                        System.out.println("Veuillez entrer 1, 2 ou 3 :");
                    }
                } else {
                    System.out.println("Veuillez entrer un entier valide :");
                    sc.next(); 
                }
            }
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
        System.out.println("Le jeu se joue avec 72 cartes carrées, chacune comportant 4 couleurs distinctes avec deux séries numérotées de 1 à 9 par couleur. Le but, pour 3 ou 4 joueurs, est de placer 4 cartes (5 pour 2 joueurs) de la même couleur côte-à-côte pour former une rangée, une colonne ou une diagonale.\r\n" + //
                "\r\n" + //
                "Chaque joueur reçoit un ensemble spécifique de cartes en fonction du nombre de joueurs. La partie démarre avec le joueur le plus jeune, qui retourne sa première carte au milieu de la table. Les cartes suivantes doivent être juxtaposées ou superposées à celles déjà posées, en respectant un carré maximum de 6x6 cartes.\r\n" + //
                "\r\n" + //
                "Le premier joueur à réussir à aligner les cartes requises remporte la manche. Si une série n'est pas réalisable, le joueur ayant réussi le plus de séries de 3 cartes (4 pour 2 joueurs) gagne. En cas d'égalité, la série avec le moins de points l'emporte.\r\n" + //
                "\r\n" + //
                "La partie se termine lorsqu'un joueur remporte deux manches, mais les règles permettent également de convenir d'un nombre différent de manches. Il est également possible de jouer en équipes pour 4 joueurs, avec pour objectif d'être la première équipe à poser 5 cartes de la même couleur dans une série.\r\n" + //
                "");
    }

    public static void PuntoTitle() {
        String espace = "\n--------------------------------------------------------\n ";
        String punto =
            "__________ ____ _____________________________ " +
            "\n\\______   \\    |   \\      \\__    ___/\\_____  \\" +
            "\n |     ___/    |   /   |   \\|    |    /   |   \\" +
            "\n |    |   |    |  /    |    \\    |   /    |    \\" +
            "\n |____|   |______/\\____|__  /____|   \\_______  /" +
            "\n                          \\/                 \\/";
        
        System.out.println(espace);
        System.out.println(punto);
    }
    

}
