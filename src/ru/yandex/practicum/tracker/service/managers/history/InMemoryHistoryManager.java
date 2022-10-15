package ru.yandex.practicum.tracker.service.managers.history;

import ru.yandex.practicum.tracker.model.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    private final ArrayList<Task> viewsHistory;

    public InMemoryHistoryManager() {
        viewsHistory = new ArrayList<>();
    }

    @Override
    public ArrayList<Task> getHistory() {
        return viewsHistory;
    }

    @Override
    public void add(Task task) {
        viewsHistory.add(task);
    }
}
