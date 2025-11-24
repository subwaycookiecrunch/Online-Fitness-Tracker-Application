package ui.forms;

import dao.UserDAO;
import model.User;
import util.CustomExceptions.DatabaseException;

import javax.swing.*;
import java.awt.*;

public class EditProfileForm extends JDialog {
    private User user;
    private UserDAO userDAO;

    public EditProfileForm(Frame owner, User user) {
        super(owner, "Edit Profile", true);
        this.user = user;
        this.userDAO = new UserDAO();

        setSize(400, 300);
        setLocationRelativeTo(owner);
        setLayout(new GridLayout(4, 2, 10, 10));

        add(new JLabel("Name:"));
        JTextField nameField = new JTextField(user.getName());
        add(nameField);

        add(new JLabel("Email:"));
        JTextField emailField = new JTextField(user.getEmail());
        add(emailField);

        add(new JLabel("Password:"));
        JPasswordField passwordField = new JPasswordField(user.getPassword());
        add(passwordField);

        JButton updateButton = new JButton("Update Profile");
        updateButton.addActionListener(e -> {
            try {
                user.setName(nameField.getText());
                user.setEmail(emailField.getText());
                user.setPassword(new String(passwordField.getPassword()));

                userDAO.updateUser(user);
                JOptionPane.showMessageDialog(this, "Profile Updated!");
                dispose();
            } catch (DatabaseException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });
        add(updateButton);
    }
}
