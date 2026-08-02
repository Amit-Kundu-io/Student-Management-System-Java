package com.amit_kundu_io.prasentation;


import com.amit_kundu_io.data.StudentDataServiceImpl;
import com.amit_kundu_io.domain.Student;
import com.amit_kundu_io.domain.StudentDataService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Main application window / controller.
 *
 * Responsibilities:
 *   1. Build and lay out the header, StudentFormPanel, StudentTablePanel and status bar.
 *   2. Wire up button clicks / selection events to the right handler methods.
 *   3. Validate user input before it is sent to storage.
 *   4. Talk to storage ONLY through the {@link StudentDataService} interface.
 *
 * Important design point:
 * This class does NOT know it is talking to MySQL. It holds a reference of
 * type StudentDataService (an interface), not StudentDAO (the concrete
 * MySQL implementation). The only place the concrete class is mentioned is
 * the single "new StudentDAO()" line below — everywhere else in this file
 * we call methods declared on the interface. This means:
 *   - The DB technology could be swapped (e.g. PostgreSQL, a REST API,
 *     an in-memory fake for unit tests) by creating a new class that
 *     implements StudentDataService, with zero changes to this file.
 *   - This class is easier to test, because a fake/mock implementation of
 *     StudentDataService can be injected instead of a real database.
 */
public class StudentManagementUI {

    // ---- Swing window ----
    private final JFrame frame;

    // ---- UI building blocks (each is its own class — see StudentFormPanel / StudentTablePanel) ----
    private final StudentFormPanel formPanel = new StudentFormPanel();
    private final StudentTablePanel tablePanel = new StudentTablePanel();

    // ---- Data layer ----
    // NOTE: the declared type is the INTERFACE (StudentDataService), not the
    // concrete StudentDAO class. This is what makes the UI storage-agnostic.
    // The concrete implementation (MySQL/JDBC) is created just once, here.
    private final StudentDataService studentDataService = new StudentDataServiceImpl();

    // Bottom status bar label, updated after every action (success/error/info).
    private JLabel statusLabel;

    /**
     * Constructs the window, lays out all panels, wires up event handlers,
     * and kicks off the initial database connection/load in the background.
     */
    public StudentManagementUI() {
        // ---- Basic frame setup ----
        frame = new JFrame("Student Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1080, 720);
        frame.setMinimumSize(new Dimension(860, 560));
        frame.setLocationRelativeTo(null); // center on screen

        // ---- Root container: header (top), form+table (center), status bar (bottom) ----
        JPanel main = new JPanel(new BorderLayout(0, 16));
        main.setBackground(Theme.BG);
        main.setBorder(new EmptyBorder(20, 24, 20, 24));

        main.add(buildHeader(), BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(0, 16));
        center.setOpaque(false);
        center.add(formPanel, BorderLayout.NORTH);   // student details + action buttons
        center.add(tablePanel, BorderLayout.CENTER); // search bar + records table
        main.add(center, BorderLayout.CENTER);

        main.add(buildStatusBar(), BorderLayout.SOUTH);

        // Connect button clicks / table selection to their handler methods.
        wireEvents();

        frame.setContentPane(main);
        frame.setVisible(true);

        // Connect to the database and populate the table (runs in the background
        // so the window shows up immediately instead of freezing while connecting).
        initializeDatabaseAndLoadData();
    }

    //  UI CONSTRUCTION

    /**
     * Builds the top header: app title + a short subtitle describing the app.
     */
    private JComponent buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel title = new JLabel("Student Management System");
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.TEXT_MAIN);

        JLabel subtitle = new JLabel("Add, update, search and manage student records (MySQL-backed)");
        subtitle.setFont(Theme.FONT_SUB);
        subtitle.setForeground(Theme.TEXT_MUTED);

        // Stack title above subtitle, left-aligned.
        JPanel textStack = new JPanel();
        textStack.setOpaque(false);
        textStack.setLayout(new BoxLayout(textStack, BoxLayout.Y_AXIS));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        textStack.add(title);
        textStack.add(Box.createVerticalStrut(4));
        textStack.add(subtitle);

        header.add(textStack, BorderLayout.WEST);
        return header;
    }

    /**
     * Builds the thin status bar at the bottom of the window, used to show
     * feedback like "Connected", "Student added", or error messages.
     */
    private JComponent buildStatusBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setOpaque(false);
        bar.setBorder(new EmptyBorder(4, 4, 0, 4));
        statusLabel = new JLabel("Connecting to database...");
        statusLabel.setFont(Theme.FONT_SUB);
        statusLabel.setForeground(Theme.TEXT_MUTED);
        bar.add(statusLabel, BorderLayout.WEST);
        return bar;
    }

    // ======================================================================
    //  EVENT WIRING
    // ======================================================================

    /**
     * Attaches listeners to every interactive component (buttons, search field,
     * table selection). Each listener simply delegates to a private handler
     * method below, keeping this method a quick-to-scan index of "what happens
     * when the user does X".
     */
    private void wireEvents() {
        // ---- Form action buttons ----
        formPanel.insertBtn.addActionListener(e -> onInsert());
        formPanel.updateBtn.addActionListener(e -> onUpdate());
        formPanel.deleteBtn.addActionListener(e -> onDelete());
        formPanel.clearBtn.addActionListener(e -> {
            formPanel.clear();
            tablePanel.table.clearSelection();
            setStatus("Form cleared.", Theme.TEXT_MUTED);
        });

        // ---- Search bar ----
        tablePanel.searchBtn.addActionListener(e -> onSearch());
        tablePanel.searchField.addActionListener(e -> onSearch()); // pressing Enter also searches
        tablePanel.allBtn.addActionListener(e -> {
            tablePanel.table.clearSelection();
            refreshTable(); // reload the full list from the database
        });

        // ---- Clicking a row in the table loads that student into the form ----
        tablePanel.table.getSelectionModel().addListSelectionListener(e -> {
            // getValueIsAdjusting() is true while the mouse is still being dragged
            // across rows; we only want to react once the selection settles.
            if (e.getValueIsAdjusting()) return;

            int r = tablePanel.table.getSelectedRow();
            if (r != -1) {
                formPanel.fill(
                        tablePanel.model.getValueAt(r, 0).toString(), // id
                        tablePanel.model.getValueAt(r, 1).toString(), // name
                        tablePanel.model.getValueAt(r, 2).toString(), // email
                        tablePanel.model.getValueAt(r, 3).toString()
                );
            }
        });
    }

    // ======================================================================
    //  DATABASE LOADING (via the StudentDataService interface)
    // ======================================================================

    /**
     * Runs once, right after the window becomes visible:
     *   1. Asks the data service to create its storage (e.g. the `students`
     *      table) if it doesn't exist yet.
     *   2. Loads every existing student and displays them in the table.
     *
     * This runs on a SwingWorker background thread — never do blocking network/
     * database calls on the Swing Event Dispatch Thread (EDT), or the whole
     * window freezes until the call finishes.
     */
    private void initializeDatabaseAndLoadData() {
        setStatus("Connecting to database...", Theme.TEXT_MUTED);

        SwingWorker<List<Student>, Void> worker = new SwingWorker<>() {
            // Runs on a background thread — safe to do slow I/O here.
            @Override
            protected List<Student> doInBackground() throws Exception {
                return studentDataService.findAll();
            }

            // Runs back on the EDT once doInBackground() finishes — safe to touch Swing components here.
            @Override
            protected void done() {
                try {
                    List<Student> students = get(); // re-throws any exception from doInBackground()
                    populateTable(students);
                    setStatus("Connected. Loaded " + students.size() + " student(s) from the database.", Theme.SUCCESS_TEXT);
                } catch (Exception ex) {
                    showDbError("Could not connect to the database.", ex);
                }
            }
        };
        worker.execute();
    }

    /**
     * Re-fetches the full student list from storage and refreshes the table.
     * Called after every successful insert/update/delete, and by "Show All".
     */
    private void refreshTable() {
        SwingWorker<List<Student>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Student> doInBackground() throws Exception {
                return studentDataService.findAll();
            }

            @Override
            protected void done() {
                try {
                    populateTable(get());
                } catch (Exception ex) {
                    showDbError("Could not refresh the student list.", ex);
                }
            }
        };
        worker.execute();
    }

    /**
     * Clears the table and re-fills it from a list of Student objects.
     * Must be called on the EDT (it touches the Swing table model directly).
     */
    private void populateTable(List<Student> students) {
        tablePanel.model.setRowCount(0); // remove all existing rows
        for (Student s : students) {
            tablePanel.model.addRow(new Object[]{
                    s.getId(), s.getName(), s.getEmail(), s.getCourse()
            });
        }
    }

    /***
    //  ACTIONS (Insert / Update / Delete / Search)
    //  Each follows the same pattern:
    //    1. Read + validate form input on the EDT (fast, no I/O).
    //    2. Do the actual database work in doInBackground() (slow, off the EDT).
    //    3. Report the result back to the user in done() (back on the EDT).


    /** Handles the "Insert" button: validates input, then adds a new student. */
    private void onInsert() {
        //  read and validate form input ----
        String name = formPanel.nameField.getText().trim();
        String id = formPanel.idField.getText().trim();
        String email = formPanel.emailField.getText().trim();
        String course = formPanel.courseField.getText().trim();

        if ( name.isEmpty()) {
            warn("Student ID and Name are required.");
            return;
        }

        if (!email.isEmpty() && !isValidEmail(email)) {
            warn("Please enter a valid email address.");
            return;
        }

        // Build the object we'll hand to the data layer.
        Student s = new Student(Integer.parseInt(id),name, email, course);

        //  save in the background, then refresh/report on the EDT ----
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {

                studentDataService.insert(s);
                return null;
            }

            @Override
            protected void done() {
                try {
                    get(); // throws if doInBackground() threw
                    formPanel.clear();
                    refreshTable();
                    setStatus("Student \"" + name + "\" added successfully.", Theme.SUCCESS_TEXT);
                } catch (Exception ex) {
                    showDbError("Could not add student.", ex);
                }
            }
        };
        worker.execute();
    }

    /** Handles the "Update" button: validates input, then saves changes to the selected student. */
    private void onUpdate() {
        int row = tablePanel.table.getSelectedRow();
        if (row == -1) {
            warn("Select a student from the table first.");
            return;
        }

        //  read and validate form input ----
        String id = formPanel.idField.getText().trim();
        String name = formPanel.nameField.getText().trim();
        String email = formPanel.emailField.getText().trim();
        String course = formPanel.courseField.getText().trim();

        if ( name.isEmpty()) {
            warn("Student ID and Name are required.");
            return;
        }
        if (!email.isEmpty() && !isValidEmail(email)) {
            warn("Please enter a valid email address.");
            return;
        }


        // The ID currently stored in the selected row, BEFORE any edits.
        // We need this to detect whether the user changed the ID itself.
        String originalId = tablePanel.model.getValueAt(row, 0).toString();
        Student s = new Student( Integer.parseInt(id), name, email, course);

        //  save in the background, then refresh/report on the EDT ----
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                    studentDataService.update(s);
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    formPanel.clear();
                    refreshTable();
                    setStatus("Student \"" + name + "\" updated successfully.", Theme.SUCCESS_TEXT);
                } catch (Exception ex) {
                    showDbError("Could not update student.", ex);
                }
            }
        };
        worker.execute();
    }

    /** Handles the "Delete" button: confirms with the user, then removes the selected student. */
    private void onDelete() {
        int row = tablePanel.table.getSelectedRow();
        if (row == -1) {
            warn("Select a student from the table first.");
            return;
        }
        String id = tablePanel.model.getValueAt(row, 0).toString();
        String name = tablePanel.model.getValueAt(row, 1).toString();

        // Deletion is destructive — always ask for confirmation first.
        int confirm = JOptionPane.showConfirmDialog(
                frame,
                "Delete student \"" + name + "\"? This cannot be undone.",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );
        if (confirm != JOptionPane.YES_OPTION) return; // user backed out

        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                studentDataService.delete(id);
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    formPanel.clear();
                    refreshTable();
                    setStatus("Student \"" + name + "\" deleted.", Theme.DANGER_TEXT);
                } catch (Exception ex) {
                    showDbError("Could not delete student.", ex);
                }
            }
        };
        worker.execute();
    }

    /** Handles the "Search" button (and Enter key in the search field): finds a student by ID. */
    private void onSearch() {
        String id = tablePanel.searchField.getText().trim();
        if (id.isEmpty()) {
            warn("Enter a Student ID to search.");
            return;
        }

        SwingWorker<Student, Void> worker = new SwingWorker<>() {
            @Override
            protected Student doInBackground() throws Exception {
                return studentDataService.findById(id);
            }

            @Override
            protected void done() {
                try {
                    Student s = get();
                    if (s != null) {
                        int row = tablePanel.findRowById(id);
                        if (row == -1) {
                            // The student exists in the DB but isn't currently shown
                            // in the table (e.g. right after "Show All" was cleared)
                            // — reload the table so it's visible.
                            refreshTable();
                        } else {
                            tablePanel.table.setRowSelectionInterval(row, row);
                            tablePanel.table.scrollRectToVisible(tablePanel.table.getCellRect(row, 0, true));
                        }
                        setStatus("Found student with ID " + id + ".", Theme.SUCCESS_TEXT);
                    } else {
                        tablePanel.table.clearSelection();
                        warn("No student found with ID " + id + ".");
                    }
                } catch (Exception ex) {
                    showDbError("Search failed.", ex);
                }
            }
        };
        worker.execute();
    }

    //  VALIDATION HELPERS

    /** Basic email format check: something@something.tld */
    private boolean isValidEmail(String email) {
        return Pattern.matches("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$", email);
    }

    /** Marks must parse as a number and fall within the 0–100 range. */
    private boolean isValidMarks(String marks) {
        try {
            double m = Double.parseDouble(marks);
            return m >= 0 && m <= 100;
        } catch (NumberFormatException ex) {
            return false; // not a number at all
        }
    }

    //  FEEDBACK HELPERS (status bar + dialogs)

    /** Shows a warning dialog AND updates the status bar in red. Used for validation/user errors. */
    private void warn(String message) {
        setStatus(message, Theme.DANGER_TEXT);
        JOptionPane.showMessageDialog(frame, message, "Notice", JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Shows a friendly message for failures coming out of the data layer
     * (bad credentials, server down, missing driver, duplicate ID, etc.)
     * instead of letting a raw stack trace reach the user.
     *
     * SwingWorker wraps whatever doInBackground() throws inside an
     * ExecutionException, so the "real" cause is usually one level down —
     * we unwrap it here to show a meaningful message.
     */
    private void showDbError(String friendlyMessage, Exception ex) {
        Throwable cause = ex.getCause() != null ? ex.getCause() : ex;
        String detail = cause.getMessage() != null ? cause.getMessage() : cause.toString();

        if (cause instanceof SQLException || cause instanceof IllegalStateException) {
            // IllegalStateException = our own "duplicate ID" checks — message is already user-friendly.
            // SQLException = a real database problem — prefix with the friendly explanation.
            warn(cause instanceof IllegalStateException ? detail : friendlyMessage + "\n" + detail);
        } else {
            // Anything unexpected (e.g. missing JDBC driver) — show both the friendly text and the detail.
            warn(friendlyMessage + "\n" + detail);
        }
    }

    /** Updates the status bar text and color (e.g. green for success, red for errors). */
    private void setStatus(String message, Color color) {
        statusLabel.setText(message);
        statusLabel.setForeground(color);
    }
}