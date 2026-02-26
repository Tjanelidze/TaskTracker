package com.tasktracker;

import com.tasktracker.service.TaskService;

public class Main {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: task-cli <command> <description>");
            return;
        }

        String command = args[0];
        String description = args[1];
        TaskService task = new TaskService();


        if (command.equals("add")) {
            task.add(description);
        } else {
            System.out.println("Unknown command: " + command);
        }
    }
}