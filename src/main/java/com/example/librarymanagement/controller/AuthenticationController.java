package com.example.librarymanagement.controller;

import com.example.librarymanagement.config.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AuthenticationController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorMessage;

    /**
     * Handles the login action when the "Login" button is clicked.
     */
    @FXML
    public void login() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            errorMessage.setText("Please fill in all fields.");
            errorMessage.setVisible(true);
        } else if (authenticate(username, password)) {
            errorMessage.setText("Login successful!");
            errorMessage.setStyle("-fx-text-fill: green;");
            errorMessage.setVisible(true);
            // Additional logic for successful login
        } else {
            errorMessage.setText("Invalid username or password.");
            errorMessage.setVisible(true);
        }
    }

    /**
     * Authenticates the user with the provided credentials.
     *
     * @param username The username entered by the user.
     * @param password The password entered by the user.
     * @return true if the credentials are valid, false otherwise.
     */
    private boolean authenticate(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ?";

        try (Connection connection = DatabaseConnection.connectDatabase();
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Set parameters for the query
            statement.setString(1, username);

            // Execute the query
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()){
                String fetchedPassword = resultSet.getString("password");
                System.out.println("password: " + fetchedPassword);
                return fetchedPassword.equals(password);
            }

            // If a matching record exists, authentication is successful
            return false;
           // return resultSet.next();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    // Additional methods for registration, password reset, etc., can go here
}
