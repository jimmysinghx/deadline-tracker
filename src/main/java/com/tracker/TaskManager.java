package com.tracker;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class TaskManager {
    private List<Task> tasks;
    private final StorageManager storageManager;

    public TaskManager() {
        this.storageManager = new StorageManager();
        this.tasks = storageManager.loadTasks();
    }

    public void addTask(String title, LocalDateTime dueDate, Priority priority) {
        String id = "T" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();
        Task task = new Task(id, title, dueDate, priority);
        tasks.add(task);
        storageManager.saveTask(task);
        System.out.println("Task added: " + task);
    }

    public void deleteTask(String id) {
        Task task = findById(id);
        if (task == null) {
            System.out.println("Task not found: " + id);
            return;
        }
        tasks.remove(task);
        storageManager.deleteTask(id);
        System.out.println("Task deleted: " + id);
    }

    public void markDone(String id) {
        Task task = findById(id);
        if (task == null) {
            System.out.println("Task not found: " + id);
            return;
        }
        task.setStatus(Status.DONE);
        storageManager.updateTask(task);
        System.out.println("Marked as done: " + task.getTitle());
    }

    public void markInProgress(String id) {
        Task task = findById(id);
        if (task == null) {
            System.out.println("Task not found: " + id);
            return;
        }
        task.setStatus(Status.IN_PROGRESS);
        storageManager.updateTask(task);
        System.out.println("Marked as in progress: " + task.getTitle());
    }

    public void listAll() {
        if (tasks.isEmpty()) {
            System.out.println("No tasks found.");
            return;
        }
        tasks.stream()
                .sorted(Comparator.comparing(Task::getDueDate))
                .forEach(System.out::println);
    }

    public void listByStatus(Status status) {
        List<Task> filtered = tasks.stream()
                .filter(t -> t.getStatus() == status)
                .sorted(Comparator.comparing(Task::getDueDate))
                .collect(Collectors.toList());
        if (filtered.isEmpty()) {
            System.out.println("No tasks with status: " + status);
            return;
        }
        filtered.forEach(System.out::println);
    }

    public void listOverdue() {
        List<Task> overdue = tasks.stream()
                .filter(t -> t.getDueDate().isBefore(LocalDateTime.now())
                        && t.getStatus() != Status.DONE)
                .sorted(Comparator.comparing(Task::getDueDate))
                .collect(Collectors.toList());
        if (overdue.isEmpty()) {
            System.out.println("No overdue tasks.");
            return;
        }
        System.out.println("Overdue tasks:");
        overdue.forEach(System.out::println);
    }

    public void summary() {
        long total = tasks.size();
        long done = tasks.stream().filter(t -> t.getStatus() == Status.DONE).count();
        long overdue = tasks.stream()
                .filter(t -> t.getDueDate().isBefore(LocalDateTime.now())
                        && t.getStatus() != Status.DONE)
                .count();
        long pending = tasks.stream().filter(t -> t.getStatus() == Status.TODO).count();
        System.out.println("--------- Summary ---------");
        System.out.println("Total tasks  : " + total);
        System.out.println("Done         : " + done);
        System.out.println("Pending      : " + pending);
        System.out.println("Overdue      : " + overdue);
        System.out.println("---------------------------");
    }

    public List<Task> getUpcomingTasks() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneHourLater = now.plusHours(1);
        return tasks.stream()
                .filter(t -> t.getStatus() != Status.DONE)
                .filter(t -> t.getDueDate().isAfter(now)
                        && t.getDueDate().isBefore(oneHourLater))
                .collect(Collectors.toList());
    }

    private Task findById(String id) {
        return tasks.stream()
                .filter(t -> t.getId().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }
}