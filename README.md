# Deadline Tracker

A command-line application built in Java to help students and professionals track tasks and deadlines. The app runs a background reminder thread that alerts you when a task is due within the next hour.

## Problem Statement

As a student, I often lose track of multiple assignment deadlines across different subjects. Existing tools are either too complex or require internet access. This project solves that with a simple, offline CLI tool that actively reminds you of upcoming deadlines.

## Features

- Add tasks with due date and priority (HIGH / MEDIUM / LOW)
- List all tasks sorted by due date
- Filter tasks by status (todo / in-progress / done / overdue)
- Mark tasks as in progress or done
- Delete tasks
- Summary report showing total, done, pending and overdue counts
- Background reminder thread that alerts you 1 hour before a deadline
- Data persisted in a local SQLite database using JDBC

## Tech Stack

- Java 17+
- SQLite via JDBC (org.xerial sqlite-jdbc)
- Maven

## Course Concepts Applied

| Concept | Where Used |
|---|---|
| OOP - Classes & Encapsulation | Task, TaskManager, StorageManager |
| Enums | Priority, Status |
| Multithreading | ReminderService using ScheduledExecutorService |
| Exception Handling | try-catch in StorageManager, Main |
| JDBC | StorageManager connects to SQLite database |
| Collections & Streams | TaskManager uses ArrayList, stream filters |
| File I/O | SQLite database stored as local .db file |

## Project Structure
```
deadline-tracker/
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── tracker/
│                   ├── Main.java
│                   ├── Task.java
│                   ├── TaskManager.java
│                   ├── StorageManager.java
│                   ├── ReminderService.java
│                   ├── Priority.java
│                   └── Status.java
├── pom.xml
|── report.md
└── README.md
```

## Setup & Installation

### Prerequisites
- Java 17 or higher
- Maven 3.6+

### Steps

1. Clone the repository:
```
git clone https://github.com/YOUR_USERNAME/deadline-tracker.git
cd deadline-tracker
```

2. Build the project:
```
mvn clean package -q
```

3. Run the application:
```
java -jar target/deadline-tracker-1.0-SNAPSHOT.jar
```

## Usage

### Add a task
```
add "Submit OS assignment" 2026-04-01 23:59 HIGH
```

### List all tasks
```
list
```

### List by status
```
list todo
list done
list in-progress
list --overdue
```

### Mark as done
```
done T7F669
```

### Mark as in progress
```
progress T7F669
```

### Delete a task
```
delete T7F669
```

### View summary
```
summary
```

### Exit
```
exit
```

## Example Session
```
> add "Submit OS assignment" 2026-04-01 23:59 HIGH
Task added: [T7F669] Submit OS assignment | Due: 2026-04-01T23:59 | Priority: HIGH | Status: TODO

> list
[T7F669] Submit OS assignment | Due: 2026-04-01T23:59 | Priority: HIGH   | Status: TODO
[T5B880] Push DBMS project    | Due: 2026-04-02T18:00 | Priority: MEDIUM | Status: TODO

> summary
--------- Summary ---------
Total tasks  : 2
Done         : 0
Pending      : 2
Overdue      : 0
---------------------------

> done T7F669
Marked as done: Submit OS assignment
```

## Author

Jimmy Singh 24BAI10279
