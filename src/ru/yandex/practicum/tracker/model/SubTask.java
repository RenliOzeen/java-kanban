package ru.yandex.practicum.tracker.model;

import java.util.Optional;

public class SubTask extends Task {
    private int epicId;
    private String epicName;

    public SubTask(String name, String details, long minutesToComplete, Optional<String> date) {
        super(name, details, minutesToComplete, date);
        setType(TaskType.SUBTASK);
        epicId = 0;
    }

    public String getEpicName(){
        return epicName;
    }

    public void setEpicName(String epicName){
        this.epicName=epicName;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

}
