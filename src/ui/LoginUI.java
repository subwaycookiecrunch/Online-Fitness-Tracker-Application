package ui;

import model.User;
import util.CustomExceptions.DatabaseException;
import util.CustomExceptions.UserNotFoundException;
import util.Validator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginUI extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private service.UserService userService;

    public LoginUI() {
        userService = new service.UserService();
        setTitle("Fitness Tracker - Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 2, 10, 10));

        add(new JLabel("Email:"));
        emailField = new JTextField();
        add(emailField);

        add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new LoginAction());
        add(loginButton);

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> {
            new RegisterUI();
            dispose();
        });
        add(registerButton);

        setVisible(true);
    }

    private class LoginAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());

            if (!Validator.validateEmail(email)) {
                JOptionPane.showMessageDialog(LoginUI.this, "Invalid Email Format");
                return;
            }

            try {
                User user = userService.login(email, password);
                JOptionPane.showMessageDialog(LoginUI.this, "Login Successful! Role: " + user.getRole());
                dispose(); // Close Login Window
                if ("ADMIN".equalsIgnoreCase(user.getRole())) {
                    new AdminDashboard(user);
                } else {
                    new UserDashboard(user);
                }
            } catch (UserNotFoundException ex) {
                JOptionPane.showMessageDialog(LoginUI.this, "Invalid Email or Password");
            } catch (DatabaseException ex) {
                JOptionPane.showMessageDialog(LoginUI.this, "Database Error: " + ex.getMessage());
            }
        }
    }
}
