package com.example.librarymanagement.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.io.IOException;

public class MainWindowController {

    @FXML
    private StackPane contentArea;

    /**
     * Loads the Dashboard view.
     */
    @FXML
    public void loadDashboardView() {
        loadView("/com/example/librarymanagement/dashboardView.fxml");
    }

    /**
     * Loads the Manage Books view.
     */
    @FXML
    public void loadManageBooksView() {
        loadView("/com/example/librarymanagement/manage-books.fxml");
    }

    /**
     * Loads the Manage Patrons view.
     */
    @FXML
    public void loadManagePatronsView() {
        loadView("/com/example/librarymanagement/manage-patrons.fxml");
    }

    /**
     * Loads the Transactions view.
     */
    @FXML
    public void loadTransactionsView() {
        loadView("/com/example/librarymanagement/transactions.fxml");
    }

    /**
     * Logs out the admin and returns to the login screen.
     */
    @FXML
    public void logout() {
        try {
            // Load the login screen
            Parent loginView = FXMLLoader.load(getClass().getResource("/com/example/librarymanagement/Login.fxml"));
            Stage stage = (Stage) contentArea.getScene().getWindow();
            stage.getScene().setRoot(loginView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Helper method to load a view into the center content area.
     *
     * @param viewPath Path to the FXML file of the view to load.
     */
    private void loadView(String viewPath) {
        try {
            // Ensure the path is correct and resource exists
            Node view = FXMLLoader.load(getClass().getResource(viewPath));
            contentArea.getChildren().clear();
            contentArea.getChildren().add(view);
        } catch (IOException e) {
            e.printStackTrace();
            // Optionally display an alert or a placeholder view if loading fails
        }
    }
}
