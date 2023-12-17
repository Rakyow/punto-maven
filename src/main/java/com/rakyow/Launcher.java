package com.rakyow;

import java.io.IOException;
import java.util.Scanner;

public class Launcher {

            Scanner sc;
    
    public Launcher() throws IOException {
        this.sc = new Scanner(System.in);   
        this.launch();
    }

    private void launch() throws IOException{

        int choice = 0;
        while(choice != 4){

            PuntoTitle();

            System.out.println("1. Play");
            System.out.println("2. Rules");
            System.out.println("3. Gestions des Bases");
            System.out.println("4. Quit");

            System.out.print("\nEnter your choice : ");
            
            while (true) {
                if (sc.hasNextInt()) {
                    choice = sc.nextInt();
                    if (choice > 0 && choice < 5) {
                        break; 
                    } else {
                        System.out.println("Veuillez entrer 1, 2, 3 ou 4 :");
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
                    Gestion();
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
    public void play(Scanner sc){
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

    /**
     * This method is used to display the gestion.
     * @throws IOException
     */
    public void Gestion() throws IOException {
        new ConnexionLauncher(this.sc);
    }

    public static void PuntoTitle() throws IOException {
        cleanTerminal();
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

    public static void cleanTerminal() {
        try {
            final String os = System.getProperty("os.name");
    
            if (os.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                Runtime.getRuntime().exec("clear");
            }
        } catch (final Exception e) {
            // Gérer toute exception liée à l'effacement de la console
            e.printStackTrace();
        }
    }
}
