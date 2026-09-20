package com.studentmanagement;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** In-memory registry with CSV persistence. */
public final class StudentRegistry {
    private static final String HEADER = "studentId,firstName,lastName,email,subject,score";
    private final Map<String, Student> students = new LinkedHashMap<>();

    public void add(Student student) {
        if (students.putIfAbsent(student.id(), student) != null) {
            throw new IllegalArgumentException("A student with ID " + student.id() + " already exists.");
        }
    }

    public Student get(String id) {
        return students.get(id.trim());
    }

    public Student require(String id) {
        Student student = get(id);
        if (student == null) throw new IllegalArgumentException("Student not found: " + id);
        return student;
    }

    public boolean remove(String id) { return students.remove(id.trim()) != null; }
    public int size() { return students.size(); }
    public List<Student> all() {
        return students.values().stream().sorted(Comparator.comparing(Student::id)).toList();
    }

    public List<Student> search(String query) {
        String needle = query.toLowerCase();
        return all().stream().filter(s -> (s.id() + " " + s.fullName() + " " + s.email())
                .toLowerCase().contains(needle)).toList();
    }

    public void save(Path path) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            writer.write(HEADER);
            writer.newLine();
            for (Student student : all()) {
                for (Grade grade : student.grades()) {
                    writer.write(String.join(",", csv(student.id()), csv(student.firstName()), csv(student.lastName()),
                            csv(student.email()), csv(grade.subject()), Double.toString(grade.score())));
                    writer.newLine();
                }
                if (student.grades().isEmpty()) {
                    writer.write(String.join(",", csv(student.id()), csv(student.firstName()), csv(student.lastName()),
                            csv(student.email()), "", ""));
                    writer.newLine();
                }
            }
        }
    }

    public int load(Path path) throws IOException {
        Map<String, Student> loaded = new LinkedHashMap<>();
        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line;
            boolean first = true;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.isBlank()) continue;
                if (first && line.equalsIgnoreCase(HEADER)) { first = false; continue; }
                first = false;
                List<String> fields = parseCsv(line);
                if (fields.size() != 6) throw new IOException("Invalid CSV at line " + lineNumber);
                try {
                    Student student = loaded.computeIfAbsent(fields.get(0),
                            ignored -> new Student(fields.get(0), fields.get(1), fields.get(2), fields.get(3)));
                    if (!fields.get(4).isBlank()) student.addGrade(new Grade(fields.get(4), Double.parseDouble(fields.get(5))));
                } catch (RuntimeException ex) {
                    throw new IOException("Invalid CSV at line " + lineNumber + ": " + ex.getMessage(), ex);
                }
            }
        }
        students.clear();
        students.putAll(loaded);
        return loaded.size();
    }

    private static String csv(String value) {
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }

    private static List<String> parseCsv(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder field = new StringBuilder();
        boolean quoted = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                if (quoted && i + 1 < line.length() && line.charAt(i + 1) == '"') { field.append('"'); i++; }
                else quoted = !quoted;
            } else if (c == ',' && !quoted) { fields.add(field.toString()); field.setLength(0); }
            else field.append(c);
        }
        if (quoted) throw new IllegalArgumentException("Unclosed CSV quote.");
        fields.add(field.toString());
        return fields;
    }
}
