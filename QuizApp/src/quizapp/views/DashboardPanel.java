package quizapp.views;

import quizapp.MainApp;
import quizapp.models.Category;
import quizapp.models.User;
import quizapp.utils.Theme;
import quizapp.views.components.StyledButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class DashboardPanel extends JPanel {
    private MainApp mainApp;
    private CardLayout mainContentLayout;
    private JPanel mainContentPanel;
    private JLabel welcomeLabel;
    private JPanel quizSelectionPanel;
    private JButton adminBtn;

    public DashboardPanel(MainApp mainApp) {
        this.mainApp = mainApp;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // ===== Sidebar =====
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBackground(Theme.BACKGROUND_DARK);
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JLabel appTitle = new JLabel("BrainBuzz");
        appTitle.setFont(Theme.getFont(Theme.FONT_TITLE));
        appTitle.setForeground(Theme.FOREGROUND_LIGHT);
        appTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        sidebar.add(appTitle);
        sidebar.add(Box.createRigidArea(new Dimension(0, 30)));

        sidebar.add(createSidebarButton("Start Quiz", "quiz"));
        sidebar.add(Box.createRigidArea(new Dimension(0, 15)));

        JButton historyBtn = createSidebarButton("History", null);
        historyBtn.addActionListener(e -> mainApp.showPanel("history"));
        sidebar.add(historyBtn);
        sidebar.add(Box.createRigidArea(new Dimension(0, 15)));

        JButton leaderboardBtn = createSidebarButton("Leaderboard", null);
        leaderboardBtn.addActionListener(e -> mainApp.showPanel("leaderboard"));
        sidebar.add(leaderboardBtn);
        sidebar.add(Box.createRigidArea(new Dimension(0, 15)));

        adminBtn = createSidebarButton("Admin Panel", null);
        adminBtn.addActionListener(e -> mainApp.showPanel("admin"));
        adminBtn.setVisible(false);
        sidebar.add(adminBtn);

        sidebar.add(Box.createVerticalGlue());

        JButton profileBtn = createSidebarButton("Profile", null);
        profileBtn.addActionListener(e -> mainApp.showPanel("profile"));
        sidebar.add(profileBtn);
        sidebar.add(Box.createRigidArea(new Dimension(0, 15)));

        JButton logoutBtn = createSidebarButton("Logout", null);
        logoutBtn.addActionListener(e -> mainApp.logoutUser());
        sidebar.add(logoutBtn);

        // ===== Main Content Area =====
        JPanel contentArea = new JPanel(new BorderLayout(10, 10));
        contentArea.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentArea.setBackground(Color.WHITE);

        welcomeLabel = new JLabel("Welcome!", SwingConstants.LEFT);
        welcomeLabel.setFont(Theme.getFont(Theme.FONT_TITLE));
        contentArea.add(welcomeLabel, BorderLayout.NORTH);

        mainContentLayout = new CardLayout();
        mainContentPanel = new JPanel(mainContentLayout);
        mainContentPanel.setOpaque(false);

        // Use a scroll pane in case there are many categories
        quizSelectionPanel = new JPanel(new GridLayout(2, 2, 20, 20)); // Keep 2x2 grid
        quizSelectionPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        quizSelectionPanel.setOpaque(false);
        JScrollPane quizScrollPane = new JScrollPane(quizSelectionPanel);
        quizScrollPane.setOpaque(false);
        quizScrollPane.getViewport().setOpaque(false);
        quizScrollPane.setBorder(null);

        mainContentPanel.add(quizScrollPane, "quiz"); // Add scroll pane instead of panel directly

        contentArea.add(mainContentPanel, BorderLayout.CENTER);
        mainContentLayout.show(mainContentPanel, "quiz");

        add(sidebar, BorderLayout.WEST);
        add(contentArea, BorderLayout.CENTER);
    }

    private JButton createSidebarButton(String text, String cardName) {
        JButton button = new JButton(text);
        button.setFont(Theme.getFont(Theme.FONT_BODY_BOLD));
        button.setForeground(Theme.SIDEBAR_BUTTON_TEXT);
        button.setBackground(Theme.SIDEBAR_BUTTON_BG);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, button.getPreferredSize().height));
        button.setOpaque(true);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(Theme.SIDEBAR_BUTTON_HOVER_BG);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(Theme.SIDEBAR_BUTTON_BG);
            }
        });

        // Navigate within the dashboard's CardLayout if cardName is provided
        if (cardName != null && mainContentPanel != null) {
            button.addActionListener(e -> mainContentLayout.show(mainContentPanel, cardName));
        }
        return button;
    }

    private void loadQuizCategories() {
        quizSelectionPanel.removeAll();
        List<Category> categories = mainApp.getQuizService().getCategories();

        // Dynamically adjust grid layout based on number of categories
        int numCategories = categories.size();
        int rows = (int) Math.ceil(numCategories / 2.0); // Keep 2 columns, adjust rows
        quizSelectionPanel.setLayout(new GridLayout(rows, 2, 20, 20));

        for (Category category : categories) {
            StyledButton subjectBtn = new StyledButton(category.getName());
            subjectBtn.setFont(Theme.getFont(Theme.FONT_HEADER));

            String iconPath = getIconPathForCategory(category.getName());
            try {
                // Ensure icon path starts with '/' for getResource
                if (!iconPath.isEmpty() && !iconPath.startsWith("/")) {
                    iconPath = "/" + iconPath;
                }
                if (!iconPath.isEmpty()) {
                    ImageIcon icon = new ImageIcon(new ImageIcon(getClass().getResource(iconPath))
                            .getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH));
                    subjectBtn.setIcon(icon);
                }
            } catch (Exception e) {
                System.err.println("Icon not found for: " + category.getName() + " at path " + iconPath + ". Error: " + e.getMessage());
                // Optionally set default icon or text
                subjectBtn.setIcon(null);
            }
            subjectBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
            subjectBtn.setHorizontalTextPosition(SwingConstants.CENTER);
            subjectBtn.setPreferredSize(new Dimension(200, 150)); // Give buttons a decent size

            subjectBtn.addActionListener(e -> mainApp.startQuiz(category));
            quizSelectionPanel.add(subjectBtn);
        }
        quizSelectionPanel.revalidate();
        quizSelectionPanel.repaint();
    }

    private String getIconPathForCategory(String categoryName) {
        // Ensure consistent path format
        switch (categoryName.toLowerCase()) {
            case "c programming": return "/icons/c.png";
            case "algorithmic thinking with python": return "/icons/atp.png";
            case "chemistry": return "/icons/chem.png";
            case "physics": return "/icons/phy.png";
            default: return "";
        }
    }

    public void updateForNewUser(User user) {
        welcomeLabel.setText("Welcome, " + user.getUsername() + "!");
        adminBtn.setVisible(user.isAdmin()); // Show/hide based on actual user role
        loadQuizCategories();
        if (mainContentLayout != null) { // Ensure layout manager is initialized
            mainContentLayout.show(mainContentPanel, "quiz");
        }
    }
}