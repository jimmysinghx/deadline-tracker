package com.tracker;

import java.time.LocalDateTime;

public class Task {
    private String id;
    private String title;
    private LocalDateTime dueDate;
    private Priority priority;
    private Status status;
    private LocalDateTime createdAt;

    public Task(String id, String title, LocalDateTime dueDate, Priority priority) {
        this.id = id;
        this.title = title;
        this.dueDate = dueDate;
        this.priority = priority;
        this.status = Status.TODO;
        this.createdAt = LocalDateTime.now();
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public LocalDateTime getDueDate() { return dueDate; }
    public Priority getPriority() { return priority; }
    public Status getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setStatus(Status status) { this.status = status; }
    public void setTitle(String title) { this.title = title; }
    public void setPriority(Priority priority) { this.priority = priority; }

    @Override
    public String toString() {
        return String.format("[%s] %-30s | Due: %s | Priority: %-6s | Status: %s",
                id, title,
                dueDate.toString(),
                priority,
                status);
    }
}