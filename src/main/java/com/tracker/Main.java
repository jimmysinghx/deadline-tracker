package com.tracker;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Main {
    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        ReminderService reminderService = new ReminderService(taskManager);
        reminderService.start();

        Scanner scanner = new Scanner(System.in);
        printHelp();

        while (true) {
            System.out.print("\n> ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) continue;

            String[] parts = input.split(" ", 2);
            String command = parts[0].toLowerCase();

            try {
                switch (command) {
                    case "add" -> handleAdd(parts, taskManager);
                    case "list" -> handleList(parts, taskManager);
                    case "done" -> {
                        if (parts.length < 2) { System.out.println("Usage: done <id>"); break; }
                        taskManager.markDone(parts[1].trim());
                    }
                    case "progress" -> {
                        if (parts.length < 2) { System.out.println("Usage: progress <id>"); break; }
                        taskManager.markInProgress(parts[1].trim());
                    }
                    case "delete" -> {
                        if (parts.length < 2) { System.out.println("Usage: delete <id>"); break; }
                        taskManager.deleteTask(parts[1].trim());
                    }
                    case "summary" -> taskManager.summary();
                    case "help" -> printHelp();
                    case "exit" -> {
                        reminderService.stop();
                        System.out.println("Goodbye!");
                        scanner.close();
                        System.exit(0);
                    }
                    default -> System.out.println("Unknown command. Type 'help' for commands.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void handleAdd(String[] parts, TaskManager taskManager) {
        if (parts.length < 2) {
            System.out.println("Usage: add \"<title>\" <yyyy-MM-dd HH:mm> <HIGH|MEDIUM|LOW>");
            return;
        }
        String[] tokens = parts[1].split("\"");
        if (tokens.length < 3) {
            System.out.println("Usage: add \"<title>\" <yyyy-MM-dd HH:mm> <HIGH|MEDIUM|LOW>");
            return;
        }
        String title = tokens[1].trim();
        String[] rest = tokens[2].trim().split(" ");
        if (rest.length < 3) {
            System.out.println("Usage: add \"<title>\" <yyyy-MM-dd HH:mm> <HIGH|MEDIUM|LOW>");
            return;
        }
        try {
            LocalDateTime dueDate = LocalDateTime.parse(rest[0] + " " + rest[1], formatter);
            Priority priority = Priority.valueOf(rest[2].toUpperCase());
            taskManager.addTask(title, dueDate, priority);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Use: yyyy-MM-dd HH:mm");
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid priority. Use: HIGH, MEDIUM or LOW");
        }
    }

    private static void handleList(String[] parts, TaskManager taskManager) {
        if (parts.length == 1) {
            taskManager.listAll();
            return;
        }
        switch (parts[1].trim().toLowerCase()) {
            case "done"        -> taskManager.listByStatus(Status.DONE);
            case "todo"        -> taskManager.listByStatus(Status.TODO);
            case "in-progress" -> taskManager.listByStatus(Status.IN_PROGRESS);
            case "--overdue"   -> taskManager.listOverdue();
            default -> System.out.println("Usage: list [done|todo|in-progress|--overdue]");
        }
    }

    private static void printHelp() {
        System.out.println("--------- Deadline Tracker ---------");
        System.out.println("Commands:");
        System.out.println("  add \"<title>\" <yyyy-MM-dd HH:mm> <HIGH|MEDIUM|LOW>");
        System.out.println("  list");
        System.out.println("  list done | todo | in-progress | --overdue");
        System.out.println("  done <id>");
        System.out.println("  progress <id>");
        System.out.println("  delete <id>");
        System.out.println("  summary");
        System.out.println("  help");
        System.out.println("  exit");
        System.out.println("------------------------------------");
    }
}