package com.example.librarymanagement;

import java.sql.*;

public class MyJDBC {
    public static void main(String[] args){

        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/library_database",
                    "root",
                    "56789"
            );
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM USERS");

            while (resultSet.next()){
                System.out.println(resultSet.getString("username"));
                System.out.println(resultSet.getString("password"));
                System.out.println(resultSet.getInt("id"));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }


    }
}
