package ru.yandex.practicum.tracker.service.managers.history;

import ru.yandex.practicum.tracker.model.Task;

/**
 * Служебный класс узла двусвязного списка для historyManager
 */
public class Node {
    Task data;
    Node next;
    Node prev;

    public Node(Task data) {
        this.data = data;
        this.next = null;
        this.prev = null;
    }

}
