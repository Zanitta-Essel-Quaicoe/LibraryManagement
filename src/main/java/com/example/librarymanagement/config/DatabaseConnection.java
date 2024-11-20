package com.example.librarymanagement.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String DB_URL="jdbc:mysql://127.0.0.1:3306/library_database";
    private static final String DB_USERNAME="root";
    private static final String DB_PASSWORD="56789";

    public  static Connection connectDatabase() throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        return connection;
    }
}
