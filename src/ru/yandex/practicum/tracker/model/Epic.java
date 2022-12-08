package ru.yandex.practicum.tracker.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class Epic extends Task {
    private final List<Integer> subTasks;
    private Optional<LocalDateTime> endTime;
    public Epic(String name, String details) {
        super(name, details, 0, Optional.empty());
        subTasks = new ArrayList<>();
        setType(TaskType.EPIC);
        endTime=Optional.empty();
    }

    public Epic(String name,String details, Long minutesToComplete,
                Optional<String> date){
        super(name,details,minutesToComplete,date);
        subTasks = new ArrayList<>();
        setType(TaskType.EPIC);
    }

    public List<Integer> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(int id) {
        this.subTasks.add(id);
    }

    @Override
    public Optional<LocalDateTime> getEndTime() {
        if(endTime.isPresent()){
            return endTime;
        } else{
            return Optional.of(EMPTY_START_TIME_VALUE);
        }

    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = Optional.of(endTime);
    }
}
