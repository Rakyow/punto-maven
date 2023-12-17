package com.rakyow;

import java.io.IOException;
import java.util.Scanner;

import com.rakyow.database.ConnexionMongoDB;
import com.rakyow.database.ConnexionMySQL;
import com.rakyow.database.ConnexionNEO4J;
import com.rakyow.database.ConnexionSQLite;

public class ConnexionLauncher {

    ConnexionMongoDB connexionMongoDB;
    ConnexionNEO4J connexionNEO4J;
    ConnexionMySQL connexionMySQL;
    ConnexionSQLite connexionSQLite;
    Scanner sc;

    public ConnexionLauncher(Scanner sc) throws IOException {
        this.sc = sc;
        connexionMongoDB = new ConnexionMongoDB();
        connexionNEO4J = new ConnexionNEO4J();
        connexionMySQL = new ConnexionMySQL();
        connexionSQLite = new ConnexionSQLite();
        this.launch();
    }

    public void launch() throws IOException{
        

        int choiceDB = 0;
        while(choiceDB != 4){

            Launcher.PuntoTitle();

            System.out.println("1. MongoDB");
            System.out.println("2. MySQL");
            System.out.println("3. SQLite");
            System.out.println("4. Back");

            System.out.print("\nEnter your choice : ");
            while (true) {
                if (this.sc.hasNextInt()) {
                    choiceDB = this.sc.nextInt();
                    if (choiceDB > 0 && choiceDB < 5) {
                        break; 
                    } else {
                        System.out.println("Veuillez entrer 1, 2, 3 ou 4 :");
                    }
                } else {
                    System.out.println("Veuillez entrer un entier valide :");
                    this.sc.next(); 
                }
            }
            switch(choiceDB){
                case 1:
                    mongoGestion();
                    break;
                case 2:
                    mysqlGestion();
                    break;
                case 3:
                    sqliteGestion();
                    break;
                case 4:
                    break;
                default:
                    break;
            }
        }
    }
    

    /**
     * This method is used to launch the gestion of MongoDB.
     * @param sc Scanner used to get the user input.
     * @param mongo MongoDB connexion
     * @throws IOException
     */
    private void mongoGestion() throws IOException {
        int choiceDB = 0;
        while(choiceDB != 4){

            Launcher.PuntoTitle();

            System.out.println("1. Voir les données");
            System.out.println("2. Ajouter des données");
            System.out.println("3. Supprimer toutes les données");
            System.out.println("4. Back");

            System.out.print("\nEnter your choice : ");
            while (true) {
                if (this.sc.hasNextInt()) {
                    choiceDB = this.sc.nextInt();
                    if (choiceDB > 0 && choiceDB < 5) {
                        break; 
                    } else {
                        System.out.println("Veuillez entrer 1, 2, 3 ou 4 :");
                    }
                } else {
                    System.out.println("Veuillez entrer un entier valide :");
                    sc.next(); 
                }
            }
            switch(choiceDB){
                case 1:
                    // mongoView();
                    break;
                case 2:
                    // mongoAddData();
                    break;
                case 3:
                    mongoDelete();
                    break;
                case 4:
                    break;
                default:
                    break;
            }
        }
    }

    private void mongoDelete() {
        this.connexionMongoDB.dropTable();
    }

    /**
     * This method is used to launch the gestion of MySQL.
     * @param sc Scanner used to get the user input.
     * @param mysql MySQL connexion
     */
    private void mysqlGestion() throws IOException{
        int choiceDB = 0;
        while(choiceDB != 4){

            Launcher.PuntoTitle();

            System.out.println("1. Voir les données");
            System.out.println("2. Ajouter des données");
            System.out.println("3. Supprimer toutes les données");
            System.out.println("4. Back");

            System.out.print("\nEnter your choice : ");
            while (true) {
                if (this.sc.hasNextInt()) {
                    choiceDB = this.sc.nextInt();
                    if (choiceDB > 0 && choiceDB < 5) {
                        break; 
                    } else {
                        System.out.println("Veuillez entrer 1, 2, 3 ou 4 :");
                    }
                } else {
                    System.out.println("Veuillez entrer un entier valide :");
                    sc.next(); 
                }
            }
            switch(choiceDB){
                case 1:
                    // mongoView();
                    break;
                case 2:
                    // mongoAddData();
                    break;
                case 3:
                    mysqlDelete();
                    break;
                default:
                    break;
            }
        }

            
    }

    private void mysqlDelete() {
        this.connexionMySQL.dropTable();
    }

    /**
     * This method is used to launch the gestion of SQLite.
     * @param sc Scanner used to get the user input.
     * @param sqlite SQLite connexion
     */
    private void sqliteGestion() throws IOException{
                
    }

    private void sqliteDelete() {
        this.connexionSQLite.dropTable();
    }
    
}
