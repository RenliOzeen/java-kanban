package ru.yandex.practicum.tracker.model;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;

public class Task {
    private String name;
    private String details;
    private Integer id;
    private TaskStatus status;
    private TaskType type;
    private Duration duration;
    private Optional<LocalDateTime> startTime;
    public static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    public static final LocalDateTime EMPTY_START_TIME_VALUE = LocalDateTime.parse("0001-01-01T00:00", formatter);

    public Task(String name, String details, long minutesToComplete, Optional<String> date) {
        this.name = name;
        this.details = details;
        status = TaskStatus.NEW;
        type = TaskType.TASK;
        duration = Duration.ofMinutes(minutesToComplete);
        if (date.isPresent()) {
            startTime = Optional.of(LocalDateTime.parse(date.get(), formatter));
        } else startTime = Optional.empty();
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

    public TaskType getType() {
        return type;
    }

    public void setType(TaskType type) {
        this.type = type;
    }

    public Duration getDuration() {
        return duration;
    }

    public Optional<LocalDateTime> getStartTime() {
        if (startTime.isEmpty()) {
            startTime = Optional.of(EMPTY_START_TIME_VALUE);
        }
        return startTime;

    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = Optional.of(startTime);
    }

    public Optional<LocalDateTime> getEndTime() {
        LocalDateTime endTime = startTime.get().plus(duration);
        return Optional.of(endTime);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(name, task.name) && Objects.equals(details, task.details) && status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, details, id, status);
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", details='" + details + '\'' +
                ", id=" + id +
                ", status=" + status +
                ", type=" + type +
                ", duration=" + duration.toMinutes() +
                ", startTime=" + startTime.get() +
                ", endTime=" + getEndTime().get() +
                '}';
    }
}

