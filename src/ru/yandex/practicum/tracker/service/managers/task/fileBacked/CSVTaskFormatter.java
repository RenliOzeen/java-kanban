package ru.yandex.practicum.tracker.service.managers.task.fileBacked;

import ru.yandex.practicum.tracker.model.*;
import ru.yandex.practicum.tracker.service.managers.history.HistoryManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class CSVTaskFormatter {

    /**
     * Метод для перевода объекта задачи(Task) в строку
     *
     * @param task объект типа Task
     * @return строковое представление задачи
     */
    public static String taskToString(Task task) {
        if (task.getType().equals(TaskType.SUBTASK)) {
            return task.getId() + "," +
                    task.getType() + "," +
                    task.getName() + "," +
                    task.getStatus() + "," +
                    task.getDetails() + "," +
                    ((SubTask) task).getEpicId() + "," +
                    task.getStartTime().get() + "," +
                    task.getEndTime().get() + "," +
                    task.getDuration().toMinutes();
        } else {
            return task.getId() + "," +
                    task.getType() + "," +
                    task.getName() + "," +
                    task.getStatus() + "," +
                    task.getDetails() + "," +
                    task.getStartTime().get() + "," +
                    task.getEndTime().get() + "," +
                    task.getDuration().toMinutes();
        }
    }

    /**
     * Метод для обратного преобразования строки в объект задачи(Task)
     *
     * @param task задача(Task) в формате строки
     * @return объект типа Task
     */
    public static Task taskFromString(String task) {
        String[] taskParts = task.split(",");
        if (task.contains("SUBTASK")) {
            SubTask typeOfTask = new SubTask(taskParts[2], taskParts[4],
            Long.parseLong(taskParts[8]), Optional.of(taskParts[6]));
            typeOfTask.setId(Integer.parseInt(taskParts[0]));
            typeOfTask.setName(taskParts[2]);
            typeOfTask.setStatus(TaskStatus.valueOf(taskParts[3]));
            typeOfTask.setDetails(taskParts[4]);
            typeOfTask.setEpicId(Integer.parseInt(taskParts[5]));
            return typeOfTask;
        } else if (task.contains("EPIC")) {
            Epic typeOfTask = new Epic(taskParts[2], taskParts[4],
                    Long.parseLong(taskParts[7]), Optional.of(taskParts[5]));
            typeOfTask.setId(Integer.parseInt(taskParts[0]));
            typeOfTask.setStatus(TaskStatus.valueOf(taskParts[3]));
            typeOfTask.setEndTime(LocalDateTime.parse(taskParts[6], Task.formatter));
            return typeOfTask;
        } else {
            Task typeOfTask = new Task(taskParts[2], taskParts[4],
                    Long.parseLong(taskParts[7]), Optional.of(taskParts[5]));
            typeOfTask.setId(Integer.parseInt(taskParts[0]));
            typeOfTask.setStatus(TaskStatus.valueOf(taskParts[3]));
            return typeOfTask;
        }
    }

    /**
     * Метод для преобразования истории просмотров в строку
     *
     * @param manager экземпляр менеджера истории просмотров
     * @return строковое представление(String) истории просмотров
     */
    public static String historyToString(HistoryManager manager) {
        StringBuilder historyRow = new StringBuilder();
        for (Task task : manager.getHistory()) {
            if (task == manager.getHistory().get(manager.getHistory().size() - 1)) {
                historyRow.append(task.getId());
            } else {
                historyRow.append(task.getId() + ",");
            }
        }
        return historyRow.toString();
    }

    /**
     * Метод для обратного преобразования строки в историю просмотров
     *
     * @param value строковое представление истории просмотров
     * @return List истории просмотров
     */
    public static List<Integer> historyFromString(String value) {
        String[] historyRows = value.split(",");
        Integer[] historyId = new Integer[historyRows.length];
        for (int i = 0; i < historyRows.length; i++) {
            historyId[i] = Integer.parseInt(historyRows[i]);
        }
        return List.of(historyId);
    }
}
