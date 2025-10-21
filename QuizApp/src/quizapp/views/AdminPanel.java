package quizapp.views;

import quizapp.MainApp;
import quizapp.utils.Theme;
import quizapp.views.components.StyledButton;

import javax.swing.*;
import java.awt.*;

public class AdminPanel extends JPanel {
    private final MainApp mainApp;

    public AdminPanel(MainApp mainApp) {
        this.mainApp = mainApp;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(20, 20));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JLabel titleLabel = new JLabel("Admin Control Panel");
        titleLabel.setFont(Theme.getFont(Theme.FONT_TITLE));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        // Panel for the main action buttons
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL; // Make buttons same width
        gbc.weightx = 1.0; // Allow buttons to expand horizontally

        StyledButton addQuestionBtn = new StyledButton("Add New Question");
        addQuestionBtn.setFont(Theme.getFont(Theme.FONT_HEADER));
        addQuestionBtn.setPreferredSize(new Dimension(300, 60)); // Set preferred size
        addQuestionBtn.addActionListener(e -> new AddQuestionDialog(mainApp).setVisible(true));

        StyledButton deleteContentBtn = new StyledButton("Delete Questions or Categories");
        deleteContentBtn.setFont(Theme.getFont(Theme.FONT_HEADER));
        deleteContentBtn.setPreferredSize(new Dimension(300, 60));
        deleteContentBtn.addActionListener(e -> new DeleteContentDialog(mainApp).setVisible(true));

        StyledButton manageUsersBtn = new StyledButton("Manage Users");
        manageUsersBtn.setFont(Theme.getFont(Theme.FONT_HEADER));
        manageUsersBtn.setPreferredSize(new Dimension(300, 60));
        manageUsersBtn.addActionListener(e -> mainApp.openUserManagementDialog());

        gbc.gridy = 0;
        buttonPanel.add(addQuestionBtn, gbc);
        gbc.gridy = 1;
        buttonPanel.add(deleteContentBtn, gbc);
        gbc.gridy = 2;
        buttonPanel.add(manageUsersBtn, gbc);

        // Add buttonPanel inside another panel to control its vertical position
        JPanel centerWrapper = new JPanel(new GridBagLayout()); // Use GridBagLayout to center vertically
        centerWrapper.setOpaque(false);
        centerWrapper.add(buttonPanel); // Add the panel containing buttons
        add(centerWrapper, BorderLayout.CENTER);

        // Back to Dashboard Button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setOpaque(false);
        JButton backButton = new StyledButton("Back to Dashboard");
        backButton.addActionListener(e -> mainApp.showPanel("dashboard"));
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // No longer needed here as dialogs handle their own data
    public void refreshData() {}
}