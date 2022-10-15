package ru.yandex.practicum.tracker.model;

import ru.yandex.practicum.tracker.service.TaskStatus;

public class Task {
    private String name;
    private String details;
    private int id;
    private TaskStatus status;

    public Task(String name, String details) {
        this.name = name;
        this.details = details;
        status = TaskStatus.NEW;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ru.yandex.practicum.tracker.model.Task{" +
                "name='" + name + '\'' +
                ", details='" + details + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                '}';
    }
}
