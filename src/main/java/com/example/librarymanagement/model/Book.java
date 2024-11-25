package com.example.librarymanagement.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Book {
    private final StringProperty title;
    private final StringProperty author;
    private final StringProperty isbn;
    private final StringProperty year;
    private final int id;  // Adding an ID field to match the primary key of the table

    public Book(int id, String title, String author, String isbn, String year) {
        this.id = id;
        this.title = new SimpleStringProperty(title);
        this.author = new SimpleStringProperty(author);
        this.isbn = new SimpleStringProperty(isbn);
        this.year = new SimpleStringProperty(year);
    }

    // Getters for properties
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public String getAuthor() {
        return author.get();
    }

    public StringProperty authorProperty() {
        return author;
    }

    public String getIsbn() {
        return isbn.get();
    }

    public StringProperty isbnProperty() {
        return isbn;
    }

    public String getYear() {
        return year.get();
    }

    public StringProperty yearProperty() {
        return year;
    }
}
