package ui.forms;

import model.Workout;
import util.CustomExceptions.DatabaseException;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;

public class EditWorkoutForm extends JDialog {
    private Workout workout;
    private service.WorkoutService workoutService;

    public EditWorkoutForm(Frame owner, Workout workout) {
        super(owner, "Edit Workout", true);
        this.workout = workout;
        this.workoutService = new service.WorkoutService();

        setSize(400, 300);
        setLocationRelativeTo(owner);
        setLayout(new GridLayout(5, 2, 10, 10));

        add(new JLabel("Type:"));
        JTextField typeField = new JTextField(workout.getType());
        add(typeField);

        add(new JLabel("Duration (mins):"));
        JTextField durationField = new JTextField(String.valueOf(workout.getDuration()));
        add(durationField);

        add(new JLabel("Intensity:"));
        String[] intensities = { "Low", "Medium", "High" };
        JComboBox<String> intensityBox = new JComboBox<>(intensities);
        intensityBox.setSelectedItem(workout.getIntensity());
        add(intensityBox);

        add(new JLabel("Date (YYYY-MM-DD):"));
        JTextField dateField = new JTextField(workout.getDate().toString());
        add(dateField);

        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(e -> {
            try {
                workout.setType(typeField.getText());
                workout.setDuration(Integer.parseInt(durationField.getText()));
                workout.setIntensity((String) intensityBox.getSelectedItem());
                workout.setDate(Date.valueOf(dateField.getText()));

                workoutService.updateWorkout(workout);
                JOptionPane.showMessageDialog(this, "Workout Updated!");
                dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid Duration Format");
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, "Invalid Date Format");
            } catch (DatabaseException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });
        add(updateButton);
    }
}
