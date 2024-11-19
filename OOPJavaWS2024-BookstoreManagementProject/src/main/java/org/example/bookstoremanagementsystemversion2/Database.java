package org.example.bookstoremanagementsystemversion2;

import java.sql.*;
import java.util.Objects;


public class Database {

    public static Connection connectToDatabase() {
        try {
            return DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/bookstore", "root", "Nghia@2000");
        } catch (SQLException e) {
            System.out.println("Database Connection Failed");
        }
        return null;
    }

    public static void updateQuery(String query, String[] arguments) {
        Connection connection = Database.connectToDatabase();
        try {
            PreparedStatement preparedStatement = Objects.requireNonNull(connection).prepareStatement(query);
            for (int i = 0; i < arguments.length; i++) {
                preparedStatement.setString(i + 1, arguments[i]);
            }

            preparedStatement.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static ResultSet selectQuery(String query, String[] arguments) {
        Connection connection = Database.connectToDatabase();
        ResultSet resultSet = null;
        try{
            PreparedStatement preparedStatement = Objects.requireNonNull(connection).prepareStatement(query);
            for (int i = 0; i < arguments.length; i++) {
                preparedStatement.setString(i + 1, arguments[i]);
            }

            resultSet = preparedStatement.executeQuery();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return resultSet;
    }

}
