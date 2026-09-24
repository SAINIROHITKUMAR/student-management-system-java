package com.studentmanagement;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/** Desktop UI for the student registry. */
public final class DesktopMain {
    private final StudentRegistry registry = new StudentRegistry();
    private final DefaultTableModel model = new DefaultTableModel(new Object[]{"ID", "Name", "Email", "Average"}, 0);
    private final JTable table = new JTable(model);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DesktopMain().show());
    }

    private void show() {
        seed();
        JFrame frame = new JFrame("Student Management System");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(760, 480);
        frame.setLocationByPlatform(true);
        JButton add = new JButton("Add student");
        JButton report = new JButton("View report");
        add.addActionListener(event -> addStudent(frame));
        report.addActionListener(event -> report(frame));
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actions.add(add); actions.add(report);
        frame.add(actions, BorderLayout.NORTH);
        frame.add(new JScrollPane(table), BorderLayout.CENTER);
        refresh();
        frame.setVisible(true);
    }

    private void addStudent(Component parent) {
        JTextField id = new JTextField(), first = new JTextField(), last = new JTextField(), email = new JTextField();
        Object[] fields = {"Student ID", id, "First name", first, "Last name", last, "Email", email};
        if (JOptionPane.showConfirmDialog(parent, fields, "Add student", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;
        try {
            registry.add(new Student(id.getText(), first.getText(), last.getText(), email.getText()));
            refresh();
        } catch (IllegalArgumentException error) {
            JOptionPane.showMessageDialog(parent, error.getMessage(), "Invalid student", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void report(Component parent) {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(parent, "Select a student first."); return; }
        String id = (String) model.getValueAt(row, 0);
        JOptionPane.showMessageDialog(parent, ReportService.studentReport(registry.require(id)), "Student report", JOptionPane.INFORMATION_MESSAGE);
    }

    private void refresh() {
        model.setRowCount(0);
        registry.all().forEach(student -> model.addRow(new Object[]{student.id(), student.fullName(), student.email(), student.averageLabel()}));
    }

    private void seed() {
        Student ada = new Student("S001", "Ada", "Lovelace", "ada@example.com");
        ada.addGrade(new Grade("Mathematics", 96)); registry.add(ada);
        Student alan = new Student("S002", "Alan", "Turing", "alan@example.com");
        alan.addGrade(new Grade("Computer Science", 94)); registry.add(alan);
    }
}
