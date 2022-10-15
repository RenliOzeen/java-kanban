package ru.yandex.practicum.tracker.model;

import java.util.HashMap;


public class Epic extends Task {
    private final HashMap<Integer, SubTask> subTasks;

    public Epic(String name, String details) {
        super(name, details);
        subTasks = new HashMap<>();
    }

    public HashMap<Integer, SubTask> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(int id, SubTask subTask) {
        this.subTasks.put(id, subTask);
    }
}
