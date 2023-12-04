package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDatabase {
    public static void main(String[] args) {
        ConnectDatabase connectDatabase = new ConnectDatabase();

        try (Connection connection = connectDatabase.openConnection()) {
            if (connection != null) {
                System.out.println("Connexion à la base de données réussie !");

            } else {
                System.out.println("La connexion à la base de données a échoué.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public Connection openConnection() throws SQLException {
        String dbUrl = System.getenv("DB_URL");
        String dbUser = System.getenv("DB_USER");
        String dbPassword = System.getenv("DB_PASSWORD");

        try {
            return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        } catch (SQLException e) {
            System.out.println("Erreur lors de la connexion à la base de données." + e);
            throw e;
        }
    }
}
