package com.example.librarymanagement.controller;

import com.example.librarymanagement.model.Transaction;
import com.example.librarymanagement.config.DatabaseConnection;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.*;
import java.time.LocalDateTime;

public class TransactionsController {

    @FXML private TableView<Transaction> transactionsTable;
    @FXML private TableColumn<Transaction, Integer> transactionIdColumn;
    @FXML private TableColumn<Transaction, String> patronColumn;
    @FXML private TableColumn<Transaction, String> bookColumn;
    @FXML private TableColumn<Transaction, String> typeColumn;
    @FXML private TableColumn<Transaction, LocalDateTime> dateColumn;

    @FXML private ComboBox<String> patronComboBox;
    @FXML private ComboBox<String> bookComboBox;
    @FXML private ComboBox<String> transactionTypeComboBox;

    private final ObservableList<Transaction> transactionList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Set up table columns
        //transactionIdColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        patronColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPatronName()));
        bookColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBookTitle()));
        typeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTransactionType()));
        dateColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getTransactionDate()));

        // Populate transaction types
        transactionTypeComboBox.getItems().addAll("borrow", "return");

        // Load data
        loadTransactions();
        loadPatrons();
        loadBooks();
    }

    private void loadTransactions() {
        transactionList.clear();
        try (Connection connection = DatabaseConnection.connectDatabase()) {
            String query = """
                SELECT t.id, t.book_id, t.patron_id, t.transaction_type, t.transaction_date, 
                       b.title AS book_title, p.name AS patron_name
                FROM transactions t
                JOIN books b ON t.book_id = b.id
                JOIN patrons p ON t.patron_id = p.id
                """;
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int bookId = resultSet.getInt("book_id");
                int patronId = resultSet.getInt("patron_id");
                String transactionType = resultSet.getString("transaction_type");
                LocalDateTime transactionDate = resultSet.getTimestamp("transaction_date").toLocalDateTime();

                Transaction transaction = new Transaction(id, bookId, patronId, transactionType, transactionDate);
                transactionList.add(transaction);
            }
            transactionsTable.setItems(transactionList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadPatrons() {
        try (Connection connection = DatabaseConnection.connectDatabase()) {
            String query = "SELECT id, name FROM patrons";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                patronComboBox.getItems().add(id + " - " + name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadBooks() {
        try (Connection connection = DatabaseConnection.connectDatabase()) {
            String query = "SELECT id, title FROM books";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                bookComboBox.getItems().add(id + " - " + title);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void recordTransaction() {
        String patronSelection = patronComboBox.getValue();
        String bookSelection = bookComboBox.getValue();
        String transactionType = transactionTypeComboBox.getValue();

        if (patronSelection == null || bookSelection == null || transactionType == null) {
            showAlert("Validation Error", "Please select a patron, book, and transaction type.");
            return;
        }

        int patronId = Integer.parseInt(patronSelection.split(" - ")[0]);
        int bookId = Integer.parseInt(bookSelection.split(" - ")[0]);

        try (Connection connection = DatabaseConnection.connectDatabase()) {
            String query = "INSERT INTO transactions (patron_id, book_id, transaction_type) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, patronId);
            statement.setInt(2, bookId);
            statement.setString(3, transactionType);

            statement.executeUpdate();
            loadTransactions();
            showAlert("Success", "Transaction recorded successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to record transaction. Please try again.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
