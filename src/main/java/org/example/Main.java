package org.example;

import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {

    public Main() {

        setTitle("Academic Tracker");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        CardLayout layout = new CardLayout();
        JPanel cards = new JPanel(layout);

        cards.add(new AttendanceGUI(), "attendance");
        cards.add(new GradeGUI(), "grades");
        cards.add(new ParentGUI(), "parent");

        JButton attendanceButton = new JButton("Attendance");
        JButton gradeButton = new JButton("Grade Card");
        JButton parentButton = new JButton("Parent Module");

        attendanceButton.addActionListener(
                e -> layout.show(cards, "attendance"));

        gradeButton.addActionListener(
                e -> layout.show(cards, "grades"));

        parentButton.addActionListener(
                e -> layout.show(cards, "parent"));

        JPanel buttons = new JPanel();
        buttons.add(attendanceButton);
        buttons.add(gradeButton);
        buttons.add(parentButton);

        add(buttons, BorderLayout.NORTH);
        add(cards, BorderLayout.CENTER);
    }

    public static void main(String[] args) {

        try {
            DataBase.initialize();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                    null,
                    "Database error: " + e.getMessage()
            );
            return;
        }

        SwingUtilities.invokeLater(() -> {
            new Main().setVisible(true);
        });
    }
}