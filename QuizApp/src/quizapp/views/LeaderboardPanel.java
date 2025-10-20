package quizapp.views;

import quizapp.MainApp;
import quizapp.models.LeaderboardEntry;
import quizapp.utils.Theme;
import quizapp.views.components.StyledButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class LeaderboardPanel extends JPanel {

    private final MainApp mainApp;
    private final DefaultTableModel tableModel;
    private final JTable leaderboardTable;

    public LeaderboardPanel(MainApp mainApp) {
        this.mainApp = mainApp;
        this.tableModel = new DefaultTableModel(new String[]{"Rank", "Username", "Category", "High Score"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        leaderboardTable = new JTable(tableModel);
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(20, 20));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 40, 40, 40));

        JLabel titleLabel = new JLabel("Leaderboard");
        titleLabel.setFont(Theme.getFont(Theme.FONT_TITLE));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        leaderboardTable.setFont(Theme.getFont(Theme.FONT_BODY));
        leaderboardTable.setRowHeight(30);
        leaderboardTable.setGridColor(Color.LIGHT_GRAY);
        leaderboardTable.getColumnModel().getColumn(0).setMaxWidth(80);
        leaderboardTable.setFillsViewportHeight(true);

        JTableHeader header = leaderboardTable.getTableHeader();
        header.setFont(Theme.getFont(Theme.FONT_BODY_BOLD));
        header.setBackground(Theme.TABLE_HEADER_BG); // Use new theme color
        header.setForeground(Theme.SIDEBAR_BUTTON_TEXT);

        JScrollPane scrollPane = new JScrollPane(leaderboardTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setOpaque(false);
        JButton backButton = new StyledButton("Back to Dashboard");
        backButton.addActionListener(e -> mainApp.showPanel("dashboard"));
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void populateLeaderboard() {
        tableModel.setRowCount(0);
        List<LeaderboardEntry> topScores = mainApp.getQuizService().getLeaderboard();

        if (topScores.isEmpty()) {
            tableModel.addRow(new Object[]{"-", "No scores recorded yet.", "", ""});
        } else {
            int rank = 1;
            for (LeaderboardEntry entry : topScores) {
                tableModel.addRow(new Object[]{rank++, entry.getUsername(), entry.getCategoryName(), entry.getHighScore()});
            }
        }
    }
}
