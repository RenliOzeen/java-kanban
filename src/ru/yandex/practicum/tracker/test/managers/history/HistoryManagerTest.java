package ru.yandex.practicum.tracker.test.managers.history;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.tracker.model.Epic;
import ru.yandex.practicum.tracker.model.Task;
import ru.yandex.practicum.tracker.service.managers.Managers;
import ru.yandex.practicum.tracker.service.managers.history.HistoryManager;
import ru.yandex.practicum.tracker.service.managers.task.TaskManager;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {
    HistoryManager manager;
    TaskManager taskManager;

    @BeforeEach
    public void setup() {
        manager = Managers.getDefaultHistory();
        taskManager = Managers.getDefault();
    }

    /**
     * Тест покрывает все методы интерфейса HistoryManager(add, remove, getHistory)
     */
    @Test
    public void shouldAddTaskToHistoryAndRemoveTaskFromHistoryAndReturnHistoryList() {
        assertTrue(manager.getHistory().isEmpty(), "История просмотров не пуста");
        Task task1 = taskManager.createTask("task1", null, 0, "2022-09-01T12:15");
        Task task2 = taskManager.createTask("task2", null, 0, "2022-08-17T12:19");
        Task task3 = taskManager.createTask("task3", null, 0, "2022-08-21T12:19");
        manager.add(task1);
        manager.add(task2);
        manager.add(task3);
        assertTrue(manager.getHistory().size() == 3, "Неверное количество задач в истории");
        manager.add(task1);
        assertTrue(manager.getHistory().size() == 3, "Неверное количество задач в истории");
        assertEquals(task2, manager.getHistory().get(0), "Неверная очередность задач в истории");
        manager.remove(2);
        assertTrue(manager.getHistory().size() == 2, "Неверное количество задач в истории");
        assertEquals(task3, manager.getHistory().get(0), "Неверная очередность задач в истории");
        manager.add(task2);
        assertEquals(task2, manager.getHistory().get(2), "Неверная очередность задач в истории");
        assertEquals(task1, manager.getHistory().get(1), "Неверная очередность задач в истории");
        manager.remove(2);
        assertTrue(manager.getHistory().size() == 2, "Неверное количество задач в истории");
        assertEquals(task3, manager.getHistory().get(0), "Неверная очередность задач в истории");
        assertEquals(task1, manager.getHistory().get(1), "Неверная очередность задач в истории");
    }


}