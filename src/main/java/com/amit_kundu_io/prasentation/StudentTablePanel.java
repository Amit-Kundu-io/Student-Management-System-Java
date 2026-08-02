package com.amit_kundu_io.prasentation;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Card containing the search bar and the striped student records table.
 * Business logic (search matching, etc.) lives in StudentManagementUI;
 * this class only builds and exposes the UI + table model.
 */
public class StudentTablePanel extends JPanel {

    public final DefaultTableModel model =
            new DefaultTableModel(new Object[]{"ID", "Name", "Email", "Course", "Marks"}, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

    public final JTable table = new JTable(model);
    public final JTextField searchField = UIComponents.styledField();
    public final JButton searchBtn = UIComponents.pillButton("Search", Theme.BTN_UPDATE_BG, Theme.BTN_UPDATE_HOVER);
    public final JButton allBtn = UIComponents.pillButton("Show All", Theme.BTN_NEUTRAL_BG, Theme.BTN_NEUTRAL_HOVER);

    public StudentTablePanel() {
        JPanel card = UIComponents.card();
        card.setLayout(new BorderLayout(0, 12));

        card.add(buildTopBar(), BorderLayout.NORTH);
        card.add(buildTable(), BorderLayout.CENTER);

        setLayout(new BorderLayout());
        add(card, BorderLayout.CENTER);
    }

    private JPanel buildTopBar() {
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);

        JLabel cardTitle = new JLabel("Student Records");
        cardTitle.setFont(Theme.FONT_LABEL.deriveFont(15f));
        cardTitle.setForeground(Theme.TEXT_MAIN);
        top.add(cardTitle, BorderLayout.WEST);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        searchPanel.setOpaque(false);

        searchField.setColumns(14);
        searchField.putClientProperty("JTextField.placeholderText", "Search by ID...");

        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);
        searchPanel.add(allBtn);

        top.add(searchPanel, BorderLayout.EAST);
        return top;
    }

    private JScrollPane buildTable() {
        table.setRowHeight(30);
        table.setFont(Theme.FONT_ROW);
        table.setSelectionBackground(Theme.SELECTION);
        table.setSelectionForeground(Theme.TEXT_MAIN);
        table.setGridColor(Theme.BORDER_CLR);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setFillsViewportHeight(true);

        table.getTableHeader().setFont(Theme.FONT_HEADER);
        table.getTableHeader().setBackground(new Color(241, 245, 249));
        table.getTableHeader().setForeground(Theme.TEXT_MAIN);
        table.getTableHeader().setPreferredSize(new Dimension(0, 36));
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER_CLR));

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : Theme.STRIPE);
                }
                setBorder(new EmptyBorder(0, 12, 0, 12));
                return c;
            }
        };
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(Theme.BORDER_CLR));
        scrollPane.getViewport().setBackground(Color.WHITE);
        return scrollPane;
    }

    /**
     * Finds the table row index for a given student ID, or -1 if not found.
     */
    public int findRowById(String id) {
        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, 0).toString().equals(id)) return i;
        }
        return -1;
    }
}