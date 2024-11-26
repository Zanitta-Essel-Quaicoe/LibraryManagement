package com.example.librarymanagement.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import com.example.librarymanagement.config.DatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DashboardController {
    @FXML
    private Label welcomeMessage;
    @FXML
    private Label totalBooksLabel;
    @FXML
    private Label availableBooksLabel;
    @FXML
    private Label borrowedBooksLabel;
    @FXML
    private Label totalPatronsLabel;

    public void initialize() {
        // Set the welcome message with the current date
        String currentDate = new SimpleDateFormat("MMMM dd, yyyy").format(new Date());
        welcomeMessage.setText("Welcome! Today is " + currentDate);

        // Fetch and display data
        loadDashboardData();
    }

    private void loadDashboardData() {
        try (Connection connection = DatabaseConnection.connectDatabase();
             Statement statement = connection.createStatement()) {

            // Total Books
            ResultSet totalBooksResult = statement.executeQuery("SELECT COUNT(*) AS total FROM books");
            if (totalBooksResult.next()) {
                totalBooksLabel.setText(String.valueOf(totalBooksResult.getInt("total")));
            }

            // Available Books
            ResultSet availableBooksResult = statement.executeQuery("SELECT COUNT(*) AS available FROM books WHERE status = 'AVAILABLE'");
            if (availableBooksResult.next()) {
                availableBooksLabel.setText(String.valueOf(availableBooksResult.getInt("available")));
            }

            // Borrowed Books
            ResultSet borrowedBooksResult = statement.executeQuery("SELECT COUNT(*) AS borrowed FROM books WHERE status = 'BORROWED'");
            if (borrowedBooksResult.next()) {
                borrowedBooksLabel.setText(String.valueOf(borrowedBooksResult.getInt("borrowed")));
            }

            // Total Patrons
            ResultSet totalPatronsResult = statement.executeQuery("SELECT COUNT(*) AS total FROM patrons");
            if (totalPatronsResult.next()) {
                totalPatronsLabel.setText(String.valueOf(totalPatronsResult.getInt("total")));
            }

        } catch (Exception e) {
            System.err.println("Error loading dashboard data: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
