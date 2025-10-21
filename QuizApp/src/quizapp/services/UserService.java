package quizapp.services;

import quizapp.dao.UserDAO;
import quizapp.models.User;
import java.util.List;
import java.util.Optional;

public class UserService {

    private final UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }

    // Consider adding password hashing/verification here instead of plain text compare
    public Optional<User> authenticate(String email, String password) {
        Optional<User> userOptional = userDAO.findUserByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // !! SECURITY WARNING: Plain text password comparison !!
            // In a real app, use a secure hashing library (e.g., BCrypt)
            if (user.getPassword().equals(password)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    public boolean isEmailTaken(String email) {
        return userDAO.findUserByEmail(email).isPresent();
    }

    // Consider adding password hashing here before saving
    public void registerUser(String username, String email, String password) {
        if (!isEmailTaken(email)) {
            // !! SECURITY WARNING: Saving plain text password !!
            userDAO.saveUser(username, email, password, false);
        }
    }

    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }
}