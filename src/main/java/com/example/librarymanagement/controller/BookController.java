package com.example.librarymanagement.controller;

import com.example.librarymanagement.model.Book;
import com.example.librarymanagement.config.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookController {

    @FXML private TextField titleField;
    @FXML private TextField authorField;
    @FXML private TextField isbnField;
    @FXML private TextField yearField;
    @FXML private Label errorMessage;
    @FXML private TableView<Book> booksTable;
    @FXML private TableColumn<Book, String> titleColumn;
    @FXML private TableColumn<Book, String> authorColumn;
    @FXML private TableColumn<Book, String> yearColumn;
    @FXML private TableColumn<Book, String> isbnColumn;

    private ObservableList<Book> booksList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Initialize table columns
        titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        authorColumn.setCellValueFactory(cellData -> cellData.getValue().authorProperty());
        yearColumn.setCellValueFactory(cellData -> cellData.getValue().yearProperty());
        isbnColumn.setCellValueFactory(cellData -> cellData.getValue().isbnProperty());

        // Load existing books into the table
        loadBooks();
    }

    private void loadBooks() {
        try (Connection connection = DatabaseConnection.connectDatabase()) {
            String query = "SELECT * FROM books";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                String isbn = resultSet.getString("isbn");
                String year = resultSet.getString("publication_year");

                Book book = new Book(id, title, author, isbn, year);
                booksList.add(book);
            }
            booksTable.setItems(booksList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void addBook() {
        String title = titleField.getText().trim();
        String author = authorField.getText().trim();
        String isbn = isbnField.getText().trim();
        String year = yearField.getText().trim();

        // Validate inputs
        if (title.isEmpty() || author.isEmpty() || isbn.isEmpty() || year.isEmpty()) {
            errorMessage.setText("All fields are required.");
            errorMessage.setVisible(true);
            return;
        }

        try (Connection connection = DatabaseConnection.connectDatabase()) {
            String query = "INSERT INTO books (title, author, isbn, publication_year) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, title);
            statement.setString(2, author);
            statement.setString(3, isbn);
            statement.setString(4, year);

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                // Add the new book to the list and update the table
                Book newBook = new Book(0, title, author, isbn, year); // id will be auto-generated
                booksList.add(newBook);
                clearFields();
            }

            errorMessage.setVisible(false);
        } catch (SQLException e) {
            e.printStackTrace();
            errorMessage.setText("Failed to add the book.");
            errorMessage.setVisible(true);
        }
    }

    @FXML
    private void deleteBook() {
        Book selectedBook = booksTable.getSelectionModel().getSelectedItem();

        if (selectedBook == null) {
            errorMessage.setText("Please select a book to delete.");
            errorMessage.setVisible(true);
            return;
        }

        try (Connection connection = DatabaseConnection.connectDatabase()) {
            String query = "DELETE FROM books WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, selectedBook.getId());

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                booksList.remove(selectedBook);
                errorMessage.setVisible(false);
            } else {
                errorMessage.setText("Failed to delete the book.");
                errorMessage.setVisible(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            errorMessage.setText("Failed to delete the book.");
            errorMessage.setVisible(true);
        }
    }

    private void clearFields() {
        titleField.clear();
        authorField.clear();
        isbnField.clear();
        yearField.clear();
    }
}
