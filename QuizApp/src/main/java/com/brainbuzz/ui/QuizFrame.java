package com.brainbuzz.ui;

import javax.swing.*;
import java.awt.*;

/**
 * QuizFrame - Shows one question at a time, handles timer
 */
public class QuizFrame extends JFrame {
    public QuizFrame() {
        setTitle("Quiz");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        // Add quiz UI components here
    }
}
