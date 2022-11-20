package ru.yandex.practicum.tracker.service.managers.task.fileBacked;

/**
 * Исключение сохранения данных задач/подзадач/эпиков и истории просмотров в файл
 */
public class ManagerSaveException extends RuntimeException{
    public ManagerSaveException(final String message){
        super(message);
    }
}
