package dao;

import model.User;
import model.Admin;
import model.NormalUser;
import util.CustomExceptions.DatabaseException;
import util.CustomExceptions.UserNotFoundException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public void addUser(User user) throws DatabaseException {
        try (Connection conn = DBConnection.getInstance().getConnection()) {
            addUser(user, conn);
        } catch (SQLException e) {
            throw new DatabaseException("Error adding user: " + e.getMessage());
        }
    }

    public void addUser(User user, Connection conn) throws DatabaseException {
        String sql = "INSERT INTO users (name, email, password, role) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getRole());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error adding user: " + e.getMessage());
        }
    }

    public void updateUser(User user) throws DatabaseException {
        try (Connection conn = DBConnection.getInstance().getConnection()) {
            updateUser(user, conn);
        } catch (SQLException e) {
            throw new DatabaseException("Error updating user: " + e.getMessage());
        }
    }

    public void updateUser(User user, Connection conn) throws DatabaseException {
        String sql = "UPDATE users SET name = ?, email = ?, password = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setInt(4, user.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error updating user: " + e.getMessage());
        }
    }

    public void deleteUser(int userId) throws DatabaseException {
        try (Connection conn = DBConnection.getInstance().getConnection()) {
            deleteUser(userId, conn);
        } catch (SQLException e) {
            throw new DatabaseException("Error deleting user: " + e.getMessage());
        }
    }

    public void deleteUser(int userId, Connection conn) throws DatabaseException {
        String sql = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error deleting user: " + e.getMessage());
        }
    }

    public User getUserByEmail(String email) throws DatabaseException, UserNotFoundException {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String role = rs.getString("role");
                if ("ADMIN".equalsIgnoreCase(role)) {
                    return new Admin(rs.getInt("id"), rs.getString("name"), rs.getString("email"),
                            rs.getString("password"));
                } else {
                    return new NormalUser(rs.getInt("id"), rs.getString("name"), rs.getString("email"),
                            rs.getString("password"));
                }
            } else {
                throw new UserNotFoundException("User not found with email: " + email);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching user: " + e.getMessage());
        }
    }

    public List<User> getAllUsers() throws DatabaseException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Connection conn = DBConnection.getInstance().getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String role = rs.getString("role");
                if ("ADMIN".equalsIgnoreCase(role)) {
                    users.add(new Admin(rs.getInt("id"), rs.getString("name"), rs.getString("email"),
                            rs.getString("password")));
                } else {
                    users.add(new NormalUser(rs.getInt("id"), rs.getString("name"), rs.getString("email"),
                            rs.getString("password")));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching all users: " + e.getMessage());
        }
        return users;
    }
}
