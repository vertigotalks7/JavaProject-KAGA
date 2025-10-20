package quizapp.views;

import quizapp.MainApp;
import quizapp.utils.Theme;
import quizapp.views.components.HyperlinkButton;
import quizapp.views.components.StyledButton;

import javax.swing.*;
import java.awt.*;

public class RegistrationPanel extends JPanel {

    private MainApp mainApp;
    private JTextField nameField;
    private JTextField emailField;
    private JPasswordField passwordField;

    public RegistrationPanel(MainApp mainApp) {
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

        JLabel titleLabel = new JLabel("Create a New Account");
        titleLabel.setFont(Theme.getFont(Theme.FONT_TITLE));
        titleLabel.setForeground(Theme.FOREGROUND_LIGHT);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel nameLabel = new JLabel("Name");
        nameLabel.setFont(Theme.getFont(Theme.FONT_BODY_BOLD));
        nameLabel.setForeground(Theme.FOREGROUND_LIGHT);
        nameField = createStyledTextField();

        JLabel emailLabel = new JLabel("Email Address");
        emailLabel.setFont(Theme.getFont(Theme.FONT_BODY_BOLD));
        emailLabel.setForeground(Theme.FOREGROUND_LIGHT);
        emailField = createStyledTextField();

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(Theme.getFont(Theme.FONT_BODY_BOLD));
        passwordLabel.setForeground(Theme.FOREGROUND_LIGHT);
        passwordField = createStyledPasswordField();

        JButton signupButton = new StyledButton("SIGN UP");
        JButton loginButton = new HyperlinkButton("Already have an account? Log in.");

        gbc.gridy = 0; gbc.gridwidth = 2; leftPanel.add(titleLabel, gbc);
        gbc.gridy++; leftPanel.add(Box.createVerticalStrut(20), gbc);
        gbc.gridy++; gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.WEST; leftPanel.add(nameLabel, gbc);
        gbc.gridy++; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER; leftPanel.add(nameField, gbc);
        gbc.gridy++; gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.WEST; leftPanel.add(emailLabel, gbc);
        gbc.gridy++; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER; leftPanel.add(emailField, gbc);
        gbc.gridy++; gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.WEST; leftPanel.add(passwordLabel, gbc);
        gbc.gridy++; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER; leftPanel.add(passwordField, gbc);
        gbc.gridy++; leftPanel.add(Box.createVerticalStrut(20), gbc);
        gbc.gridy++; gbc.gridwidth = 2; leftPanel.add(signupButton, gbc);
        gbc.gridy++; leftPanel.add(loginButton, gbc);

        rightPanel.setBackground(Color.WHITE);
        rightPanel.setLayout(new GridBagLayout());
        JLabel imageLabel = new JLabel();
        try {
            imageLabel.setIcon(new ImageIcon(getClass().getResource("/icons/signup.jpg")));
        } catch (Exception e) {
            imageLabel.setText("Image not found");
        }
        rightPanel.add(imageLabel);

        setLayout(new GridLayout(1, 2));
        add(leftPanel);
        add(rightPanel);

        signupButton.addActionListener(e -> attemptRegistration());
        loginButton.addActionListener(e -> mainApp.showPanel("login"));
    }

    private void attemptRegistration() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String emailRegex = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$";

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!email.matches(emailRegex)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address.", "Invalid Email", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (mainApp.getUserService().isEmailTaken(email)) {
            JOptionPane.showMessageDialog(this, "This email is already registered.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        mainApp.getUserService().registerUser(name, email, password);
        JOptionPane.showMessageDialog(this, "Registration successful! Please log in.", "Success", JOptionPane.INFORMATION_MESSAGE);
        mainApp.getLoginPanel().setEmail(email);
        mainApp.showPanel("login");
    }
}