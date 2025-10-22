package quizapp.views;

import quizapp.MainApp;
import quizapp.models.User;
import quizapp.utils.Theme;
import quizapp.views.components.HyperlinkButton;
import quizapp.views.components.StyledButton;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

public class LoginPanel extends JPanel {

    private JTextField emailField;
    private JPasswordField passwordField;
    private MainApp mainApp;

    public LoginPanel(MainApp mainApp) {
        this.mainApp = mainApp;
        initComponents();
    }

    private JTextField createStyledTextField() {
        JTextField textField = new JTextField(25);
        textField.setFont(Theme.getFont(Theme.FONT_BODY));
        textField.setBackground(Theme.TEXT_FIELD_BACKGROUND);
        textField.setForeground(Theme.FOREGROUND_LIGHT);
        textField.setCaretColor(Theme.ACCENT_COLOR);
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 1, 1, 1, Theme.BORDER_COLOR),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        return textField;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField passField = new JPasswordField(25);
        passField.setFont(Theme.getFont(Theme.FONT_BODY));
        passField.setBackground(Theme.TEXT_FIELD_BACKGROUND);
        passField.setForeground(Theme.FOREGROUND_LIGHT);
        passField.setCaretColor(Theme.ACCENT_COLOR);
        passField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 1, 1, 1, Theme.BORDER_COLOR),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        return passField;
    }

    private void initComponents() {
        JPanel leftPanel = new JPanel();
        JPanel rightPanel = new JPanel();

        leftPanel.setBackground(Theme.BACKGROUND_DARK);
        leftPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 40, 10, 40);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        JLabel titleLabel = new JLabel("Welcome Back!");
        titleLabel.setFont(Theme.getFont(Theme.FONT_TITLE));
        titleLabel.setForeground(Theme.FOREGROUND_LIGHT);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel emailLabel = new JLabel("Email Address");
        emailLabel.setFont(Theme.getFont(Theme.FONT_BODY_BOLD));
        emailLabel.setForeground(Theme.FOREGROUND_LIGHT);
        emailField = createStyledTextField();

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(Theme.getFont(Theme.FONT_BODY_BOLD));
        passwordLabel.setForeground(Theme.FOREGROUND_LIGHT);
        passwordField = createStyledPasswordField();

        JButton loginButton = new StyledButton("LOGIN");
        JButton signupButton = new HyperlinkButton("Don't have an account? Sign up.");

        gbc.gridy = 0; gbc.gridwidth = 2; leftPanel.add(titleLabel, gbc);
        gbc.gridy++; leftPanel.add(Box.createVerticalStrut(20), gbc);
        gbc.gridy++; gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.WEST; leftPanel.add(emailLabel, gbc);
        gbc.gridy++; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER; leftPanel.add(emailField, gbc);
        gbc.gridy++; gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.WEST; leftPanel.add(passwordLabel, gbc);
        gbc.gridy++; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER; leftPanel.add(passwordField, gbc);
        gbc.gridy++; leftPanel.add(Box.createVerticalStrut(20), gbc);
        gbc.gridy++; gbc.gridwidth = 2; leftPanel.add(loginButton, gbc);
        gbc.gridy++; leftPanel.add(signupButton, gbc);

        // Right (Image) Panel
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setLayout(new GridBagLayout());
        JLabel imageLabel = new JLabel();
        try {
            imageLabel.setIcon(new ImageIcon(getClass().getResource("/icons/8899732.png")));
        } catch (Exception e) {
            imageLabel.setText("Image not found");
            System.err.println("Login image not found: /icons/8899732.png. Error: " + e.getMessage());
        }
        rightPanel.add(imageLabel);

        setLayout(new GridLayout(1, 2));
        add(leftPanel);
        add(rightPanel);

        loginButton.addActionListener(e -> attemptLogin());
        signupButton.addActionListener(e -> mainApp.showPanel("register"));
    }

    public void setEmail(String email) {
        emailField.setText(email);
        passwordField.requestFocusInWindow();
    }

    public void clearFields() {
        emailField.setText("");
        passwordField.setText("");
    }

    private void attemptLogin() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Email and password cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Optional<User> userOptional = mainApp.getUserService().authenticate(email, password);
        if (userOptional.isPresent()) {
            mainApp.loginUser(userOptional.get());
        } else {
            JOptionPane.showMessageDialog(this, "Invalid email or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}