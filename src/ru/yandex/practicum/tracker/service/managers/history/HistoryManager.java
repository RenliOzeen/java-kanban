package ru.yandex.practicum.tracker.service.managers.history;

import ru.yandex.practicum.tracker.model.Task;

import java.util.ArrayList;

public interface HistoryManager {

    void add(Task task);

    ArrayList<Task> getHistory();

}
