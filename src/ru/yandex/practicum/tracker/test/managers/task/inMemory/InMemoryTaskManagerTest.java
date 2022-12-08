package ru.yandex.practicum.tracker.test.managers.task.inMemory;

import ru.yandex.practicum.tracker.service.managers.task.TaskManager;
import ru.yandex.practicum.tracker.service.managers.task.inMemory.InMemoryTaskManager;
import ru.yandex.practicum.tracker.test.managers.task.TaskManagerTest;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {


    @Override
    public TaskManager getType() {
        manager = new InMemoryTaskManager();
        return manager;
    }
}