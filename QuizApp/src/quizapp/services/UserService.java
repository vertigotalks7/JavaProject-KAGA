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

    public Optional<User> authenticate(String email, String password) {
        Optional<User> userOptional = userDAO.findUserByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            if (user.getPassword().equals(password)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    public boolean isEmailTaken(String email) {
        return userDAO.findUserByEmail(email).isPresent();
    }

    public void registerUser(String username, String email, String password) {
        if (!isEmailTaken(email)) {
            userDAO.saveUser(username, email, password, false);
        }
    }

    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }
}