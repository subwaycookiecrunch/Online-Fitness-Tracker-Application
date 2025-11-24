package dao;

import model.SystemSettings;
import util.CustomExceptions.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SettingsDAO {

    public void updateSetting(String key, String value) throws DatabaseException {
        String sql = "INSERT INTO system_settings (setting_key, setting_value) VALUES (?, ?) ON DUPLICATE KEY UPDATE setting_value = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, key);
            stmt.setString(2, value);
            stmt.setString(3, value);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error updating setting: " + e.getMessage());
        }
    }

    public List<SystemSettings> getAllSettings() throws DatabaseException {
        List<SystemSettings> settings = new ArrayList<>();
        String sql = "SELECT * FROM system_settings";
        try (Connection conn = DBConnection.getInstance().getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                settings.add(new SystemSettings(
                        rs.getInt("id"),
                        rs.getString("setting_key"),
                        rs.getString("setting_value")));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching settings: " + e.getMessage());
        }
        return settings;
    }
}
