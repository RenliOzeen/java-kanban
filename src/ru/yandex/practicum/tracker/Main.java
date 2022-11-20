package ru.yandex.practicum.tracker;

import ru.yandex.practicum.tracker.service.managers.Managers;
import ru.yandex.practicum.tracker.service.managers.task.TaskManager;

public class Main {

    public static void main(String[] args) {
        //различные тесты:

        TaskManager taskManager = Managers.getDefault();


        taskManager.createTask("Погулять с собакой", "Одеться\n Выйти с собакой на улицу");

        taskManager.createTask("Купить продукты", "Написать список продуктов\n Пойти в магазин");

        taskManager.createEpic("Получить права");
        taskManager.createSubTask("Получить права", "Пройти обучение в автошколе");
        taskManager.createSubTask("Получить права", "сдать экзамен");
        taskManager.createSubTask("Получить права", "отпраздновать");

        taskManager.createEpic("Приготовить ужин");

        System.out.println(taskManager.getEpicById(3));
        System.out.println("история" + taskManager.getHistory());
        System.out.println(taskManager.getSubTaskById(5));
        System.out.println("история" + taskManager.getHistory());
        System.out.println(taskManager.getSubTaskById(4));
        System.out.println("история" + taskManager.getHistory());
        System.out.println(taskManager.getSubTaskById(6));
        System.out.println("история" + taskManager.getHistory());
        System.out.println(taskManager.getTaskById(2));
        System.out.println("история" + taskManager.getHistory());
        System.out.println(taskManager.getEpicById(7));
        System.out.println("история" + taskManager.getHistory());
        System.out.println(taskManager.getTaskById(1));
        System.out.println("история" + taskManager.getHistory());
        System.out.println(taskManager.getSubTaskById(6));
        System.out.println("история" + taskManager.getHistory());
        System.out.println(taskManager.getTaskById(2));
        System.out.println("история" + taskManager.getHistory());
        System.out.println(taskManager.getEpicById(7));
        System.out.println("история" + taskManager.getHistory());
        System.out.println(taskManager.getTaskById(1));
        System.out.println("история" + taskManager.getHistory());
        System.out.println(taskManager.getEpicById(3));
        System.out.println("история" + taskManager.getHistory());
        System.out.println(taskManager.getSubTaskById(5));
        System.out.println("история" + taskManager.getHistory());
        System.out.println(taskManager.getSubTaskById(4));
        System.out.println("история" + taskManager.getHistory());
        System.out.println(taskManager.getEpicById(7));
        System.out.println("история" + taskManager.getHistory());
        System.out.println(taskManager.getTaskById(1));
        System.out.println("история" + taskManager.getHistory());
        System.out.println(taskManager.getSubTaskById(6));
        System.out.println("история" + taskManager.getHistory());
        System.out.println(taskManager.getTaskById(2));
        System.out.println("история" + taskManager.getHistory());
        System.out.println(taskManager.getEpicById(7));
        System.out.println("история" + taskManager.getHistory());
        System.out.println(taskManager.getEpicById(3));
        System.out.println("история" + taskManager.getHistory());
        System.out.println(taskManager.getSubTaskById(5));
        System.out.println("история" + taskManager.getHistory());
        System.out.println(taskManager.getSubTaskById(4));
        System.out.println("история" + taskManager.getHistory());
        System.out.println(taskManager.getSubTaskById(6));
        System.out.println("история" + taskManager.getHistory());
        System.out.println(taskManager.getTaskById(2));
        System.out.println("история" + taskManager.getHistory());
        System.out.println(taskManager.getEpicById(7));
        System.out.println("история" + taskManager.getHistory());
        System.out.println(taskManager.getTaskById(1));
        System.out.println("история" + taskManager.getHistory());
        System.out.println(taskManager.getSubTaskById(6));
        System.out.println("история" + taskManager.getHistory());
        System.out.println(taskManager.getTaskById(2));
        System.out.println("история" + taskManager.getHistory());
        System.out.println(taskManager.getEpicById(7));
        System.out.println("история" + taskManager.getHistory());
        System.out.println(taskManager.getTaskById(1));
        System.out.println("история" + taskManager.getHistory());
        System.out.println(taskManager.getEpicById(3));
        System.out.println("история" + taskManager.getHistory());

        System.out.println("удаляю таску");
        taskManager.deleteTaskById(1);
        System.out.println("история" + taskManager.getHistory());

        System.out.println("Удаляю эпик");
        taskManager.deleteEpicById(3);
        System.out.println("история" + taskManager.getHistory());
    }
}
