package com.tasktracker.service;

import com.tasktracker.model.Task;
import com.tasktracker.model.TaskStatus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

public class TaskService {


    public void add(String description) {
        Path filePath = Path.of("tasks.json");

        try {
            if (!Files.exists(filePath) || Files.size(filePath) == 0) {
                Files.writeString(filePath, "[]");
            }

            Task newTask = new Task(UUID.randomUUID(), description, TaskStatus.TODO);
            String content = Files.readString(filePath);
            String updatedContent = getString(content, newTask);

            Files.writeString(filePath, updatedContent);

            System.out.println("Task added successfully (ID: " + newTask.getId() + ")");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private static String getString(String content, Task newTask) {
        int lastBracketIndex = content.lastIndexOf(']');
        String beginning = content.substring(0, lastBracketIndex);

        String comma = beginning.trim().endsWith("[") ? "" : ",";

        String newTaskJson = String.format(
                "{\"id\":\"%s\",\"description\":\"%s\",\"status\":\"%s\",\"createdAt\":\"%s\",\"updatedAt\":\"%s\"}",
                newTask.getId().toString(),
                escapeJson(newTask.getDescription()),
                newTask.getStatus(),
                newTask.getCreatedAt(),
                newTask.getUpdatedAt()
        );

        return beginning + comma + newTaskJson + "]";
    }


    private static String escapeJson(String value) {
        if (value == null) return "";
        return value.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
