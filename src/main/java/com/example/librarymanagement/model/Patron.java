package com.example.librarymanagement.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Patron {
    private final StringProperty name;
    private final StringProperty email;
    private final StringProperty phone;
    private final int id;

    public Patron(int id, String name, String email, String phone) {
        this.id = id;
        this.name = new SimpleStringProperty(name);
        this.email = new SimpleStringProperty(email);
        this.phone = new SimpleStringProperty(phone);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getEmail() {
        return email.get();
    }

    public StringProperty emailProperty() {
        return email;
    }

    public String getPhone() {
        return phone.get();
    }

    public StringProperty phoneProperty() {
        return phone;
    }
}
