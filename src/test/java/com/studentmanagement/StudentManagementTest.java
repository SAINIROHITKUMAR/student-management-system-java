package com.studentmanagement;

import java.nio.file.Files;
import java.nio.file.Path;

/** Lightweight test runner; keeps the project runnable with only a JDK. */
public final class StudentManagementTest {
    private static int checks;

    public static void main(String[] args) throws Exception {
        gradeValidationAndLetters();
        studentAveragesAndReplacement();
        registrySearchAndCsvRoundTrip();
        reportContainsSummary();
        System.out.println("PASS: " + checks + " checks");
    }

    private static void gradeValidationAndLetters() {
        check(new Grade("Math", 90).letter().equals("A"), "90 is an A");
        check(new Grade("Math", 59.9).letter().equals("F"), "59.9 is an F");
        expectFailure(() -> new Grade("Math", 101), "score range");
    }

    private static void studentAveragesAndReplacement() {
        Student student = new Student("S1", "Test", "Student", "test@example.com");
        student.addGrade(new Grade("Math", 80));
        student.addGrade(new Grade("Science", 100));
        check(student.average() == 90, "average is calculated");
        student.addGrade(new Grade("Math", 60));
        check(student.grades().size() == 2 && student.average() == 80, "subject grade is replaced");
    }

    private static void registrySearchAndCsvRoundTrip() throws Exception {
        StudentRegistry registry = new StudentRegistry();
        Student student = new Student("S1", "Grace", "Hopper", "grace@example.com");
        student.addGrade(new Grade("Programming", 99));
        registry.add(student);
        check(registry.search("hopper").size() == 1, "search is case insensitive");
        Path file = Files.createTempFile("student-management-test", ".csv");
        registry.save(file);
        StudentRegistry restored = new StudentRegistry();
        check(restored.load(file) == 1 && restored.require("S1").average() == 99, "CSV round trip");
        Files.deleteIfExists(file);
    }

    private static void reportContainsSummary() {
        Student student = new Student("S1", "Test", "Student", "test@example.com");
        student.addGrade(new Grade("Math", 75));
        String report = ReportService.studentReport(student);
        check(report.contains("Average: 75.0") && report.contains("PASS"), "student report summary");
    }

    private static void check(boolean condition, String message) {
        checks++;
        if (!condition) throw new AssertionError(message);
    }

    private static void expectFailure(Runnable action, String message) {
        try { action.run(); throw new AssertionError("Expected failure: " + message); }
        catch (IllegalArgumentException expected) { checks++; }
    }
}
