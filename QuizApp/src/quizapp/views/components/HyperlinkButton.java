package quizapp.views.components;

import quizapp.utils.Theme;
import javax.swing.*;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class HyperlinkButton extends JButton {

    public HyperlinkButton(String text) {
        super(text);

        setFont(Theme.getFont(Theme.FONT_SMALL));
        setForeground(Theme.ACCENT_COLOR);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        setBorderPainted(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setOpaque(false);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setForeground(Theme.ACCENT_COLOR_HOVER);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setForeground(Theme.ACCENT_COLOR);
            }
        });
    }
}