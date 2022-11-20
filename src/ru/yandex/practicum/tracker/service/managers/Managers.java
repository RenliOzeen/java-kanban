package ru.yandex.practicum.tracker.service.managers;

import ru.yandex.practicum.tracker.service.managers.history.HistoryManager;
import ru.yandex.practicum.tracker.service.managers.history.InMemoryHistoryManager;
import ru.yandex.practicum.tracker.service.managers.task.inMemory.InMemoryTaskManager;
import ru.yandex.practicum.tracker.service.managers.task.TaskManager;

public class Managers {

    public static TaskManager getDefault() {
        TaskManager taskManager = new InMemoryTaskManager();
        return taskManager;
    }

    public static HistoryManager getDefaultHistory() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        return historyManager;
    }
}
