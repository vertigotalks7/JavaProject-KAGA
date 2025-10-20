package quizapp.frames;

import javax.swing.*;
import java.awt.*;

public class ProfileFrame extends JFrame {
    private String username;

    public ProfileFrame(String username) {
        this.username = username;

        setTitle("Profile - " + username);
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JButton backBtn = new JButton("Back to Dashboard");

        setLayout(new FlowLayout());
        add(new JLabel("Profile Page for: " + username));
        add(backBtn);

        backBtn.addActionListener(e -> {
            dispose(); 
            new DashboardFrame(username).setVisible(true); // ✅ return to same user’s dashboard
        });
    }
}
