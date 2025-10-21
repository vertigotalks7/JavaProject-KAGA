package quizapp.views;

import quizapp.MainApp;
import quizapp.models.User;
import quizapp.utils.Theme;
import quizapp.views.components.StyledButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class UserManagementDialog extends JDialog {

    private final MainApp mainApp;
    private JList<User> userList;
    private DefaultListModel<User> userListModel;

    public UserManagementDialog(Frame parent) {
        super(parent, "User Management", true); // Ensure parent is Frame
        this.mainApp = (MainApp) parent;
        initComponents();
        setSize(500, 400);
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        contentPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("All Registered Users");
        titleLabel.setFont(Theme.getFont(Theme.FONT_HEADER));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentPanel.add(titleLabel, BorderLayout.NORTH);

        userListModel = new DefaultListModel<>();
        userList = new JList<>(userListModel);
        userList.setFont(Theme.getFont(Theme.FONT_BODY));
        userList.setCellRenderer(new UserListCellRenderer());
        refreshUserList(); // Load users when dialog opens
        contentPanel.add(new JScrollPane(userList), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new GridLayout(1, 2, 10, 0)); // Use GridLayout for even spacing
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(new EmptyBorder(10, 0, 0, 0)); // Add top margin

        JButton resetScoreBtn = new StyledButton("Reset History for User");
        resetScoreBtn.addActionListener(e -> resetUserScores());

        JButton closeBtn = new StyledButton("Close");
        closeBtn.setBackground(Color.GRAY); // Optional: different color for close
        closeBtn.addActionListener(e -> dispose()); // Simply close the dialog

        bottomPanel.add(resetScoreBtn);
        bottomPanel.add(closeBtn);
        contentPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(contentPanel);
    }

    private void refreshUserList() {
        userListModel.clear();
        List<User> users = mainApp.getUserService().getAllUsers();
        for (User user : users) {
            userListModel.addElement(user);
        }
    }

    private void resetUserScores() {
        User selectedUser = userList.getSelectedValue();
        if (selectedUser == null) {
            JOptionPane.showMessageDialog(this, "Please select a user to reset.", "No User Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete ALL quiz history for '" + selectedUser.getUsername() + "'?\nThis action cannot be undone.",
                "Confirm Score Reset",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            mainApp.getQuizService().deleteResultsForUser(selectedUser.getId());
            JOptionPane.showMessageDialog(this, "Score history for " + selectedUser.getUsername() + " has been reset.", "Success", JOptionPane.INFORMATION_MESSAGE);
            // Optionally, refresh leaderboard or history if they are currently visible in the main app
        }
    }

    // Custom renderer to display user info cleanly in the JList
    private static class UserListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            // Use default renderer for selection colors etc.
            Component comp = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof User) {
                User user = (User) value;
                String role = user.isAdmin() ? " (Admin)" : "";
                // Use User's toString method for consistent display
                setText(user.toString());
            }
            return comp;
        }
    }
}