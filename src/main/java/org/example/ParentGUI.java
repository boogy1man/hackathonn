package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class ParentGUI extends JPanel {

    AttendanceManager attendanceManager = new AttendanceManager();
    GradeManager gradeManager = new GradeManager();

    JTextField thresholdField = new JTextField("75", 5);
    JComboBox<Integer> semesterBox = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8});
    JLabel alertLabel = new JLabel("Status: Normal");

    DefaultTableModel model = new DefaultTableModel(
            new String[]{"Category", "Subject", "Score / Attendance", "Status"}, 0) {
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    JTable table = new JTable(model);

    public ParentGUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("Parent Module", JLabel.CENTER);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Min Attendance %:"));
        topPanel.add(thresholdField);
        topPanel.add(new JLabel("Semester:"));
        topPanel.add(semesterBox);

        JButton refreshButton = new JButton("Refresh");
        topPanel.add(refreshButton);
        topPanel.add(alertLabel);

        JPanel north = new JPanel(new BorderLayout());
        north.add(title, BorderLayout.NORTH);
        north.add(topPanel, BorderLayout.CENTER);

        add(north, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        refreshButton.addActionListener(e -> loadData());
        semesterBox.addActionListener(e -> loadData());

        loadData();
    }

    void loadData() {
        model.setRowCount(0);

        int threshold = 75;
        try {
            threshold = Integer.parseInt(thresholdField.getText().trim());
        } catch (NumberFormatException e) {
            threshold = 75;
        }

        int sem = (Integer) semesterBox.getSelectedItem();
        boolean alertFound = false;

        try {
            ArrayList<Subject> subjects = attendanceManager.getSubjects();
            for (Subject s : subjects) {
                String status = "OK";
                if (s.getPercentage() < threshold) {
                    status = "Low Attendance!";
                    alertFound = true;
                }

                model.addRow(new Object[]{
                        "Attendance",
                        s.getName(),
                        s.getPercentage() + "% (" + s.getAttended() + "/" + s.getTotal() + ")",
                        status
                });
            }

            ArrayList<Grade> grades = gradeManager.getGrades(sem);
            for (Grade g : grades) {
                String status = "Grade: " + g.getGrade();
                if (g.getMarks() < 60) {
                    status += " (Low Marks)";
                    alertFound = true;
                }

                model.addRow(new Object[]{
                        "Semester " + sem + " Grade",
                        g.getSubject(),
                        g.getMarks() + " Marks",
                        status
                });
            }

            if (alertFound) {
                alertLabel.setForeground(Color.RED);
                alertLabel.setText("Status: Warning - Check Low Records!");
            } else {
                alertLabel.setForeground(Color.DARK_GRAY);
                alertLabel.setText("Status: All Good");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
}