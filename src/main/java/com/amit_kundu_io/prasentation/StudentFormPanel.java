package com.amit_kundu_io.prasentation;


import javax.swing.*;
import java.awt.*;

/**
 * Card containing the Student Details form: ID / Name / Email / Course / Marks
 * fields plus the Insert / Update / Delete / Clear action buttons.
 * <p>
 * This panel only builds UI and exposes its fields/buttons publicly.
 * All business logic (validation, saving, etc.) lives in StudentManagementUI.
 */
public class StudentFormPanel extends JPanel {

    public final JTextField idField = UIComponents.styledField();
    public final JTextField nameField = UIComponents.styledField();
    public final JTextField emailField = UIComponents.styledField();
    public final JTextField courseField = UIComponents.styledField();

    public final JButton insertBtn = UIComponents.pillButton("+ Insert", Theme.BTN_INSERT_BG, Theme.BTN_INSERT_HOVER);
    public final JButton updateBtn = UIComponents.pillButton("Update", Theme.BTN_UPDATE_BG, Theme.BTN_UPDATE_HOVER);
    public final JButton deleteBtn = UIComponents.pillButton("Delete", Theme.BTN_DELETE_BG, Theme.BTN_DELETE_HOVER);
    public final JButton clearBtn = UIComponents.pillButton("Clear Form", Theme.BTN_NEUTRAL_BG, Theme.BTN_NEUTRAL_HOVER);

    public StudentFormPanel() {
        JPanel card = UIComponents.card();
        card.setLayout(new BorderLayout(0, 14));

        JLabel cardTitle = new JLabel("Student Details");
        cardTitle.setFont(Theme.FONT_LABEL.deriveFont(15f));
        cardTitle.setForeground(Theme.TEXT_MAIN);
        card.add(cardTitle, BorderLayout.NORTH);

        card.add(buildForm(), BorderLayout.CENTER);
        card.add(buildActionRow(), BorderLayout.SOUTH);

        setLayout(new BorderLayout());
        add(card, BorderLayout.CENTER);
    }

    private JPanel buildForm() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6, 8, 6, 8);
        gc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        addFormRow(form, gc, row++, "Student ID *", idField, "Name *", nameField);
        addFormRow(form, gc, row++, "Email", emailField, "Course", courseField);

        return form;
    }

    private void addFormRow(JPanel form, GridBagConstraints gc, int row,
                            String label1, JComponent field1,
                            String label2, JComponent field2) {
        gc.gridy = row;

        gc.gridx = 0;
        gc.weightx = 0;
        form.add(UIComponents.fieldLabel(label1), gc);

        gc.gridx = 1;
        gc.weightx = 1;
        form.add(field1, gc);

        if (label2 != null) {
            gc.gridx = 2;
            gc.weightx = 0;
            form.add(UIComponents.fieldLabel(label2), gc);

            gc.gridx = 3;
            gc.weightx = 1;
            form.add(field2, gc);
        } else {
            gc.gridx = 2;
            gc.weightx = 0;
            form.add(Box.createHorizontalStrut(1), gc);
            gc.gridx = 3;
            gc.weightx = 1;
            form.add(Box.createHorizontalStrut(1), gc);
        }
    }

    private JPanel buildActionRow() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
        row.setOpaque(false);
        row.add(insertBtn);
        row.add(updateBtn);
        row.add(deleteBtn);
        row.add(clearBtn);
        return row;
    }

    /**
     * Clears all input fields.
     */
    public void clear() {
        idField.setText("");
        nameField.setText("");
        emailField.setText("");
        courseField.setText("");
    }

    public void fill(String id, String name, String email, String course, String marks) {
        idField.setText(id);
        nameField.setText(name);
        emailField.setText(email);
        courseField.setText(course);
    }
}