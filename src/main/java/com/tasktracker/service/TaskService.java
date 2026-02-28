package com.tasktracker.service;

import com.tasktracker.model.Task;
import com.tasktracker.model.TaskStatus;
import com.tasktracker.repository.TaskRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class TaskService {
    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public void add(String description) {
        List<Task> tasks = new ArrayList<>(repository.getAll());

        Task newTask = new Task(UUID.randomUUID(), description, TaskStatus.TODO);
        tasks.add(newTask);

        repository.saveAll(tasks);

        System.out.println("Task added successfully (ID: " + newTask.getId() + ")");
    }

    public void listTask() {
        List<Task> tasks = repository.getAll();

        if (tasks.isEmpty()) {
            System.out.println("No tasks found.");
            return;
        }

        for (Task task : tasks) {
            System.out.printf("[%s] %s - %s (Created: %s)%n",
                    task.getId().toString().substring(0, 8),
                    task.getStatus(),
                    task.getDescription(),
                    task.getCreatedAt());
        }
    }

    public void updateTask(String taskId, Consumer<Task> mutator, String successMessage) {
        List<Task> tasks = new ArrayList<>(repository.getAll());
        boolean found = false;

        for (Task task : tasks) {
            if (taskId.equals(task.getId().toString())) {
                mutator.accept(task);
                task.setUpdatedAt(LocalDateTime.now());
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("Task ID not found.");
        } else {
            repository.saveAll(tasks);
            System.out.println(successMessage);
        }
    }


    public void updateStatus(String taskId, TaskStatus status) {
        updateTask(taskId, task -> task.setStatus(status), "Task status has been updated");
    }

    public void delete(String taskId) {
        List<Task> tasks = new ArrayList<>(repository.getAll());

        boolean removed = tasks.removeIf(task -> task.getId().toString().equals(taskId));

        if (!removed) {
            System.out.println("Task ID not found.");
        } else {
            repository.saveAll(tasks);
            System.out.println("Task has been deleted.");
        }
    }

}
