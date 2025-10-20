package quizapp.views.components;

import quizapp.utils.Theme;
import javax.swing.*;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class StyledButton extends JButton {

    public StyledButton(String text) {
        super(text);

        setFont(Theme.getFont(Theme.FONT_BUTTON));
        setBackground(Theme.ACCENT_COLOR);
        setForeground(Color.WHITE);
        setFocusPainted(false);
        setBorderPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setOpaque(true);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(Theme.ACCENT_COLOR_HOVER);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(Theme.ACCENT_COLOR);
            }
        });
    }
}