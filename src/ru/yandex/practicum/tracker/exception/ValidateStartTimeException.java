package ru.yandex.practicum.tracker.exception;

/**
 * Исключение ошибки валидации времени начала выполнение и продолжительности новой задачи
 */
public class ValidateStartTimeException extends RuntimeException {

    public ValidateStartTimeException(final String message) {
        super(message);
    }
}
