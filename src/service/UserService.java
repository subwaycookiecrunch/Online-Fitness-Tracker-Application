package service;

import dao.UserDAO;
import model.User;
import util.CustomExceptions.DatabaseException;
import util.CustomExceptions.UserNotFoundException;

public class UserService {
    private UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }

    public User login(String email, String password) throws UserNotFoundException, DatabaseException {
        User user = userDAO.getUserByEmail(email);
        if (user.getPassword().equals(password)) {
            return user;
        } else {
            throw new UserNotFoundException("Invalid credentials");
        }
    }

    public void registerUser(User user) throws DatabaseException {
        // Business logic: Check if user already exists (optional, but good practice)
        // For now, just delegate to DAO
        userDAO.addUser(user);
    }
}
