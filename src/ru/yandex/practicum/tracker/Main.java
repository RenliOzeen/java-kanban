package ru.yandex.practicum.tracker;

import com.google.gson.Gson;
import ru.yandex.practicum.tracker.server.kv.server.KVServer;
import ru.yandex.practicum.tracker.service.managers.Managers;
import ru.yandex.practicum.tracker.service.managers.task.TaskManager;

import java.io.IOException;

public class Main {


    public static void main(String[] args) throws IOException {
        String key="0";
        new KVServer().start();
        Gson gson=new Gson();
        //различные тесты:
        TaskManager manager=Managers.getDefault();
        manager.createTask("task1", null, 2, "2022-09-01T12:15");
        manager.createTask("task2", null, 2, "2022-08-25T12:19");


        manager.createTask("task3", null, 2, "2022-09-04T12:15");
        manager.createEpic("Получить права");
        manager.createSubTask("Получить права", "Пройти обучение в автошколе", 1300, "2022-09-03T12:15");
        manager.createSubTask("Получить права", "сдать экзамен", 180, "2022-09-07T12:15");
        manager.createSubTask("Получить права", "отпраздновать", 120, "2022-09-11T12:15");



        System.out.println(manager.getEpicById(4));
        System.out.println("история" + manager.getHistory());
        System.out.println(manager.getSubTaskById(5));
        System.out.println("история" + manager.getHistory());
        System.out.println(manager.getSubTaskById(7));
        System.out.println("история" + manager.getHistory());
        System.out.println(manager.getSubTaskById(6));
        System.out.println("история" + manager.getHistory());
        System.out.println(manager.getTaskById(2));
        System.out.println("история" + manager.getHistory());
        System.out.println(manager.getTaskById(1));
        System.out.println("история" + manager.getHistory());
        System.out.println(manager.getSubTaskById(6));
        System.out.println("история" + manager.getHistory());
        System.out.println(manager.getTaskById(2));
        System.out.println("история" + manager.getHistory());
        System.out.println(manager.getTaskById(1));
        System.out.println("история" + manager.getHistory());
        System.out.println(manager.getEpicById(4));
        System.out.println("история" + manager.getHistory());
        System.out.println(manager.getSubTaskById(5));
        System.out.println("история" + manager.getHistory());
        System.out.println(manager.getSubTaskById(7));
        System.out.println("история" + manager.getHistory());
        System.out.println(manager.getTaskById(1));
        System.out.println("история" + manager.getHistory());
        System.out.println(manager.getSubTaskById(6));
        System.out.println("история" + manager.getHistory());
        System.out.println(manager.getTaskById(2));
        System.out.println("история" + manager.getHistory());
        System.out.println(manager.getEpicById(4));
        System.out.println("история" + manager.getHistory());
        System.out.println(manager.getSubTaskById(5));
        System.out.println("история" + manager.getHistory());
        System.out.println(manager.getSubTaskById(7));
        System.out.println("история" + manager.getHistory());
        System.out.println(manager.getSubTaskById(6));
        System.out.println("история" + manager.getHistory());
        System.out.println(manager.getTaskById(2));
        System.out.println("история" + manager.getHistory());
        System.out.println(manager.getTaskById(1));
        System.out.println("история" + manager.getHistory());
        System.out.println(manager.getSubTaskById(6));
        System.out.println("история" + manager.getHistory());

    }
}
