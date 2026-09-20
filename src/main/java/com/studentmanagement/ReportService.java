package com.studentmanagement;

import java.util.List;
import java.util.Locale;

public final class ReportService {
    private ReportService() {}

    public static String studentReport(Student student) {
        StringBuilder report = new StringBuilder();
        report.append("\nStudent Report\n--------------\n")
                .append(student.id()).append(" | ").append(student.fullName()).append(" | ").append(student.email()).append('\n');
        if (student.grades().isEmpty()) {
            return report.append("No grades recorded.\nAverage: N/A\nStatus: N/A\n").toString();
        }
        report.append("Grades:\n");
        student.grades().forEach(g -> report.append("  - ").append(g).append('\n'));
        report.append("Average: ").append(student.averageLabel()).append('\n')
                .append("Status: ").append(student.passed() ? "PASS" : "AT RISK").append('\n');
        return report.toString();
    }

    public static String classReport(List<Student> students) {
        long graded = students.stream().filter(s -> !s.grades().isEmpty()).count();
        double average = students.stream().filter(s -> !s.grades().isEmpty()).mapToDouble(Student::average).average().orElse(Double.NaN);
        long passed = students.stream().filter(Student::passed).count();
        return String.format(Locale.ROOT, "\nClass Report\n------------\nStudents: %d\nStudents with grades: %d\nClass average: %s\nPass rate: %s\n",
                students.size(), graded, Double.isNaN(average) ? "N/A" : String.format(Locale.ROOT, "%.1f", average),
                graded == 0 ? "N/A" : String.format(Locale.ROOT, "%.1f%%", passed * 100.0 / graded));
    }
}
