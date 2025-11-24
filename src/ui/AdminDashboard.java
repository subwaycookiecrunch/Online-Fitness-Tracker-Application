package ui;

import dao.ContentDAO;
import dao.SettingsDAO;
import dao.UserDAO;
import model.FitnessContent;
import model.SystemSettings;
import model.User;
import ui.forms.ContentApprovalForm;
import util.CustomExceptions.DatabaseException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminDashboard extends JFrame {
    private User adminUser;
    private UserDAO userDAO;
    private ContentDAO contentDAO;
    private SettingsDAO settingsDAO;

    public AdminDashboard(User user) {
        this.adminUser = user;
        this.userDAO = new UserDAO();
        this.contentDAO = new ContentDAO();
        this.settingsDAO = new SettingsDAO();

        setTitle("Admin Dashboard - " + user.getName());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("User Management", createUserManagementPanel());
        tabbedPane.addTab("Fitness Content", createContentManagementPanel());
        tabbedPane.addTab("System Settings", createSettingsPanel());
        tabbedPane.addTab("Fitness Statistics", createStatsPanel());
        tabbedPane.addTab("Activity Logs", createLogsPanel());

        add(tabbedPane);
        setVisible(true);
    }

    private JPanel createUserManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] columns = { "ID", "Name", "Email", "Role" };
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);

        try {
            List<User> users = userDAO.getAllUsers();
            for (User u : users) {
                model.addRow(new Object[] { u.getId(), u.getName(), u.getEmail(), u.getRole() });
            }
        } catch (DatabaseException e) {
            e.printStackTrace();
        }

        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton deleteButton = new JButton("Delete User");
        deleteButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int userId = (int) model.getValueAt(row, 0);
                try {
                    userDAO.deleteUser(userId);
                    model.removeRow(row);
                    JOptionPane.showMessageDialog(this, "User Deleted");
                } catch (DatabaseException ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }
            }
        });
        buttonPanel.add(deleteButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createContentManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] columns = { "ID", "Title", "Status" };
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);

        try {
            List<FitnessContent> contents = contentDAO.getPendingContent();
            for (FitnessContent c : contents) {
                model.addRow(new Object[] { c.getId(), c.getTitle(), c.getStatus() });
            }
        } catch (DatabaseException e) {
            e.printStackTrace();
        }

        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton reviewButton = new JButton("Review Content");
        reviewButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int contentId = (int) model.getValueAt(row, 0);
                String title = (String) model.getValueAt(row, 1);
                // Fetch full content object (simplified here)
                FitnessContent content = new FitnessContent(contentId, title, "Description Placeholder", "PENDING");
                new ContentApprovalForm(this, content).setVisible(true);
                // Refresh table logic would go here
            }
        });
        buttonPanel.add(reviewButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createSettingsPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 2));
        // Simplified settings
        panel.add(new JLabel("System Name:"));
        JTextField nameField = new JTextField("Fitness Tracker");
        panel.add(nameField);

        JButton saveButton = new JButton("Save Settings");
        saveButton.addActionListener(e -> {
            try {
                settingsDAO.updateSetting("system_name", nameField.getText());
                JOptionPane.showMessageDialog(this, "Settings Saved");
            } catch (DatabaseException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });
        panel.add(saveButton);
        return panel;
    }

    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] columns = { "Metric", "Value" };
        Object[][] data = {
                { "Total Users", "150" },
                { "Active Challenges", "5" },
                { "Workouts Logged Today", "45" }
        };
        JTable table = new JTable(data, columns);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createLogsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] columns = { "Timestamp", "Activity" };
        Object[][] data = {
                { "2023-10-27 10:00", "User Login: john@example.com" },
                { "2023-10-27 10:05", "New Workout Added: Running" }
        };
        JTable table = new JTable(data, columns);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }
}
