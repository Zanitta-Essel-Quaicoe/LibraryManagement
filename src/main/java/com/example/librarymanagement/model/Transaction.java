package com.example.librarymanagement.model;

import java.time.LocalDateTime;

public class Transaction {
    private int id;
    private int bookId;
    private int patronId;
    private String transactionType;
    private LocalDateTime transactionDate;

    public Transaction(int id, int bookId, int patronId, String transactionType, LocalDateTime transactionDate) {
        this.id = id;
        this.bookId = bookId;
        this.patronId = patronId;
        this.transactionType = transactionType;
        this.transactionDate = transactionDate;
    }

    public int getId() {
        return id;
    }

    public int getBookId() {
        return bookId;
    }

    public int getPatronId() {
        return patronId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    // Convenience methods for display purposes
    public String getBookTitle() {
        return "Book ID: " + bookId; // Replace with actual title retrieval if needed
    }

    public String getPatronName() {
        return "Patron ID: " + patronId; // Replace with actual name retrieval if needed
    }
}
