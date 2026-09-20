package com.studentmanagement;

import java.util.Locale;

/** A score for one subject. */
public record Grade(String subject, double score) {
    public Grade {
        if (subject == null || subject.isBlank()) {
            throw new IllegalArgumentException("Subject is required.");
        }
        subject = subject.trim();
        if (!Double.isFinite(score) || score < 0 || score > 100) {
            throw new IllegalArgumentException("Score must be between 0 and 100.");
        }
    }

    public String letter() {
        if (score >= 90) return "A";
        if (score >= 80) return "B";
        if (score >= 70) return "C";
        if (score >= 60) return "D";
        return "F";
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT, "%s: %.1f (%s)", subject, score, letter());
    }
}
