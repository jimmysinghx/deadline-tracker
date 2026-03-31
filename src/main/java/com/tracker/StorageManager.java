package com.tracker;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class StorageManager {
    private static final String DB_URL = "jdbc:sqlite:tasks.db";
    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public StorageManager() {
        createTableIfNotExists();
    }

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    private void createTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS tasks (" +
                "id TEXT PRIMARY KEY," +
                "title TEXT NOT NULL," +
                "dueDate TEXT NOT NULL," +
                "priority TEXT NOT NULL," +
                "status TEXT NOT NULL," +
                "createdAt TEXT NOT NULL)";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Error creating table: " + e.getMessage());
        }
    }

    public List<Task> loadTasks() {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Task task = new Task(
                        rs.getString("id"),
                        rs.getString("title"),
                        LocalDateTime.parse(rs.getString("dueDate"), formatter),
                        Priority.valueOf(rs.getString("priority"))
                );
                task.setStatus(Status.valueOf(rs.getString("status")));
                tasks.add(task);
            }
        } catch (SQLException e) {
            System.out.println("Error loading tasks: " + e.getMessage());
        }
        return tasks;
    }

    public void saveTask(Task task) {
        String sql = "INSERT OR REPLACE INTO tasks " +
                "(id, title, dueDate, priority, status, createdAt) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, task.getId());
            pstmt.setString(2, task.getTitle());
            pstmt.setString(3, task.getDueDate().format(formatter));
            pstmt.setString(4, task.getPriority().name());
            pstmt.setString(5, task.getStatus().name());
            pstmt.setString(6, task.getCreatedAt().format(formatter));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error saving task: " + e.getMessage());
        }
    }

    public void updateTask(Task task) {
        String sql = "UPDATE tasks SET title=?, dueDate=?, priority=?, status=? WHERE id=?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, task.getTitle());
            pstmt.setString(2, task.getDueDate().format(formatter));
            pstmt.setString(3, task.getPriority().name());
            pstmt.setString(4, task.getStatus().name());
            pstmt.setString(5, task.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating task: " + e.getMessage());
        }
    }

    public void deleteTask(String id) {
        String sql = "DELETE FROM tasks WHERE id=?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting task: " + e.getMessage());
        }
    }
}