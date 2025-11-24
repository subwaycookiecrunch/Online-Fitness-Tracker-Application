package ui.forms;

import dao.ChallengeDAO;
import model.Challenge;
import util.CustomExceptions.DatabaseException;

import javax.swing.*;
import java.awt.*;

public class AddChallengeForm extends JDialog {
    private ChallengeDAO challengeDAO;

    public AddChallengeForm(Frame owner) {
        super(owner, "Add Challenge", true);
        this.challengeDAO = new ChallengeDAO();

        setSize(400, 300);
        setLocationRelativeTo(owner);
        setLayout(new GridLayout(4, 2, 10, 10));

        add(new JLabel("Title:"));
        JTextField titleField = new JTextField();
        add(titleField);

        add(new JLabel("Description:"));
        JTextField descField = new JTextField();
        add(descField);

        add(new JLabel("Status:"));
        String[] statuses = { "ACTIVE", "UPCOMING", "COMPLETED" };
        JComboBox<String> statusBox = new JComboBox<>(statuses);
        add(statusBox);

        JButton saveButton = new JButton("Create Challenge");
        saveButton.addActionListener(e -> {
            try {
                Challenge challenge = new Challenge(0, titleField.getText(), descField.getText(),
                        (String) statusBox.getSelectedItem());
                challengeDAO.addChallenge(challenge);
                JOptionPane.showMessageDialog(this, "Challenge Created!");
                dispose();
            } catch (DatabaseException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });
        add(saveButton);
    }
}
