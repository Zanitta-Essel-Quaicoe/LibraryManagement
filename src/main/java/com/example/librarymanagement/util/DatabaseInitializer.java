package com.example.librarymanagement.util;

import com.example.librarymanagement.config.DatabaseConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    public static void createTablesIfNotExist() {
        try (Connection connection = DatabaseConnection.connectDatabase()) {
            // Create tables if they don't exist
            createBooksTable(connection);
            createPatronsTable(connection);
            createTransactionsTable(connection);
            createAdminTable(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createBooksTable(Connection connection) throws SQLException {
        String createBooksTableQuery = "CREATE TABLE IF NOT EXISTS books ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "title VARCHAR(255) NOT NULL, "
                + "author VARCHAR(255) NOT NULL, "
                + "isbn VARCHAR(13) UNIQUE NOT NULL, "
                + "publication_year INT, "
                + "copies_available INT NOT NULL DEFAULT 0"
                + ");";

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(createBooksTableQuery);
            System.out.println("Books table created (if not exists).");
        }
    }

    private static void createPatronsTable(Connection connection) throws SQLException {
        String createPatronsTableQuery = "CREATE TABLE IF NOT EXISTS patrons ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "name VARCHAR(255) NOT NULL, "
                + "email VARCHAR(255) UNIQUE NOT NULL, "
                + "phone VARCHAR(15) "
                + ");";

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(createPatronsTableQuery);
            System.out.println("Patrons table created (if not exists).");
        }
    }

    private static void createTransactionsTable(Connection connection) throws SQLException {
        String createTransactionsTableQuery = "CREATE TABLE IF NOT EXISTS transactions ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "book_id INT, "
                + "patron_id INT, "
                + "transaction_type ENUM('BORROW', 'RETURN') NOT NULL, "
                + "transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                + "due_date TIMESTAMP, "
                + "return_date TIMESTAMP, "
                + "FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE, "
                + "FOREIGN KEY (patron_id) REFERENCES patrons(id) ON DELETE CASCADE"
                + ");";

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(createTransactionsTableQuery);
            System.out.println("Transactions table created (if not exists).");
        }
    }

    private static void createAdminTable(Connection connection) throws SQLException {
        String createAdminTableQuery = "CREATE TABLE IF NOT EXISTS admin ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "username VARCHAR(255) UNIQUE NOT NULL, "
                + "password VARCHAR(255) NOT NULL, "
                + "role VARCHAR(50) NOT NULL"
                + ");";

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(createAdminTableQuery);
            System.out.println("Admin table created (if not exists).");
        }
    }
}
