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
            if (lastBracket == -1 || lastBracket != content.length() - 1) {
                System.out.println("An error occurred: tasks.json is malformed.");
                return;
            }

            String baseContent = content.substring(0, lastBracket).trim();
            if (baseContent.isEmpty()) {
                System.out.println("An error occurred: tasks.json is malformed.");
                return;
            }


            if (!baseContent.equals("[")) {
                int closingBracket = baseContent.lastIndexOf('}');
                boolean duplicateBracket = baseContent.trim().endsWith("}}");

                if (closingBracket == -1 || duplicateBracket) {
                    System.out.println("An error occurred: tasks.json is malformed.");
                    return;
                }
            }
            if (baseContent.endsWith(",")) {
                baseContent = baseContent.substring(0, baseContent.length() - 1);
            }

            String separator = baseContent.equals("[") ? "\n" : ",\n";
            String updatedContent = baseContent + separator + newTaskJson + "\n]";
            Files.writeString(filePath, updatedContent);

            System.out.println("Task added successfully (ID: " + newTask.getId() + ")");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void listTask() {
        try {
            if (!Files.exists(filePath) || Files.size(filePath) == 0) {
                System.out.println("[]");
                return;
            }

            String content = Files.readString(filePath);


            System.out.println(content);
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void update(String description, String taskId) {
        try {
            if (!Files.exists(filePath) || Files.size(filePath) == 0) {
                System.out.println("There are no Tasks to update!");
                return;
            }

            String content = Files.readString(filePath).trim();

            String idPattern = "\"id\": \"" + taskId + "\"";
            int idIndex = content.indexOf(idPattern);
            if (idIndex == -1) {
                System.out.println("Task ID not found.");
                return;
            }

            String finalFileContent = updateTask(description, idIndex, content);

            Files.writeString(filePath, finalFileContent);
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void delete(String taskId) {
        try {
            if (!Files.exists(filePath) || Files.size(filePath) == 0) {
                System.out.println("There are no Tasks to delete!");
                return;
            }

            String content = Files.readString(filePath).trim();
            String idPattern = "\"id\": \"" + taskId + "\"";
            int idIndex = content.indexOf(idPattern);
            if (idIndex == -1) {
                System.out.println("Task ID not found.");
                return;
            }

            String finalFileContent = deleteTask(idIndex, content);

            Files.writeString(filePath, finalFileContent);
            System.out.println("Task deleted successfully!");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private static String updateTask(String description, int idIndex, String content) {
        int start = content.lastIndexOf('{', idIndex);
        int end = content.indexOf('}', idIndex);

        if (start == -1 || end == -1 || start >= end) {
            return content;
        }

        String targetObject = content.substring(start, end + 1);
        String key = "\"description\": \"";

        int keyIndex = targetObject.indexOf(key);
        if (keyIndex == -1) return content;

        int startOfValue = keyIndex + key.length();
        int endOfValue = findJsonStringEnd(targetObject, startOfValue);

        if (endOfValue == -1) return content;

        String updatedObject = targetObject.substring(0, startOfValue)
                + escapeJson(description)
                + targetObject.substring(endOfValue);

        return content.substring(0, start)
                + updatedObject
                + content.substring(end + 1);
    }

    private static String deleteTask(int idIndex, String content) {
        int start = content.lastIndexOf('{', idIndex);
        int end = content.indexOf('}', idIndex);

        if (start == -1 || end == -1 || start >= end) {
            return content;
        }

        int removeStart = start;
        int removeEnd = end + 1;

        while (removeEnd < content.length() && Character.isWhitespace(content.charAt(removeEnd))) {
            removeEnd++;
        }
        if (removeEnd < content.length() && content.charAt(removeEnd) == ',') {
            return content.substring(0, start) + content.substring(removeEnd + 1);
        }

        while (removeStart > 0 && Character.isWhitespace(content.charAt(removeStart - 1))) {
            removeStart--;
        }
        if (removeStart > 0 && content.charAt(removeStart - 1) == ',') {
            removeStart--;
        }
        return content.substring(0, removeStart) + content.substring(end + 1);
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

    private static int findJsonStringEnd(String s, int from) {
        boolean escaped = false;
        for (int i = from; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '"' && !escaped) return i;
            escaped = c == '\\' && !escaped;
        }
        return -1;
    }
}
