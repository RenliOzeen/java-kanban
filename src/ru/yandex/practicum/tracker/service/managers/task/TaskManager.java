package ru.yandex.practicum.tracker.service.managers.task;

import ru.yandex.practicum.tracker.model.Epic;
import ru.yandex.practicum.tracker.model.SubTask;
import ru.yandex.practicum.tracker.model.Task;


import java.util.ArrayList;
import java.util.List;

/**
 * Интерфейс, реализуемый менеджерами задач
 */
public interface TaskManager {

    /**
     * Метод для получения списка всех задач
     * @return List с объектами класса ru.yandex.practicum.tracker.model.Task
     */
    ArrayList<Task> getTasks();

    /**
     * Метод для получения списка всех подзадач
     * @return List с объектами класса ru.yandex.practicum.tracker.model.SubTask
     */
    ArrayList<SubTask> getSubTasks();

    /**
     * Метод для получения списка всех эпиков
     * @return List с объектами класса ru.yandex.practicum.tracker.model.Epic
     */
    ArrayList<Epic> getEpics();

    /**
     * Метод для создания новых задач
     * @param name - Название задачи
     * @param details - Детали задачи(более подробное описание)
     * @return объект класса ru.yandex.practicum.tracker.model.Task
     */
    Task createTask(String name, String details);

    /**
     * Метод для создания новых эпиков
     * @param name - Название эпика
     * @return объект класса ru.yandex.practicum.tracker.model.Epic
     */
    Epic createEpic(String name);

    /**
     * Метод для создания новых подзадач
     * @param epicName - Название эпика, внутри которого будет создана подзадача
     * @param subTaskName - Название самой подзадачи
     * @return объект класса ru.yandex.practicum.tracker.model.SubTask
     */
    SubTask createSubTask(String epicName, String subTaskName);

    /**
     * Метод для удаления всех задач(Task)
     */
    void deleteTasks();

    /**
     * Метод для удаления всех подзадач(SubTask)
     */
    void deleteSubTasks();

    /**
     * Метод для удаления всех эпиков(Epic)
     */
    void deleteEpics();

    /**
     * Метод для получения конкретной задачи(Task) по ее id
     * @param id задачи
     * @return объект класса ru.yandex.practicum.tracker.model.Task
     */
    Task getTaskById(int id);

    /**
     * Метод для получения конкретного эпика(Epic) по его id
     * @param id эпика
     * @return объект класса ru.yandex.practicum.tracker.model.Epic
     */
    Epic getEpicById(int id);

    /**
     * Метод для получения конкретной подзадачи(SubTask) по ее id
     * @param id подзадачи
     * @return объект класса ru.yandex.practicum.tracker.model.SubTask
     */
    SubTask getSubTaskById(int id);

    /**
     * Метод для обновления задач(Task), по сути заменяет имеющийся объект Task новым
     * @param task - объект задачи, которую нужно обновить
     * @param id , присваеваемый задаче
     */
    void updateTask(Task task, int id);

    /**
     * Метод для обновления подзадач(SubTask), по сути заменяет имеющийся объект SubTask новым
     * @param subTask - объект подзадачи, которую нужно обновить
     * @param epicName - название эпика, в рамках которого выполняется данная задача
     * @param id , присваеваемый подзадаче
     */
    void updateSubTask(SubTask subTask, String epicName, int id);

    /**
     * Метод для обновления эпиков(Epic), по сути заменяет имеющийся объект Epic новым
     * @param epic - объект эпика, который нужно обновить
     * @param id , присваеваемый эпику
     */
    void updateEpic(Epic epic, int id);

    /**
     * Метод для удаления задачи(Task) по ее id
     * @param id задачи
     */
    void deleteTaskById(int id);

    /**
     * Метод для удаления эпика(Epic) по его id
     * @param id эпика
     */
    void deleteEpicById(int id);

    /**
     * Метод для удаления подзадачи(SubTask) по ее id
     * @param id подзадачи
     */
    void deleteSubTaskById(int id);

    /**
     * Метод для получения списка подзадач(SubTask) конкретного эпика(Epic)
     * @param epicId - id эпика
     * @return List с объектами класса ru.yandex.practicum.tracker.model.SubTask
     */
    List<SubTask> getListSubsOfEpic(int epicId);

    /**
     * Метод для получения списка истории просмотров
     * @return List с объектами класса ru.yandex.practicum.tracker.model.Task
     */
    List<Task> getHistoryManager();
}
