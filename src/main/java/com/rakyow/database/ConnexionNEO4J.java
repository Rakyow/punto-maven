
package com.rakyow.database;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Session;

public class ConnexionNEO4J {

    private Driver driver;

    public ConnexionNEO4J() {
        String uri = "bolt://localhost:7687";
        String user = "neo4j";
        String password = "Azerty123";

        driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
        System.out.println("Connexion réussi !");
        createGraph();
    }

    public Driver getDriver() {
        return driver;
    }

    public void createGraph() {
        try (Session session = driver.session()) {

            session.writeTransaction(tx -> {
                tx.run("CREATE (:Player {name: 'Alice'})");
                tx.run("CREATE (:Player {name: 'Bob'})");

                return null;
            });
        }
    }

    public void close() {
        if (driver != null) {
            driver.close();
        }
    }
}
