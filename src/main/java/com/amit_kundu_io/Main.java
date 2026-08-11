package com.amit_kundu_io;

import com.amit_kundu_io.prasentation.StudentManagementUI;

import javax.swing.*;

/**
 * Application entry point.
 */
public class Main {

    public static void main(String[] args) {

        System.out.println("I LoVe MeG");

        try {
            // Use the operating system's native look and feel
            // (Windows, macOS, Linux) instead of the default Swing theme.
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        } catch (Exception ignored) {
            // If the system theme can't be applied,
            // Swing uses its default look and feel.
        }

        // Start the Swing application on the Event Dispatch Thread (EDT),
        // which is the thread responsible for creating and updating the UI.
        SwingUtilities.invokeLater(StudentManagementUI::new);
    }
}