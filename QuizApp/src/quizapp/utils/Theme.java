package quizapp.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;

public class Theme {

    // --- COLOR PALETTE (Refined) ---
    public static final Color BACKGROUND_DARK = new Color(45, 45, 45);
    public static final Color FOREGROUND_LIGHT = new Color(240, 240, 240);

    public static final Color SIDEBAR_BUTTON_BG = new Color(240, 240, 240);
    public static final Color SIDEBAR_BUTTON_HOVER_BG = new Color(220, 220, 220);
    public static final Color SIDEBAR_BUTTON_TEXT = Color.BLACK;

    public static final Color TABLE_HEADER_BG = new Color(230, 230, 230); // New color for table headers

    public static final Color ACCENT_COLOR = new Color(0, 150, 255);
    public static final Color ACCENT_COLOR_HOVER = new Color(0, 120, 220);

    public static final Color TEXT_FIELD_BACKGROUND = new Color(70, 70, 70);
    public static final Color BORDER_COLOR = new Color(80, 80, 80);

    // --- FONTS ---
    private static final String FONT_NAME = "Segoe UI";
    public static final Font FONT_TITLE = new Font(FONT_NAME, Font.BOLD, 28);
    public static final Font FONT_HEADER = new Font(FONT_NAME, Font.BOLD, 22);
    public static final Font FONT_BODY = new Font(FONT_NAME, Font.PLAIN, 16);
    public static final Font FONT_BODY_BOLD = new Font(FONT_NAME, Font.BOLD, 16);
    public static final Font FONT_SMALL = new Font(FONT_NAME, Font.PLAIN, 14);
    public static final Font FONT_BUTTON = new Font(FONT_NAME, Font.BOLD, 16);

    public static Font getFont(Font baseFont) {
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            for (String fontName : ge.getAvailableFontFamilyNames()) {
                if (fontName.equals(FONT_NAME)) {
                    return baseFont;
                }
            }
            return new Font(Font.SANS_SERIF, baseFont.getStyle(), baseFont.getSize());
        } catch (Exception e) {
            return new Font(Font.SANS_SERIF, baseFont.getStyle(), baseFont.getSize());
        }
    }
}
