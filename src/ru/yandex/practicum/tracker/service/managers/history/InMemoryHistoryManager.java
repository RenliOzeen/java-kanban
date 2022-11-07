package ru.yandex.practicum.tracker.service.managers.history;

import ru.yandex.practicum.tracker.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class InMemoryHistoryManager implements HistoryManager {

    private final HashMap<Integer, Node> hashTable;
    private Node tail;
    private Node head;
    private int size = 0;

    public InMemoryHistoryManager() {
        hashTable = new HashMap<>();
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void add(Task task) {
        if (task != null) {
            if (hashTable.containsKey(task.getId())) {
                removeNode(hashTable.get(task.getId()));
                linkLast(task);
            } else {
                linkLast(task);
            }
        }


    }

    @Override
    public void remove(int id) {
        if (hashTable.containsKey(id)) {
            removeNode(hashTable.get(id));
        }
    }

    /**
     * Метод добавления узла в конец связного списка
     *
     * @param data объект типа Task
     */
    private void linkLast(Task data) {
        final Node oldTail = tail;
        final Node newTail = new Node(data);
        tail = newTail;

        if (oldTail == null) {
            head = newTail;
        } else {
            oldTail.next = newTail;
            tail.prev = oldTail;
        }

        size++;
        hashTable.put(data.getId(), tail);
    }

    /**
     * Метод формирования List из CustomLinkedList
     *
     * @return List<Task>
     */
    private List<Task> getTasks() {
        List<Task> historyList = new ArrayList<>();
        Node node = head;
        while (node != null) {
            historyList.add(node.data);
            node = node.next;
        }
        return historyList;
    }

    /**
     * Метод вырезания узла связного списка с логикой переноса ссылок соседних элементов
     *
     * @param node узел типа Node, который необходимо вырезать
     */
    private void removeNode(Node node) {
        if (hashTable.get(node.data.getId()).prev == null) {
            hashTable.get(node.data.getId()).next.prev = null;
            head = hashTable.get(node.data.getId()).next;
        } else if (hashTable.get(node.data.getId()).next == null) {
            hashTable.get(node.data.getId()).prev.next = null;
            tail = hashTable.get(node.data.getId()).prev;
        } else {
            hashTable.get(node.data.getId()).next.prev = hashTable.get(node.data.getId()).prev;
            hashTable.get(node.data.getId()).prev.next = hashTable.get(node.data.getId()).next;
        }
        hashTable.remove(node);
        size--;
    }
}


