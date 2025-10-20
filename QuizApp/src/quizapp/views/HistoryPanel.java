package quizapp.views;

import quizapp.MainApp;
import quizapp.models.QuizResult;
import quizapp.utils.Theme;
import quizapp.views.components.StyledButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class HistoryPanel extends JPanel {

    private final MainApp mainApp;
    private final DefaultTableModel tableModel;
    private final JTable historyTable;

    public HistoryPanel(MainApp mainApp) {
        this.mainApp = mainApp;
        this.tableModel = new DefaultTableModel(new String[]{"Category", "Score", "Date Taken"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        historyTable = new JTable(tableModel);
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(20, 20));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 40, 40, 40));

        JLabel titleLabel = new JLabel("Your Quiz History");
        titleLabel.setFont(Theme.getFont(Theme.FONT_TITLE));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        historyTable.setFont(Theme.getFont(Theme.FONT_BODY));
        historyTable.setRowHeight(30);
        historyTable.setFillsViewportHeight(true);
        historyTable.setGridColor(Color.LIGHT_GRAY);

        JTableHeader header = historyTable.getTableHeader();
        header.setFont(Theme.getFont(Theme.FONT_BODY_BOLD));
        header.setBackground(Theme.TABLE_HEADER_BG); // Use new theme color
        header.setForeground(Theme.SIDEBAR_BUTTON_TEXT);

        JScrollPane scrollPane = new JScrollPane(historyTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setOpaque(false);
        JButton backButton = new StyledButton("Back to Dashboard");
        backButton.addActionListener(e -> mainApp.showPanel("dashboard"));
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void populateHistory() {
        tableModel.setRowCount(0);
        List<QuizResult> results = mainApp.getQuizService().getHistoryForUser(mainApp.getCurrentUser());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if (results.isEmpty()) {
            tableModel.addRow(new Object[]{"No history found.", "", ""});
        } else {
            for (QuizResult result : results) {
                String score = result.getScore() + " / " + result.getTotalQuestions();
                String date = dateFormat.format(result.getDateTaken());
                tableModel.addRow(new Object[]{result.getCategoryName(), score, date});
            }
        }
    }
}
