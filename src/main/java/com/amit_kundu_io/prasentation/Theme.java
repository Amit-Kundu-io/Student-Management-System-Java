package com.amit_kundu_io.prasentation;


import java.awt.Color;
import java.awt.Font;

/**
 * Central place for all colors and fonts used across the app.
 * Change a value here and it updates everywhere.
 */
public final class Theme {

    private Theme() {
    }

    // ---- Brand / accents (used for borders, headings, focus rings) ----
    public static final Color PRIMARY = new Color(37, 99, 235);   // blue-600
    public static final Color PRIMARY_DARK = new Color(29, 78, 216);   // blue-700

    // ---- Button colors: soft pastel background + BLACK text for readability ----
    public static final Color BTN_INSERT_BG = new Color(187, 247, 208); // soft green
    public static final Color BTN_INSERT_HOVER = new Color(134, 239, 172);

    public static final Color BTN_UPDATE_BG = new Color(191, 219, 254); // soft blue
    public static final Color BTN_UPDATE_HOVER = new Color(147, 197, 253);

    public static final Color BTN_DELETE_BG = new Color(254, 202, 202); // soft red
    public static final Color BTN_DELETE_HOVER = new Color(252, 165, 165);

    public static final Color BTN_NEUTRAL_BG = new Color(226, 232, 240); // soft gray
    public static final Color BTN_NEUTRAL_HOVER = new Color(203, 213, 225);

    public static final Color BTN_TEXT = Color.BLACK;

    // ---- Status text colors ----
    public static final Color SUCCESS_TEXT = new Color(21, 128, 61);
    public static final Color DANGER_TEXT = new Color(185, 28, 28);

    // ---- Surfaces ----
    public static final Color BG = new Color(248, 250, 252); // slate-50
    public static final Color CARD_BG = Color.WHITE;
    public static final Color BORDER_CLR = new Color(226, 232, 240); // slate-200
    public static final Color STRIPE = new Color(241, 245, 249); // slate-100
    public static final Color SELECTION = new Color(219, 234, 254); // blue-100

    // ---- Text ----
    public static final Color TEXT_MAIN = new Color(15, 23, 42);    // slate-900
    public static final Color TEXT_MUTED = new Color(100, 116, 139);

    // ---- Fonts ----
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 26);
    public static final Font FONT_SUB = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_LABEL = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_FIELD = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_ROW = new Font("Segoe UI", Font.PLAIN, 13);
}