# Student Management System (Java)

A dependency-free Java 17 CLI for student records, subject grades, CSV persistence, and individual/class reports.

## Run

```powershell
.\run.ps1
```

## Test

```powershell
.\test.ps1
```

The app seeds sample records on startup. Menu options cover listing, adding students, recording grades, searching, removing, reporting, and CSV save/load. CSV columns are `studentId,firstName,lastName,email,subject,score`.

## Desktop UI

Run `.\run.ps1 desktop` on Windows to open a Swing desktop interface. It lists students, lets you add records, and opens an individual report. The terminal CLI remains available with `.\run.ps1`.
