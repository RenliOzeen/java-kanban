package ru.yandex.practicum.tracker.server.http;

/**
 * Вспомогательный enum с эндпоинтами
 */
public enum Endpoint {
    GET_TASKS,
    GET_SUBTASKS,
    GET_EPICS,
    GET_ALL,
    GET_TASK_BY_ID,
    GET_SUBTASK_BY_ID,
    GET_EPIC_BY_ID,
    CREATE_TASK,
    CREATE_SUBTASK,
    CREATE_EPIC,
    DELETE_TASKS,
    DELETE_EPICS,
    DELETE_SUBTASKS,
    UPDATE_TASK,
    UPDATE_SUBTASK,
    UPDATE_EPIC,
    DELETE_TASK_BY_ID,
    DELETE_SUBTASK_BY_ID,
    DELETE_EPIC_BY_ID,
    GET_EPIC_SUBS,
    GET_HISTORY,
    GET_PRIORITY,
    UNKNOWN
}
