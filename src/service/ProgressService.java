package service;

import dao.WorkoutDAO;
import model.Workout;
import util.CustomExceptions.DatabaseException;
import util.ThreadUtil;

import java.sql.Connection;
import java.util.List;

public class ProgressService {
    private WorkoutDAO workoutDAO;

    public ProgressService() {
        this.workoutDAO = new WorkoutDAO();
    }

    public void generateProgressReport(int userId) {
        ThreadUtil.runAsync(() -> {
            try (java.sql.Connection conn = dao.DBConnection.getInstance().getConnection()) {
                System.out.println("Generating progress report for User ID: " + userId + "...");
                List<Workout> workouts = workoutDAO.getWorkoutsByUser(userId, conn);
                int totalDuration = calculateTotalDuration(workouts);

                // Simulate processing time
                Thread.sleep(1000);
                System.out.println("Progress Report Ready: Total Workout Duration = " + totalDuration + " minutes.");
            } catch (DatabaseException | InterruptedException | java.sql.SQLException e) {
                System.err.println("Error generating progress report: " + e.getMessage());
            }
        });
    }

    public void updateProgress(int userId, Connection conn) throws DatabaseException {
        // Business logic: Calculate progress and potentially update a 'user_stats'
        // table
        // For now, we just calculate and log, as we don't have a stats table in the
        // schema provided yet
        List<Workout> workouts = workoutDAO.getWorkoutsByUser(userId, conn);
        int totalDuration = calculateTotalDuration(workouts);
        System.out.println("[Transaction] Updated progress for User " + userId + ": Total Duration = " + totalDuration);
    }

    private int calculateTotalDuration(List<Workout> workouts) {
        int totalDuration = 0;
        for (Workout w : workouts) {
            totalDuration += w.getDuration();
        }
        return totalDuration;
    }
}
