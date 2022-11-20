package ru.yandex.practicum.tracker.service.managers.task.fileBacked;

/**
 * Исключение восстановления менеджера из файла
 */
public class ManagerLoadException extends RuntimeException{
    public ManagerLoadException(final String message){
        super(message);
    }
}
