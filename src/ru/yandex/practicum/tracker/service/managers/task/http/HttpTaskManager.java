package ru.yandex.practicum.tracker.service.managers.task.http;

import com.google.gson.Gson;
import ru.yandex.practicum.tracker.model.Epic;
import ru.yandex.practicum.tracker.model.SubTask;
import ru.yandex.practicum.tracker.model.Task;
import ru.yandex.practicum.tracker.server.kv.server.KVServer;
import ru.yandex.practicum.tracker.server.kv.client.KVTaskClient;
import ru.yandex.practicum.tracker.service.managers.history.HistoryManager;
import ru.yandex.practicum.tracker.service.managers.task.fileBacked.CSVTaskFormatter;
import ru.yandex.practicum.tracker.service.managers.task.fileBacked.FileBackedTasksManager;
import ru.yandex.practicum.tracker.service.managers.task.inMemory.InMemoryTaskManager;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class HttpTaskManager extends FileBackedTasksManager {
    private String keyToTaskManager = "0";
    private String keyToHistoryManager = "1";
    private static final Gson gson = new Gson();
    private final KVTaskClient client;


    public HttpTaskManager(String url) {
        client = new KVTaskClient(url);
    }

    public static void main(String[] args) throws IOException {
        new KVServer().start();
        HttpTaskManager taskManager = new HttpTaskManager("http://localhost:8078");

        taskManager.createTask("Погулять с собакой", "Одеться; Выйти с собакой на улицу", 25, "2022-08-25T12:15");

        taskManager.createTask("Купить продукты", "Написать список продуктов; Пойти в магазин", 60, "2022-08-17T12:19");

        taskManager.createEpic("Получить права");
        taskManager.createSubTask("Получить права", "Пройти обучение в автошколе", 1300, "2022-09-01T12:15");
        taskManager.createSubTask("Получить права", "сдать экзамен", 180, "2022-09-04T12:15");
        taskManager.createSubTask("Получить права", "отпраздновать", 120, "2022-09-11T12:15");

        taskManager.createEpic("Приготовить ужин");

        Task task1 = new Task("Скушать яблоко", "Найти яблоко; потом можно и скушать", 60, Optional.of("2022-08-17T12:20"));
        taskManager.updateTask(task1, 2);
        System.out.println("Эпики" + taskManager.getEpics());
        System.out.println("Таски" + taskManager.getTasks());
        System.out.println("Сабтаски" + taskManager.getSubTasks());

        taskManager.getEpicById(3);
        taskManager.getSubTaskById(5);
        taskManager.getSubTaskById(4);
        taskManager.getSubTaskById(6);
        taskManager.getTaskById(222);
        taskManager.getEpicById(7);
        taskManager.getTaskById(1);
        taskManager.getSubTaskById(6);
        taskManager.getTaskById(2);
        System.out.println("Список отсортированных задач" + taskManager.getPrioritizedTasks());
        System.out.println("история" + taskManager.getHistory());

        InMemoryTaskManager backupManager = new HttpTaskManager("http://localhost:8078");

        backupManager = taskManager.loadFromServer(taskManager.client.load(taskManager.keyToTaskManager),
                taskManager.client.load(taskManager.keyToHistoryManager));
        System.out.println("Восстановленная история" + backupManager.getHistory());
        System.out.println("Восстановленные таски" + backupManager.getTasks());
        System.out.println("Восстановленные эпики" + backupManager.getEpics());
        System.out.println("Восстановленные сабтаски" + backupManager.getSubTasks());
    }


    @Override
    public void save() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        for (Task task : getTasks()) {
            manager.getMapOfTasks().put(task.getId(), task);
        }

        for (Epic task : getEpics()) {
            manager.getMapOfEpics().put(task.getId(), task);

        }

        for (SubTask task : getSubTasks()) {
            manager.getMapOfSubTasks().put(task.getId(), task);
        }
        HistoryManager historyManager = getHistoryManager();


        client.put(keyToHistoryManager, gson.toJson(CSVTaskFormatter.historyToString(historyManager)));
        client.put(keyToTaskManager, gson.toJson(manager));


    }

    /**
     * Метод для восстановления экземпляра менеджера из json
     *
     * @param jsonTask
     * @param jsonHistory
     * @return
     */
    public InMemoryTaskManager loadFromServer(String jsonTask, String jsonHistory) {
        String strHistory = jsonHistory.substring(1, jsonHistory.length() - 1);
        List<Integer> history = CSVTaskFormatter.historyFromString(strHistory);
        InMemoryTaskManager manager = gson.fromJson(jsonTask, InMemoryTaskManager.class);
        for (int id : history) {
            if (manager.getTaskById(id) != null) {
                manager.getHistoryManager().getHistory().add(manager.getTaskById(id));
            } else if (manager.getSubTaskById(id) != null) {
                manager.getHistoryManager().getHistory().add(manager.getSubTaskById(id));
            } else {
                manager.getHistoryManager().getHistory().add(manager.getEpicById(id));
            }
        }

        return manager;
    }

}
