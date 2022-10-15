package ru.yandex.practicum.tracker.service.managers.task;

import ru.yandex.practicum.tracker.model.Epic;
import ru.yandex.practicum.tracker.model.SubTask;
import ru.yandex.practicum.tracker.model.Task;
import ru.yandex.practicum.tracker.service.managers.history.HistoryManager;

import java.util.ArrayList;
import java.util.HashMap;

public interface TaskManager {

    ArrayList<Task> getTasks();


    ArrayList<SubTask> getSubTasks();

    ArrayList<Epic> getEpics();

    int createId();

    Task createTask(String name, String details);

    Epic createEpic(String name);

    SubTask createSubTask(String epicName, String subTaskName);

    void deleteTasks();

    void deleteSubTasks();

    void deleteEpics();

    Task getTaskById(int id);

    Epic getEpicById(int id);

    SubTask getSubTaskById(int id);

    void updateTask(Task task, int id);

    void updateSubTask(SubTask subTask, String epicName, int id);

    void updateEpic(Epic epic, int id);

    void deleteTaskById(int id);

    void deleteEpicById(int id);

    void deleteSubTaskById(int id);

    HashMap getListSubsOfEpic(int epicId);

    HistoryManager getHistoryManager();
}
