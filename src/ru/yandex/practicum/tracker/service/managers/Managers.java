package ru.yandex.practicum.tracker.service.managers;

import ru.yandex.practicum.tracker.model.Task;
import ru.yandex.practicum.tracker.service.managers.history.HistoryManager;
import ru.yandex.practicum.tracker.service.managers.history.InMemoryHistoryManager;
import ru.yandex.practicum.tracker.service.managers.task.fileBacked.FileBackedTasksManager;
import ru.yandex.practicum.tracker.service.managers.task.http.HttpTaskManager;
import ru.yandex.practicum.tracker.service.managers.task.inMemory.InMemoryTaskManager;
import ru.yandex.practicum.tracker.service.managers.task.TaskManager;

public class Managers {

    public static TaskManager getDefault() {
        TaskManager taskManager = new HttpTaskManager("http://localhost:8078");
        return taskManager;
    }

    public static TaskManager getDefaultFileBacked() {
        TaskManager taskManager = new FileBackedTasksManager();
        return taskManager;
    }

    public static HistoryManager getDefaultHistory() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        return historyManager;
    }
}
