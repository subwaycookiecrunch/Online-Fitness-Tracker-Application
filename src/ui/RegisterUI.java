package ui;

import model.NormalUser;
import util.CustomExceptions.DatabaseException;
import util.Validator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterUI extends JFrame {
    private JTextField nameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private service.UserService userService;

    public RegisterUI() {
        userService = new service.UserService();
        setTitle("Fitness Tracker - Register");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(6, 2, 10, 10));

        add(new JLabel("Name:"));
        nameField = new JTextField();
        add(nameField);

        add(new JLabel("Email:"));
        emailField = new JTextField();
        add(emailField);

        add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        add(passwordField);

        add(new JLabel("Confirm Password:"));
        confirmPasswordField = new JPasswordField();
        add(confirmPasswordField);

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(new RegisterAction());
        add(registerButton);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            new LoginUI();
            dispose();
        });
        add(backButton);

        setVisible(true);
    }

    private class RegisterAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = nameField.getText();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(RegisterUI.this, "All fields are required");
                return;
            }

            if (!Validator.validateEmail(email)) {
                JOptionPane.showMessageDialog(RegisterUI.this, "Invalid Email Format");
                return;
            }

            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(RegisterUI.this, "Passwords do not match");
                return;
            }

            try {
                // ID is auto-incremented in DB, so passing 0
                NormalUser newUser = new NormalUser(0, name, email, password);
                userService.registerUser(newUser);
                JOptionPane.showMessageDialog(RegisterUI.this, "Registration Successful! Please Login.");
                new LoginUI();
                dispose();
            } catch (DatabaseException ex) {
                JOptionPane.showMessageDialog(RegisterUI.this, "Registration Failed: " + ex.getMessage());
            }
        }
    }
}
