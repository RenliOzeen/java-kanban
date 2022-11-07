package ru.yandex.practicum.tracker.service.managers.history;

import ru.yandex.practicum.tracker.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class InMemoryHistoryManager implements HistoryManager {

    private final HashMap<Integer, Node> hashTable;
    private Node tail;
    private Node head;

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
            hashTable.put(task.getId(), tail);
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
        Node nodeMap=hashTable.remove(node.data.getId());
        if(nodeMap==null){
            return;
        } else{
            if (nodeMap.prev == null) {
                nodeMap.next.prev = null;
                head = nodeMap.next;
            } else if (nodeMap.next == null) {
                nodeMap.prev.next = null;
                tail = nodeMap.prev;
            } else {
                nodeMap.next.prev = nodeMap.prev;
                nodeMap.prev.next = nodeMap.next;
            }
        }
    }
}


