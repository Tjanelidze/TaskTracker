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


        if (command.equals("add")) {
            if (args.length < 2) {
                System.out.println("Usage: task-cli add <description>");
                return;
            }

            String description = args[1];
            taskService.add(description);
        } else if (command.equals("list")) {
            taskService.listTask();
        } else {
            System.out.println("Unknown command: " + command);
        }
    }
}