package com.example.librarymanagement.controller;

import com.example.librarymanagement.model.Patron;
import com.example.librarymanagement.config.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PatronsController {

    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private Label errorMessage;
    @FXML private TableView<Patron> patronsTable;
    @FXML private TableColumn<Patron, String> nameColumn;
    @FXML private TableColumn<Patron, Integer> idColumn;
    @FXML private TableColumn<Patron, String> emailColumn;
    @FXML private TableColumn<Patron, String> phoneColumn;

    private ObservableList<Patron> patronsList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Initialize table columns
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        //.setCellValueFactory(new PropertyValueFactory<>("id"));
        emailColumn.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        phoneColumn.setCellValueFactory(cellData -> cellData.getValue().phoneProperty());

        // Load existing patrons into the table
        loadPatrons();
    }

    private void loadPatrons() {
        try (Connection connection = DatabaseConnection.connectDatabase()) {
            String query = "SELECT * FROM patrons";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String phone = resultSet.getString("phone");
                System.out.println("ID: " + id);
                Patron patron = new Patron(id, name, email, phone);
                patronsList.add(patron);
            }
            patronsTable.setItems(patronsList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onAddPatron() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();

        // Validate inputs
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            errorMessage.setText("All fields are required.");
            errorMessage.setVisible(true);
            return;
        }

        try (Connection connection = DatabaseConnection.connectDatabase()) {
            String query = "INSERT INTO patrons (name, email, phone) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, phone);

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                // Add the new patron to the list and update the table
                Patron newPatron = new Patron(0, name, email, phone); // id will be auto-generated
                patronsList.add(newPatron);
                clearFields();
            }

            errorMessage.setVisible(false);
        } catch (SQLException e) {
            e.printStackTrace();
            errorMessage.setText("Failed to add the patron.");
            errorMessage.setVisible(true);
        }
    }

    private void clearFields() {
        nameField.clear();
        emailField.clear();
        phoneField.clear();
    }
}
