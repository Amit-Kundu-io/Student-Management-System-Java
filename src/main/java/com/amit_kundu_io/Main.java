package com.amit_kundu_io;

import com.amit_kundu_io.prasentation.StudentManagementUI;

import javax.swing.*;

/**
 * Application entry point.
 */
public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // fall back to default look and feel
        }

        SwingUtilities.invokeLater(StudentManagementUI::new);
    }
}
 