package ru.yandex.practicum.tracker.model;

public class SubTask extends Task {
    private int epicId;

    public SubTask(String name, String details) {
        super(name, details);
        setType(TaskType.SUBTASK);

    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

}
