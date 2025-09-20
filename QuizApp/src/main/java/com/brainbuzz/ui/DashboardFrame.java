package com.brainbuzz.ui;

import javax.swing.*;
import java.awt.*;

/**
 * DashboardFrame - User hub: start quiz, history, etc.
 */
public class DashboardFrame extends JFrame {
    public DashboardFrame() {
        setTitle("Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Dashboard", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 32));
        add(title, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 3, 20, 20));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JButton startQuizBtn = new JButton("Start Quiz");
        JButton historyBtn = new JButton("View History");
        JButton resultsBtn = new JButton("Results");
        JButton leaderboardBtn = new JButton("Leaderboard");
        JButton troubleshootBtn = new JButton("Troubleshoot");
        JButton adminPanelBtn = new JButton("Admin Panel");

        buttonPanel.add(startQuizBtn);
        buttonPanel.add(historyBtn);
        buttonPanel.add(resultsBtn);
        buttonPanel.add(leaderboardBtn);
        buttonPanel.add(troubleshootBtn);
        buttonPanel.add(adminPanelBtn);

        add(buttonPanel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DashboardFrame().setVisible(true));
    }
}
