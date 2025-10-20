package quizapp.frames;

import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {

    private JPanel sidebar, mainPanel;
    private CardLayout cardLayout;
    private String username; // store logged-in user

    // Main constructor (with username)
    public DashboardFrame(String username) {
        this.username = username;
        setTitle("BrainBuzz Dashboard - Welcome " + username);
        setSize(1000, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // ===== Sidebar =====
        sidebar = new JPanel();
        sidebar.setLayout(new GridLayout(7, 1, 10, 10)); // 7 buttons now
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.setBackground(new Color(45, 45, 45));

        JButton quizBtn = new JButton("Start Quiz");
        JButton historyBtn = new JButton("History");
        JButton leaderboardBtn = new JButton("Leaderboard");
        JButton troubleshootBtn = new JButton("Troubleshoot");
        JButton adminBtn = new JButton("Admin Panel");
        JButton profileBtn = new JButton("Profile");
        JButton logoutBtn = new JButton("Logout");

        sidebar.add(quizBtn);
        sidebar.add(historyBtn);
        sidebar.add(leaderboardBtn);
        sidebar.add(troubleshootBtn);
        sidebar.add(adminBtn);
        sidebar.add(profileBtn);
        sidebar.add(logoutBtn);

        // ===== Main Content with CardLayout =====
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // --- Subjects Panel ---
        JPanel quizPanel = new JPanel(new GridLayout(3, 2, 15, 15));
        quizPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        String[] subjects = {"Math", "Computer Science", "General Knowledge", "Science", "History", "English"};
        for (String subject : subjects) {
            JButton subjectBtn = new JButton(subject);
            subjectBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
            quizPanel.add(subjectBtn);

            subjectBtn.addActionListener(e -> {
                JOptionPane.showMessageDialog(this,
                        "You selected: " + subject + "\n(Here you can load QuizFrame for " + subject + ")");
            });
        }

        mainPanel.add(quizPanel, "quiz");
        mainPanel.add(new JLabel("📊 History Page", SwingConstants.CENTER), "history");
        mainPanel.add(new JLabel("🏆 Leaderboard Page", SwingConstants.CENTER), "leaderboard");
        mainPanel.add(new JLabel("🛠 Troubleshoot Page", SwingConstants.CENTER), "troubleshoot");
        mainPanel.add(new JLabel("⚙ Admin Panel", SwingConstants.CENTER), "admin");

        // ===== Event Listeners =====
        quizBtn.addActionListener(e -> cardLayout.show(mainPanel, "quiz"));
        historyBtn.addActionListener(e -> cardLayout.show(mainPanel, "history"));
        leaderboardBtn.addActionListener(e -> cardLayout.show(mainPanel, "leaderboard"));
        troubleshootBtn.addActionListener(e -> cardLayout.show(mainPanel, "troubleshoot"));
        adminBtn.addActionListener(e -> cardLayout.show(mainPanel, "admin"));

        profileBtn.addActionListener(e -> {
            dispose();
            new ProfileFrame(username).setVisible(true);
        });

        logoutBtn.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> {
                new Loginframe().setVisible(true);
            });
        });

        // ===== Layout Setup =====
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(sidebar, BorderLayout.WEST);
        getContentPane().add(mainPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    // Overloaded default constructor
    public DashboardFrame() {
        this("Guest");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DashboardFrame("Angelo").setVisible(true));
    }
}
