package service;

import dao.DBConnection;
import dao.WorkoutDAO;
import model.Workout;
import util.CustomExceptions.DatabaseException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class WorkoutService {
    private WorkoutDAO workoutDAO;
    private ProgressService progressService;
    private GoalService goalService;

    public WorkoutService() {
        this.workoutDAO = new WorkoutDAO();
        this.progressService = new ProgressService();
        this.goalService = new GoalService();
    }

    public void logWorkout(Workout workout) throws DatabaseException {
        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getConnection();
            conn.setAutoCommit(false); // Start Transaction

            // 1. Add Workout
            workoutDAO.addWorkout(workout, conn);

            // 2. Update Progress (Business Logic)
            progressService.updateProgress(workout.getUserId(), conn);

            // 3. Check Goals (Business Logic)
            goalService.checkGoals(workout.getUserId(), conn);

            conn.commit(); // Commit Transaction
            System.out.println("[Transaction] Workout logged and progress updated successfully.");

        } catch (SQLException | DatabaseException e) {
            rollback(conn);
            throw new DatabaseException("Failed to log workout: " + e.getMessage());
        } finally {
            close(conn);
        }
    }

    public void updateWorkout(Workout workout) throws DatabaseException {
        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getConnection();
            conn.setAutoCommit(false); // Start Transaction

            // 1. Update Workout
            workoutDAO.updateWorkout(workout, conn);

            // 2. Update Progress (Recalculate as duration might have changed)
            progressService.updateProgress(workout.getUserId(), conn);

            conn.commit();
            System.out.println("[Transaction] Workout updated and progress recalculated.");

        } catch (SQLException | DatabaseException e) {
            rollback(conn);
            throw new DatabaseException("Failed to update workout: " + e.getMessage());
        } finally {
            close(conn);
        }
    }

    public void deleteWorkout(int workoutId) throws DatabaseException {
        // Simple delete, but could be part of a transaction if we needed to update
        // stats
        // For now, just delegate, but keep it in Service for MVC
        workoutDAO.deleteWorkout(workoutId);
    }

    public List<Workout> getWorkoutsByUser(int userId) throws DatabaseException {
        return workoutDAO.getWorkoutsByUser(userId);
    }

    private void rollback(Connection conn) {
        if (conn != null) {
            try {
                conn.rollback();
                System.err.println("[Transaction] Rolled back.");
            } catch (SQLException ex) {
                System.err.println("Error rolling back: " + ex.getMessage());
            }
        }
    }

    private void close(Connection conn) {
        if (conn != null) {
            try {
                conn.setAutoCommit(true);
                conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}
