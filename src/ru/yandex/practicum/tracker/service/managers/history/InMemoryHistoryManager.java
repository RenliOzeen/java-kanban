package ru.yandex.practicum.tracker.service.managers.history;

import ru.yandex.practicum.tracker.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class InMemoryHistoryManager implements HistoryManager {
    private final ArrayList<Task> viewsHistory;
    private final HashMap<Integer, Node> hashTable;
    private Node tail;
    private Node head;
    private int size = 0;

    public InMemoryHistoryManager() {
        viewsHistory = new ArrayList<>();
        hashTable = new HashMap<>();
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void add(Task task) {
        if (viewsHistory.contains(task)) {
            removeNode(hashTable.get(task.getId()));
            linkLast(task);
        } else {
            viewsHistory.add(task);
            linkLast(task);
        }


    }

    @Override
    public void remove(int id) {
        if (viewsHistory.contains(hashTable.get(id).data)) {
            viewsHistory.remove(hashTable.get(id).data);
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
        Node curNode = hashTable.get(head.data.getId());
        for (Node node : hashTable.values()) {
            historyList.add(curNode.data);
            if (curNode.next != null) {
                curNode = curNode.next;
            } else {
                break;
            }
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


