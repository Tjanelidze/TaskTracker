package com.tasktracker;

import com.tasktracker.service.TaskService;

public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: task-cli <command> [arguments]");
            return;
        }

        String command = args[0];
        TaskService taskService = new TaskService();


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
                taskService.listTask();
                break;

            case "update":
                if (args.length < 3) {
                    System.out.println("Usage: task-cli update <task-ID> <description>");
                    return;
                }
                String taskId = args[1];
                String updateDesc = args[2];
                taskService.update(updateDesc, taskId);
                break;
            default:
                System.out.println("Unknown command: " + command);
        }
    }
}