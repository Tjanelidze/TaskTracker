package com.tasktracker;

import com.tasktracker.model.TaskStatus;
import com.tasktracker.repository.TaskRepository;
import com.tasktracker.service.TaskService;

public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: task-cli <command> [arguments]");
            return;
        }

        String command = args[0];
        TaskRepository repository = new TaskRepository();
        TaskService taskService = new TaskService(repository);


        switch (command) {
            case "add":
                if (args.length < 2) {
                    System.out.println("Usage: task-cli add <description>");
                    return;
                }
                String description = args[1];
                taskService.add(description);
                break;

            case "list":
                if (args.length > 1) {
                    TaskStatus filter = parseStatus(args[1]);
                    if (filter != null) taskService.listTasksByStatus(filter);
                } else {
                    taskService.listTask();
                }
                break;

            case "update":
                if (args.length < 3) {
                    System.out.println("Usage: task-cli update <task-ID> <description>");
                    return;
                }
                String taskId = args[1];
                String updateDesc = args[2];
                String successMessage = "Task status has been updated";
                taskService.updateTask(taskId, task -> task.setDescription(updateDesc), successMessage);
                break;

            case "delete":
                if (args.length < 2) {
                    System.out.println("Usage: task-cli delete <task-ID>");
                    return;
                }
                String deleteTaskId = args[1];
                taskService.delete(deleteTaskId);
                break;

            case "mark-in-progress":
            case "mark-done":
            case "mark-todo":
                if (args.length < 2) {
                    System.out.println("Usage: task-cli " + command + " <task-ID>");
                    return;
                }

                String statusStr = command.replace("mark-", "").replace("-", "_").toUpperCase();
                TaskStatus status = TaskStatus.valueOf(statusStr);

                taskService.updateStatus(args[1], status);
                break;
            default:
                System.out.println("Unknown command: " + command);
                printUsage();
        }
    }

    private static TaskStatus parseStatus(String input) {
        try {
            String normalized = input.replace("-", "_").toUpperCase();

            return TaskStatus.valueOf(normalized);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid status: '" + input + "'");
            System.out.println("Valid options: todo, in-progress, done");
            return null;
        }
    }

    private static void printUsage() {
        System.out.println("\n--- Task Tracker CLI Usage ---");
        System.out.println("list                      : List all tasks");
        System.out.println("list <status>             : List tasks by status (todo, in-progress, done)");
        System.out.println("add <description>         : Add a new task");
        System.out.println("update <id> <description> : Update a task description");
        System.out.println("delete <id>               : Delete a task");
        System.out.println("mark-<status> <id>        : Mark task as todo, in-progress, or done");
        System.out.println("------------------------------\n");
    }
}