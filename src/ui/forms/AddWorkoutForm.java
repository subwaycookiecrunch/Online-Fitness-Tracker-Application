package ui.forms;

import model.User;
import model.Workout;
import util.CustomExceptions.DatabaseException;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;

public class AddWorkoutForm extends JDialog {
    private User user;
    private service.WorkoutService workoutService;

    public AddWorkoutForm(Frame owner, User user) {
        super(owner, "Add Workout", true);
        this.user = user;
        this.workoutService = new service.WorkoutService();

        setSize(400, 300);
        setLocationRelativeTo(owner);
        setLayout(new GridLayout(5, 2, 10, 10));

        add(new JLabel("Type:"));
        JTextField typeField = new JTextField();
        add(typeField);

        add(new JLabel("Duration (mins):"));
        JTextField durationField = new JTextField();
        add(durationField);

        add(new JLabel("Intensity:"));
        String[] intensities = { "Low", "Medium", "High" };
        JComboBox<String> intensityBox = new JComboBox<>(intensities);
        add(intensityBox);

        add(new JLabel("Date (YYYY-MM-DD):"));
        JTextField dateField = new JTextField(new Date(System.currentTimeMillis()).toString());
        add(dateField);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            try {
                String type = typeField.getText();
                int duration = Integer.parseInt(durationField.getText());
                String intensity = (String) intensityBox.getSelectedItem();
                Date date = Date.valueOf(dateField.getText());

                Workout workout = new Workout(0, user.getId(), type, duration, intensity, date);
                workoutService.logWorkout(workout);
                JOptionPane.showMessageDialog(this, "Workout Added!");
                dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid Duration Format");
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, "Invalid Date Format");
            } catch (DatabaseException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });
        add(saveButton);
    }
}
