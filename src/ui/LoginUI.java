package ui;

import dao.UserDAO;
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
    private UserDAO userDAO;

    public LoginUI() {
        userDAO = new UserDAO();
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

        JButton registerButton = new JButton("Register"); // Placeholder for now
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
                User user = userDAO.getUserByEmail(email);
                if (user.getPassword().equals(password)) {
                    JOptionPane.showMessageDialog(LoginUI.this, "Login Successful! Role: " + user.getRole());
                    dispose(); // Close Login Window
                    if ("ADMIN".equalsIgnoreCase(user.getRole())) {
                        new AdminDashboard(user);
                    } else {
                        new UserDashboard(user);
                    }
                } else {
                    JOptionPane.showMessageDialog(LoginUI.this, "Invalid Password");
                }
            } catch (UserNotFoundException ex) {
                JOptionPane.showMessageDialog(LoginUI.this, "User not found");
            } catch (DatabaseException ex) {
                JOptionPane.showMessageDialog(LoginUI.this, "Database Error: " + ex.getMessage());
            }
        }
    }
}
