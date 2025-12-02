package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import util.CustomExceptions.DatabaseException;

public class DBConnection {
    private static DBConnection instance;
    private Connection connection;
    private static final String URL = "jdbc:mysql://localhost:3306/fitness_tracker_db";
    private static final String USER = "root";
    private static final String PASSWORD = "Amazing123@"; // Placeholder, user should update

    private DBConnection() throws DatabaseException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            throw new DatabaseException("Failed to connect to database: " + e.getMessage());
        }
    }

    public static DBConnection getInstance() throws DatabaseException {
        if (instance == null) {
            instance = new DBConnection();
        } else {
            try {
                if (instance.getConnection().isClosed()) {
                    instance = new DBConnection();
                }
            } catch (SQLException e) {
                throw new DatabaseException("Database connection error: " + e.getMessage());
            }
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
