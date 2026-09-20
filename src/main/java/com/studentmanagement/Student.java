package com.studentmanagement;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/** A student and their subject grades. */
public final class Student {
    private final String id;
    private String firstName;
    private String lastName;
    private String email;
    private final List<Grade> grades = new ArrayList<>();

    public Student(String id, String firstName, String lastName, String email) {
        this.id = required(id, "Student ID");
        this.firstName = required(firstName, "First name");
        this.lastName = required(lastName, "Last name");
        this.email = required(email, "Email");
    }

    public String id() { return id; }
    public String firstName() { return firstName; }
    public String lastName() { return lastName; }
    public String email() { return email; }
    public String fullName() { return firstName + " " + lastName; }
    public List<Grade> grades() { return List.copyOf(grades); }

    public void update(String firstName, String lastName, String email) {
        this.firstName = required(firstName, "First name");
        this.lastName = required(lastName, "Last name");
        this.email = required(email, "Email");
    }

    public void addGrade(Grade grade) {
        grades.removeIf(existing -> existing.subject().equalsIgnoreCase(grade.subject()));
        grades.add(grade);
        grades.sort(Comparator.comparing(Grade::subject, String.CASE_INSENSITIVE_ORDER));
    }

    public double average() {
        return grades.stream().mapToDouble(Grade::score).average().orElse(Double.NaN);
    }

    public boolean passed() {
        return !grades.isEmpty() && average() >= 60;
    }

    public String averageLabel() {
        return Double.isNaN(average()) ? "N/A" : String.format(Locale.ROOT, "%.1f", average());
    }

    private static String required(String value, String label) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException(label + " is required.");
        return value.trim();
    }
}
