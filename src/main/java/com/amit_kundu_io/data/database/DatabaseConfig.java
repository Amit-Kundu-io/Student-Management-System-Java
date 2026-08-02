package com.amit_kundu_io.data.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {

    private static final String URL = "jdbc:mysql://localhost:3306/Students";

    private static final String USER = "root";
    private static final String PASSWORD = "113920";

    private static Connection connection;

    public static Connection getInstance() {

        try {

            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            }
            System.out.println("Database Initialize");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return connection;
    }
}