package com.example.librarymanagement.controller;

import com.example.librarymanagement.config.DatabaseConnection;
import com.example.librarymanagement.util.NavigationManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

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
            errorMessage.setStyle("-fx-text-fill: red;");
            errorMessage.setVisible(true);
        } else if (authenticate(username, password)) {
            errorMessage.setText("Login successful!");
            errorMessage.setStyle("-fx-text-fill: green;");
            errorMessage.setVisible(true);

            // Navigate to the main window
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/librarymanagement/main-window.fxml"));
                Parent mainWindow = loader.load();
                Stage stage = (Stage) usernameField.getScene().getWindow(); // Current stage
                stage.setScene(new Scene(mainWindow));
                stage.setWidth(800);
                stage.setHeight(600);
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
                errorMessage.setText("Unable to load the main window.");
                errorMessage.setStyle("-fx-text-fill: red;");
            }
        } else {
            errorMessage.setText("Invalid username or password.");
            errorMessage.setStyle("-fx-text-fill: red;");
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
        String query = "SELECT * FROM admin WHERE username = ?";

        try (Connection connection = DatabaseConnection.connectDatabase();
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Set parameters for the query
            statement.setString(1, username);

            // Execute the query
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String fetchedPassword = resultSet.getString("password");
                return fetchedPassword.equals(password);//BCrypt.checkpw(password, fetchedPassword);
            }
            return false;

        } catch (Exception e) {
            e.printStackTrace();
            errorMessage.setText("Database error. Please contact the administrator.");
            errorMessage.setStyle("-fx-text-fill: red;");
            return false;
        }
    }

    public void register() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        // Validate input fields
        if (username.isEmpty() || password.isEmpty()) {
            errorMessage.setText("Please fill in all fields.");
            errorMessage.setStyle("-fx-text-fill: red;");
            errorMessage.setVisible(true);
            return;
        }

        // Insert the new user into the database
        String query = "INSERT INTO admin (username, password) VALUES (?, ?)";

        try (Connection connection = DatabaseConnection.connectDatabase();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, username);
            statement.setString(2,password);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                errorMessage.setText("Registration successful!");
                errorMessage.setStyle("-fx-text-fill: green;");
                errorMessage.setVisible(true);

                // Optionally clear the fields after successful registration
                usernameField.clear();
                passwordField.clear();
                navigateToLoginButton();
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorMessage.setText("An error occurred. Please try again.");
            errorMessage.setStyle("-fx-text-fill: red;");
            errorMessage.setVisible(true);
        }
    }

    @FXML
    public void navigateToRegisterButton(){
        NavigationManager.navigateTo("/com/example/librarymanagement/register.fxml");
    }

    public void navigateToLoginButton(){
        NavigationManager.navigateTo("/com/example/librarymanagement/login.fxml");
    }

}
