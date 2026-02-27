package com.tasktracker.service;

import com.tasktracker.model.Task;
import com.tasktracker.model.TaskStatus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

public class TaskService {
    private final Path filePath = Path.of("tasks.json");

    public void add(String description) {
        try {
            Task newTask = new Task(UUID.randomUUID(), description, TaskStatus.TODO);
            String newTaskJson = formatTaskToJson(newTask);

            if (!Files.exists(filePath) || Files.size(filePath) == 0) {
                Files.writeString(filePath, "[\n" + newTaskJson + "\n]");
                System.out.println("Task added successfully (ID: " + newTask.getId() + ")");
                return;
            }

            String content = Files.readString(filePath).trim();
            int lastBracket = content.lastIndexOf("]");
            if (lastBracket == -1) {
                Files.writeString(filePath, "[\n" + newTaskJson + "\n]");
                return;
            }

            String baseContent = content.substring(0, content.lastIndexOf("]")).trim();
            if (baseContent.endsWith(",")) {
                baseContent = baseContent.substring(0, baseContent.length() - 1);
            }

            String updatedContent = baseContent + ",\n" + newTaskJson + "\n]";
            Files.writeString(filePath, updatedContent);

            System.out.println("Task added successfully (ID: " + newTask.getId() + ")");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void listTask() {
        try {
            String content = Files.readString(filePath);

            System.out.println(content);
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private String formatTaskToJson(Task task) {
        return "  {\n" +
                "    \"id\": \"" + task.getId() + "\",\n" +
                "    \"description\": \"" + escapeJson(task.getDescription()) + "\",\n" +
                "    \"status\": \"" + task.getStatus() + "\",\n" +
                "    \"createdAt\": \"" + task.getCreatedAt() + "\",\n" +
                "    \"updatedAt\": \"" + task.getUpdatedAt() + "\"\n" +
                "  }";
    }

    private static String escapeJson(String value) {
        if (value == null) return "";
        return value.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\b", "\\b")
                .replace("\f", "\\f")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
