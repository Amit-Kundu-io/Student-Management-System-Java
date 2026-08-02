package com.amit_kundu_io.prasentation;


import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Small factory methods for building consistently styled Swing components.
 */
public final class UIComponents {

    private UIComponents() { }

    /** A white rounded-border "card" panel used to group related content. */
    public static JPanel card() {
        JPanel card = new JPanel();
        card.setBackground(Theme.CARD_BG);
        card.setBorder(new CompoundBorder(
                new LineBorder(Theme.BORDER_CLR, 1, true),
                new EmptyBorder(16, 18, 16, 18)
        ));
        return card;
    }

    /** A text field with padding and a focus-highlight border. */
    public static JTextField styledField() {
        JTextField field = new JTextField();
        field.setFont(Theme.FONT_FIELD);
        field.setBorder(new CompoundBorder(
                new LineBorder(Theme.BORDER_CLR, 1, true),
                new EmptyBorder(6, 10, 6, 10)
        ));
        field.setPreferredSize(new Dimension(field.getPreferredSize().width, 32));

        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                field.setBorder(new CompoundBorder(
                        new LineBorder(Theme.PRIMARY, 1, true),
                        new EmptyBorder(6, 10, 6, 10)));
            }

            @Override
            public void focusLost(FocusEvent e) {
                field.setBorder(new CompoundBorder(
                        new LineBorder(Theme.BORDER_CLR, 1, true),
                        new EmptyBorder(6, 10, 6, 10)));
            }
        });

        return field;
    }

    /**
     * A pill-shaped button with a pastel background and BLACK bold text,
     * which stays readable at all times (unlike white text on solid color).
     */
    public static JButton pillButton(String text, Color base, Color hover) {
        JButton btn = new JButton(text);
        btn.setFont(Theme.FONT_BUTTON);
        btn.setForeground(Theme.BTN_TEXT);
        btn.setBackground(base);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(9, 18, 9, 18));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(hover);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(base);
            }
        });

        return btn;
    }

    public static JLabel fieldLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(Theme.FONT_LABEL);
        l.setForeground(Theme.TEXT_MAIN);
        return l;
    }
}