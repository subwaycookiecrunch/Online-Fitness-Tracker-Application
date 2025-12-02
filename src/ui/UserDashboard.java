package ui;

import model.Challenge;
import model.User;
import model.Workout;
import service.ProgressService;
import service.RecommendationService;
import ui.forms.AddWorkoutForm;
import ui.forms.EditProfileForm;
import ui.forms.EditWorkoutForm;
import util.CustomExceptions.DatabaseException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class UserDashboard extends JFrame {
    private User user;
    private service.WorkoutService workoutService;
    private service.ChallengeService challengeService;
    private service.UserService userService;
    private ProgressService progressService;
    private RecommendationService recommendationService;

    public UserDashboard(User user) {
        this.user = user;
        this.workoutService = new service.WorkoutService();
        this.challengeService = new service.ChallengeService();
        this.userService = new service.UserService();
        this.progressService = new ProgressService();
        this.recommendationService = new RecommendationService();

        setTitle("User Dashboard - " + user.getName());
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Workout Log", createWorkoutLogPanel());
        tabbedPane.addTab("Progress Tracking", createProgressPanel());
        tabbedPane.addTab("Fitness Challenges", createChallengesPanel());
        tabbedPane.addTab("Profile Management", createProfilePanel());
        tabbedPane.addTab("Challenge History", createHistoryPanel());

        add(tabbedPane);
        setVisible(true);
    }

    private JPanel createWorkoutLogPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] columns = { "ID", "Type", "Duration", "Intensity", "Date" };
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);

        refreshWorkoutTable(model);

        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Workout");
        addButton.addActionListener(e -> {
            new AddWorkoutForm(this, user).setVisible(true);
            refreshWorkoutTable(model);
        });
        buttonPanel.add(addButton);

        JButton editButton = new JButton("Edit Workout");
        editButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int id = (int) model.getValueAt(row, 0);
                String type = (String) model.getValueAt(row, 1);
                int duration = (int) model.getValueAt(row, 2);
                String intensity = (String) model.getValueAt(row, 3);
                java.sql.Date date = (java.sql.Date) model.getValueAt(row, 4);
                Workout w = new Workout(id, user.getId(), type, duration, intensity, date);
                new EditWorkoutForm(this, w).setVisible(true);
                refreshWorkoutTable(model);
            }
        });
        buttonPanel.add(editButton);

        JButton deleteButton = new JButton("Delete Workout");
        deleteButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int id = (int) model.getValueAt(row, 0);
                try {
                    workoutService.deleteWorkout(id);
                    refreshWorkoutTable(model);
                    JOptionPane.showMessageDialog(this, "Workout Deleted");
                } catch (DatabaseException ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }
            }
        });
        buttonPanel.add(deleteButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);
        return panel;
    }

    private void refreshWorkoutTable(DefaultTableModel model) {
        model.setRowCount(0);
        try {
            List<Workout> workouts = workoutService.getWorkoutsByUser(user.getId());
            for (Workout w : workouts) {
                model.addRow(new Object[] { w.getId(), w.getType(), w.getDuration(), w.getIntensity(), w.getDate() });
            }
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }

    private JPanel createProgressPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JTextArea reportArea = new JTextArea();
        reportArea.setEditable(false);
        panel.add(new JScrollPane(reportArea), BorderLayout.CENTER);

        JButton generateButton = new JButton("Generate Progress Report");
        generateButton.addActionListener(e -> {
            progressService.generateProgressReport(user.getId());
            reportArea.setText("Generating report in background... Check console for output.");
        });
        panel.add(generateButton, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createChallengesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] columns = { "ID", "Title", "Description", "Status" };
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);

        try {
            List<Challenge> challenges = challengeService.getAllChallenges();
            for (Challenge c : challenges) {
                model.addRow(new Object[] { c.getId(), c.getTitle(), c.getDescription(), c.getStatus() });
            }
        } catch (DatabaseException e) {
            e.printStackTrace();
        }

        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JButton joinButton = new JButton("Join Challenge");
        joinButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int challengeId = (int) model.getValueAt(row, 0);
                challengeService.joinChallenge(user.getId(), challengeId);
                JOptionPane.showMessageDialog(this, "Joined Challenge Successfully!");
            }
        });
        panel.add(joinButton, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createProfilePanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2));
        panel.add(new JLabel("Name: " + user.getName()));
        panel.add(new JLabel("Email: " + user.getEmail()));
        panel.add(new JLabel("Role: " + user.getRole()));

        JButton editButton = new JButton("Edit Profile");
        editButton.addActionListener(e -> new EditProfileForm(this, user).setVisible(true));
        panel.add(editButton);

        return panel;
    }

    private JPanel createHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] columns = { "Challenge", "Joined On" };
        Object[][] data = {
                { "Marathon Prep", "2023-01-15" },
                { "30 Day Yoga", "2023-02-01" }
        };
        JTable table = new JTable(data, columns);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }
}
