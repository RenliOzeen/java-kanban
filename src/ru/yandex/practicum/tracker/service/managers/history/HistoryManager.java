package ru.yandex.practicum.tracker.service.managers.history;

import ru.yandex.practicum.tracker.model.Task;

import java.util.List;

/**
 * Интерфейс, реализуемый менеджерами истории просмотров
 */
public interface HistoryManager {

    /**
     * Метод для добавления задач/подзадач/эпиков(Task/SubTask/Epic) в историю просмотров
     *
     * @param task объект задачи класса ru.yandex.practicum.tracker.model.Task
     */
    void add(Task task);

    /**
     * Метод для удаления задачи(Task/SubTask/Epic) из истории просмотров по ее id
     *
     * @param id задачи, которую нужно удалить
     */
    void remove(int id);

    /**
     * Метод для получения истории просмотров
     *
     * @return список(List) с объектами класса ru.yandex.practicum.tracker.model.Task
     */
    List<Task> getHistory();

}
