# Project Report on  Deadline Tracker

## 1. Problem Statement

As a student managing multiple subjects simultaneously, I have personally missed assignment
deadlines because I either forgot about them or did not have a simple way to track them all
in one place. Existing tools like Google Calendar or Notion are either too heavy, require
internet access, or are not built specifically for tracking academic deadlines with priority
levels. I wanted something lightweight, offline, and that would actively remind me  not
just something I had to remember to check.

This is the problem I set out to solve with the Deadline Tracker.

## 2. Why It Matters

Missing deadlines has real consequences  lost marks, incomplete submissions, and
unnecessary stress. The problem is not unique to me  most students juggle 5 or more
subjects at a time and rely on memory or scattered notes to track due dates. A dedicated
CLI tool that runs in the background and alerts you when a deadline is approaching directly
addresses this gap.

## 3. Approach

I built the Deadline Tracker as a Java command-line application with the following design
goals in mind:

- Simple to use  : a few short commands to add, list and manage tasks
- Persistent :  tasks should survive after the app is closed
- Proactive :  the app should remind the user without them having to ask

I structured the project around five core classes, each with a single responsibility:

- **Task** :  represents a single task with a title, due date, priority and status
- **Priority / Status** : enums representing task urgency and completion state
- **StorageManager** : handles all database operations using JDBC and SQLite
- **TaskManager** : manages the task list in memory and exposes all operations
- **ReminderService** : runs a background thread that checks for upcoming deadlines
- **Main** : the CLI entry point that reads user input and routes commands

## 4. Key Decisions

### SQLite over flat file storage
I initially considered storing tasks in a JSON file, but switched to SQLite via JDBC
because it maps more directly to the course content (Unit 5 JDBC) and is more robust
for structured data. SQLite requires no server setup and stores data in a single `.db`
file, making it simple to use offline.

### ScheduledExecutorService for reminders
Rather than using a plain `Thread` with a `while(true)` loop and `Thread.sleep()`, I used
`ScheduledExecutorService` which is cleaner, safer and easier to shut down gracefully. This
was the most challenging part of the project because I had to understand how background
threads interact with the main thread and how to avoid race conditions when reading the
task list.

### Enum for Priority and Status
Instead of storing priority and status as plain strings, I used Java enums. This prevents
invalid values from being stored and makes the code easier to read and maintain.

### Maven for dependency management
I used Maven to manage the SQLite JDBC dependency and build a fat jar using the
maven-shade-plugin. This made the project easy to distribute  anyone can run it with a
single `java -jar` command without installing anything extra.

## 5. Challenges Faced

### Multithreading
The hardest part of this project was implementing the `ReminderService`. I had not worked
with `ScheduledExecutorService` before and had to understand how it schedules tasks on a
separate thread, how to pass shared state to it safely, and how to shut it down cleanly
when the user types `exit`. Getting this right took multiple attempts.

### JDBC and SQLite setup
Connecting Java to SQLite using JDBC was new to me. The initial error I encountered was
"No suitable driver found" which happened because the SQLite driver was not bundled in the
jar. I resolved this by switching from the default maven-jar-plugin to maven-shade-plugin
which packages all dependencies into a single fat jar.

### Maven on Windows
Setting up Maven on Windows required manually configuring the PATH environment variable
in PowerShell, which was not straightforward. This was a good lesson in understanding how
development tools are configured at the system level.

## 6. Course Concepts Applied

| Concept | Unit | How It Was Applied |
|---|---|---|
| Classes and Objects | Unit 2 | Task, TaskManager, StorageManager, ReminderService |
| Constructors and Encapsulation | Unit 2 | All classes use private fields with public getters |
| Enums | Unit 2 | Priority (HIGH/MEDIUM/LOW) and Status (TODO/IN_PROGRESS/DONE) |
| Exception Handling | Unit 3 | try-catch blocks in StorageManager and Main |
| Multithreading | Unit 3 | ReminderService uses ScheduledExecutorService |
| Collections and Streams | Unit 4 | ArrayList, stream filters, Comparator for sorting |
| File I/O | Unit 4 | SQLite database stored as a local .db file |
| JDBC | Unit 5 | StorageManager uses JDBC to connect, query and update SQLite |

## 7. What I Learned

This project taught me how to build a real Java application from scratch, not just write
isolated programs for exercises. Specifically:

- How JDBC connects a Java application to a relational database and how to use
  PreparedStatements to safely insert and query data
- How multithreading works in a real application  not just creating a thread but managing
  its lifecycle, scheduling it, and shutting it down cleanly
- How to structure a Java project using Maven, manage dependencies, and build a
  distributable jar file
- How to apply object-oriented principles like encapsulation and single responsibility in
  a project with multiple interacting classes

The most valuable insight was that real programming involves a lot of setup, debugging and
small decisions that exercises don't prepare you for  like fixing a missing driver error
or figuring out why a thread isn't shutting down properly.

## 8. Conclusion

The Deadline Tracker is a purposeful, well-structured Java application that solves a real
problem I personally experienced. It applies the core concepts from the Programming in Java
course  OOP, exception handling, multithreading, collections, JDBC in a cohesive and
practical way. The project is small in scale but complete in execution, and I am confident
it demonstrates both technical understanding and problem-solving ability.