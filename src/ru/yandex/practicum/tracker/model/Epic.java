package ru.yandex.practicum.tracker.model;

import java.util.ArrayList;
import java.util.List;


public class Epic extends Task {
    private final List<Integer> subTasks;

    public Epic(String name, String details) {
        super(name, details);
        subTasks = new ArrayList<>();
        setType(TaskType.EPIC);
    }

    public List<Integer> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(int id) {
        this.subTasks.add(id);
    }

}
