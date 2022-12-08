package ru.yandex.practicum.tracker.test.managers.task.fileBacked;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.tracker.model.Epic;
import ru.yandex.practicum.tracker.model.SubTask;
import ru.yandex.practicum.tracker.model.Task;
import ru.yandex.practicum.tracker.service.managers.task.TaskManager;
import ru.yandex.practicum.tracker.service.managers.task.fileBacked.FileBackedTasksManager;
import ru.yandex.practicum.tracker.test.managers.task.TaskManagerTest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    FileBackedTasksManager taskManager = new FileBackedTasksManager();

    @Override
    public TaskManager getType() {
        manager = new FileBackedTasksManager();
        return manager;
    }

    public int fileReader(File file) {
        String row;
        int rowCount = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(taskManager.getBackupFile()))) {
            while (reader.ready()) {
                row = reader.readLine();
                if (!row.isEmpty()) {
                    rowCount++;
                }
            }
        } catch (IOException e) {
            return -1;
        }
        return rowCount;
    }

    /**
     * Тест покрывает весь собственный функционал FileBackedTaskManager'а
     * сохранение и восстановление из файла при различных граничных условиях
     */
    @Test
    public void shouldSaveConditionAndLoadFromFile() {
        taskManager.save();
        assertTrue(fileReader(taskManager.getBackupFile()) == 1, "В файле бэкапа сохранено что-то кроме полей");
        assertTrue(FileBackedTasksManager.loadFromFile(taskManager.getBackupFile()).getTasks().isEmpty());
        assertTrue(FileBackedTasksManager.loadFromFile(taskManager.getBackupFile()).getSubTasks().isEmpty());
        assertTrue(FileBackedTasksManager.loadFromFile(taskManager.getBackupFile()).getEpics().isEmpty());
        assertTrue(FileBackedTasksManager.loadFromFile(taskManager.getBackupFile()).getHistory().isEmpty());
        Task task1 = taskManager.createTask("task1", null, 0, "2022-09-01T12:15");
        Task task2 = taskManager.createTask("task2", null, 0, "2022-08-17T12:19");
        assertTrue(fileReader(taskManager.getBackupFile()) == 3, "в файле бэкапа сохранено неверное количество задач");
        assertFalse(FileBackedTasksManager.loadFromFile(taskManager.getBackupFile()).getTasks().isEmpty());
        Epic epic = taskManager.createEpic("Получить права");
        assertTrue(fileReader(taskManager.getBackupFile()) == 4, "в файле бэкапа сохранено неверное количество задач");
        assertFalse(FileBackedTasksManager.loadFromFile(taskManager.getBackupFile()).getEpics().isEmpty());
        assertTrue(FileBackedTasksManager.loadFromFile(taskManager.getBackupFile()).getEpicById(3).getSubTasks().isEmpty());
        assertTrue(FileBackedTasksManager.loadFromFile(taskManager.getBackupFile()).getSubTasks().isEmpty());
        SubTask sub1 = taskManager.createSubTask("Получить права", "Пройти обучение в автошколе", 1300, "2022-09-02T12:15");
        SubTask sub2 = taskManager.createSubTask("Получить права", "сдать экзамен", 180, "2022-09-04T12:15");
        SubTask sub3 = taskManager.createSubTask("Получить права", "отпраздновать", 120, "2022-09-11T12:15");
        assertTrue(fileReader(taskManager.getBackupFile()) == 7, "в файле бэкапа сохранено неверное количество задач");
        assertFalse(FileBackedTasksManager.loadFromFile(taskManager.getBackupFile()).getEpics().isEmpty());
        assertTrue(FileBackedTasksManager.loadFromFile(taskManager.getBackupFile()).getEpicById(3).getSubTasks().size() == 3);
        assertFalse(FileBackedTasksManager.loadFromFile(taskManager.getBackupFile()).getSubTasks().isEmpty());
        assertTrue(FileBackedTasksManager.loadFromFile(taskManager.getBackupFile()).getSubTaskById(4).getEpicId() == 3);
        taskManager.getTaskById(2);
        taskManager.getTaskById(1);
        taskManager.getEpicById(3);
        taskManager.getSubTaskById(5);
        taskManager.getSubTaskById(4);
        taskManager.getSubTaskById(6);
        assertFalse(FileBackedTasksManager.loadFromFile(taskManager.getBackupFile()).getHistory().isEmpty());
    }

}