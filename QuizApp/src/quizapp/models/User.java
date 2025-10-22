package quizapp.models;

public class User {
    private final int id;
    private String username;
    private String password;
    private String email;
    private boolean isAdmin;

    public User(int id, String username, String password, String email, boolean isAdmin) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.isAdmin = isAdmin;
    }

    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public boolean isAdmin() { return isAdmin; }

    @Override
    public String toString() {
        String role = isAdmin ? " (Admin)" : "";
        return username + " - " + email + role;
    }
}