package dao;

import model.Challenge;
import util.CustomExceptions.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChallengeDAO {

    public void addChallenge(Challenge challenge) throws DatabaseException {
        String sql = "INSERT INTO challenges (title, description, status) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, challenge.getTitle());
            stmt.setString(2, challenge.getDescription());
            stmt.setString(3, challenge.getStatus());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error adding challenge: " + e.getMessage());
        }
    }

    public void joinChallenge(int userId, int challengeId) throws DatabaseException {
        String sql = "INSERT INTO user_challenges (user_id, challenge_id, joined_on) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, challengeId);
            stmt.setDate(3, new java.sql.Date(System.currentTimeMillis()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error joining challenge: " + e.getMessage());
        }
    }

    public List<Challenge> getAllChallenges() throws DatabaseException {
        List<Challenge> challenges = new ArrayList<>();
        String sql = "SELECT * FROM challenges";
        try (Connection conn = DBConnection.getInstance().getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                challenges.add(new Challenge(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("status")));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching challenges: " + e.getMessage());
        }
        return challenges;
    }
}
