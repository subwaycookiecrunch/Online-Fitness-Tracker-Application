package service;

import dao.WorkoutDAO;
import model.Workout;
import util.CustomExceptions.DatabaseException;
import util.ThreadUtil;

import java.util.List;

public class ProgressService {
    private WorkoutDAO workoutDAO;

    public ProgressService() {
        this.workoutDAO = new WorkoutDAO();
    }

    public void generateProgressReport(int userId) {
        ThreadUtil.runAsync(() -> {
            try {
                System.out.println("Generating progress report for User ID: " + userId + "...");
                List<Workout> workouts = workoutDAO.getWorkoutsByUser(userId);
                int totalDuration = 0;
                for (Workout w : workouts) {
                    totalDuration += w.getDuration();
                }
                // Simulate processing time
                Thread.sleep(1000);
                System.out.println("Progress Report Ready: Total Workout Duration = " + totalDuration + " minutes.");
            } catch (DatabaseException | InterruptedException e) {
                System.err.println("Error generating progress report: " + e.getMessage());
            }
        });
    }
}
