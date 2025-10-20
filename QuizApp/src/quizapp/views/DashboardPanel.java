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

        quizSelectionPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        quizSelectionPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        quizSelectionPanel.setOpaque(false);

        mainContentPanel.add(new JScrollPane(quizSelectionPanel), "quiz");

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

        if (cardName != null) {
            button.addActionListener(e -> mainContentLayout.show(mainContentPanel, cardName));
        }
        return button;
    }

    private void loadQuizCategories() {
        quizSelectionPanel.removeAll();
        List<Category> categories = mainApp.getQuizService().getCategories();

        for (Category category : categories) {
            StyledButton subjectBtn = new StyledButton(category.getName());
            subjectBtn.setFont(Theme.getFont(Theme.FONT_HEADER));

            String iconPath = getIconPathForCategory(category.getName());
            try {
                ImageIcon icon = new ImageIcon(new ImageIcon(getClass().getResource(iconPath))
                        .getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH));
                subjectBtn.setIcon(icon);
            } catch (Exception e) {
                System.err.println("Icon not found for: " + category.getName() + " at path " + iconPath);
            }
            subjectBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
            subjectBtn.setHorizontalTextPosition(SwingConstants.CENTER);

            subjectBtn.addActionListener(e -> mainApp.startQuiz(category));
            quizSelectionPanel.add(subjectBtn);
        }
        quizSelectionPanel.revalidate();
        quizSelectionPanel.repaint();
    }

    private String getIconPathForCategory(String categoryName) {
        switch (categoryName.toLowerCase()) {
            case "c programming":
                return "/icons/c.jpg";
            case "algorithmic thinking with python":
                return "/icons/atp.jpg";
            case "chemistry":
                return "/icons/chem.jpg";
            case "physics":
                return "/icons/phy.jpg";
            default:
                return "";
        }
    }

    public void updateForNewUser(User user) {
        welcomeLabel.setText("Welcome, " + user.getUsername() + "!");
        adminBtn.setVisible(user.isAdmin());
        loadQuizCategories();
        mainContentLayout.show(mainContentPanel, "quiz");
    }
}