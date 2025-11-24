package dao;

import model.FitnessContent;
import util.CustomExceptions.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContentDAO {

    public void submitContent(FitnessContent content) throws DatabaseException {
        String sql = "INSERT INTO fitness_content (title, description, status) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, content.getTitle());
            stmt.setString(2, content.getDescription());
            stmt.setString(3, "PENDING");
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error submitting content: " + e.getMessage());
        }
    }

    public void approveContent(int contentId) throws DatabaseException {
        updateContentStatus(contentId, "APPROVED");
    }

    public void rejectContent(int contentId) throws DatabaseException {
        updateContentStatus(contentId, "REJECTED");
    }

    private void updateContentStatus(int contentId, String status) throws DatabaseException {
        String sql = "UPDATE fitness_content SET status = ? WHERE id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, contentId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error updating content status: " + e.getMessage());
        }
    }

    public List<FitnessContent> getPendingContent() throws DatabaseException {
        List<FitnessContent> contentList = new ArrayList<>();
        String sql = "SELECT * FROM fitness_content WHERE status = 'PENDING'";
        try (Connection conn = DBConnection.getInstance().getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                contentList.add(new FitnessContent(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("status")));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching pending content: " + e.getMessage());
        }
        return contentList;
    }
}
