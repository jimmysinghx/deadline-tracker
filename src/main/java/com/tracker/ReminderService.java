package com.tracker;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ReminderService {
    private final ScheduledExecutorService scheduler;
    private final TaskManager taskManager;

    public ReminderService(TaskManager taskManager) {
        this.taskManager = taskManager;
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    public void start() {
        scheduler.scheduleAtFixedRate(this::checkDeadlines, 0, 60, TimeUnit.SECONDS);
        System.out.println("Reminder service started. Checking every 60 seconds...");
    }

    public void stop() {
        scheduler.shutdown();
        System.out.println("Reminder service stopped.");
    }

    private void checkDeadlines() {
        List<Task> upcoming = taskManager.getUpcomingTasks();
        if (!upcoming.isEmpty()) {
            System.out.println("\n*** DEADLINE REMINDER ***");
            for (Task task : upcoming) {
                System.out.println("  Task due within 1 hour: " + task.getTitle()
                        + " | Due: " + task.getDueDate()
                        + " | Priority: " + task.getPriority());
            }
            System.out.println("*************************\n");
        }
    }
}