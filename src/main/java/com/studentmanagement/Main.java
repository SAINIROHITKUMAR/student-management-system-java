package com.studentmanagement;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;

public final class Main {
    private final StudentRegistry registry = new StudentRegistry();
    private final Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        new Main().run();
    }

    private void run() {
        seed();
        System.out.println("Student Management System");
        System.out.println("Type a menu number, or 0 to exit.");
        while (true) {
            printMenu();
            String choice = input.nextLine().trim();
            try {
                switch (choice) {
                    case "1" -> list();
                    case "2" -> addStudent();
                    case "3" -> addGrade();
                    case "4" -> report();
                    case "5" -> search();
                    case "6" -> remove();
                    case "7" -> save();
                    case "8" -> load();
                    case "0" -> { System.out.println("Goodbye."); return; }
                    default -> System.out.println("Please choose a number from 0 to 8.");
                }
            } catch (IllegalArgumentException | IOException ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        }
    }

    private void printMenu() {
        System.out.println("\n[1] List  [2] Add student  [3] Record grade  [4] Report");
        System.out.println("[5] Search [6] Remove       [7] Save CSV     [8] Load CSV [0] Exit");
        System.out.print("> ");
    }

    private void list() {
        if (registry.size() == 0) { System.out.println("No students."); return; }
        System.out.printf("%-8s %-24s %-28s %s%n", "ID", "NAME", "EMAIL", "AVERAGE");
        registry.all().forEach(s -> System.out.printf("%-8s %-24s %-28s %s%n", s.id(), s.fullName(), s.email(), s.averageLabel()));
    }

    private void addStudent() {
        Student student = new Student(ask("ID"), ask("First name"), ask("Last name"), ask("Email"));
        registry.add(student);
        System.out.println("Student added.");
    }

    private void addGrade() {
        Student student = registry.require(ask("Student ID"));
        student.addGrade(new Grade(ask("Subject"), Double.parseDouble(ask("Score (0-100)"))));
        System.out.println("Grade recorded.");
    }

    private void report() { System.out.println(ReportService.studentReport(registry.require(ask("Student ID")))); }
    private void search() { registry.search(ask("Search")).forEach(s -> System.out.println(s.id() + " | " + s.fullName() + " | " + s.email())); }
    private void remove() { System.out.println(registry.remove(ask("Student ID")) ? "Student removed." : "Student not found."); }
    private void save() throws IOException { Path path = Path.of(ask("CSV path")); registry.save(path); System.out.println("Saved " + registry.size() + " student(s)."); }
    private void load() throws IOException { Path path = Path.of(ask("CSV path")); System.out.println("Loaded " + registry.load(path) + " student(s)."); }
    private String ask(String label) { System.out.print(label + ": "); return input.nextLine().trim(); }

    private void seed() {
        Student ada = new Student("S001", "Ada", "Lovelace", "ada@example.com");
        ada.addGrade(new Grade("Mathematics", 96));
        ada.addGrade(new Grade("Physics", 91));
        Student alan = new Student("S002", "Alan", "Turing", "alan@example.com");
        alan.addGrade(new Grade("Mathematics", 84));
        alan.addGrade(new Grade("Computer Science", 94));
        registry.add(ada);
        registry.add(alan);
    }
}
