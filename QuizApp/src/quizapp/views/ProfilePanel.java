package quizapp.views;

import quizapp.MainApp;
import quizapp.models.User;
import quizapp.utils.Theme;
import quizapp.views.components.StyledButton;

import javax.swing.*;
import java.awt.*;

public class ProfilePanel extends JPanel {
    private MainApp mainApp;
    private JLabel usernameLabel;
    private JLabel emailLabel;
    private JLabel roleLabel;

    public ProfilePanel(MainApp mainApp) {
        this.mainApp = mainApp;
        initComponents();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel title = new JLabel("User Profile");
        title.setFont(Theme.getFont(Theme.FONT_TITLE));

        usernameLabel = new JLabel();
        emailLabel = new JLabel();
        roleLabel = new JLabel();

        Font labelFont = Theme.getFont(Theme.FONT_BODY);
        Font dataFont = Theme.getFont(Theme.FONT_BODY_BOLD);

        JLabel userLabelTitle = new JLabel("Username:");
        JLabel emailLabelTitle = new JLabel("Email:");
        JLabel roleLabelTitle = new JLabel("Role:");

        userLabelTitle.setFont(labelFont);
        emailLabelTitle.setFont(labelFont);
        roleLabelTitle.setFont(labelFont);

        usernameLabel.setFont(dataFont);
        emailLabel.setFont(dataFont);
        roleLabel.setFont(dataFont);

        JButton backBtn = new StyledButton("Back to Dashboard");

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        add(title, gbc);

        gbc.gridy++; gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.EAST;
        add(userLabelTitle, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        add(usernameLabel, gbc);

        gbc.gridy++; gbc.gridx = 0; gbc.anchor = GridBagConstraints.EAST;
        add(emailLabelTitle, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        add(emailLabel, gbc);

        gbc.gridy++; gbc.gridx = 0; gbc.anchor = GridBagConstraints.EAST;
        add(roleLabelTitle, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        add(roleLabel, gbc);

        gbc.gridy++; gbc.gridx = 0; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(30, 10, 10, 10);
        add(backBtn, gbc);

        backBtn.addActionListener(e -> mainApp.showPanel("dashboard"));
    }

    public void updateProfileInfo(User user) {
        if (user != null) {
            usernameLabel.setText(user.getUsername());
            emailLabel.setText(user.getEmail());
            roleLabel.setText(user.isAdmin() ? "Administrator" : "Standard User");
        }
    }
}