package dao;

import model.Workout;
import util.CustomExceptions.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WorkoutDAO {

    public void addWorkout(Workout workout) throws DatabaseException {
        String sql = "INSERT INTO workouts (user_id, type, duration, intensity, date) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, workout.getUserId());
            stmt.setString(2, workout.getType());
            stmt.setInt(3, workout.getDuration());
            stmt.setString(4, workout.getIntensity());
            stmt.setDate(5, workout.getDate());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error adding workout: " + e.getMessage());
        }
    }

    public void updateWorkout(Workout workout) throws DatabaseException {
        String sql = "UPDATE workouts SET type = ?, duration = ?, intensity = ?, date = ? WHERE id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, workout.getType());
            stmt.setInt(2, workout.getDuration());
            stmt.setString(3, workout.getIntensity());
            stmt.setDate(4, workout.getDate());
            stmt.setInt(5, workout.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error updating workout: " + e.getMessage());
        }
    }

    public void deleteWorkout(int workoutId) throws DatabaseException {
        String sql = "DELETE FROM workouts WHERE id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, workoutId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error deleting workout: " + e.getMessage());
        }
    }

    public List<Workout> getWorkoutsByUser(int userId) throws DatabaseException {
        List<Workout> workouts = new ArrayList<>();
        String sql = "SELECT * FROM workouts WHERE user_id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                workouts.add(new Workout(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("type"),
                        rs.getInt("duration"),
                        rs.getString("intensity"),
                        rs.getDate("date")));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching workouts: " + e.getMessage());
        }
        return workouts;
    }
}
