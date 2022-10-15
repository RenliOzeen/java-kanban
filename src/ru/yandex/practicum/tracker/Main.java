package ru.yandex.practicum.tracker;

import ru.yandex.practicum.tracker.model.SubTask;
import ru.yandex.practicum.tracker.model.Task;
import ru.yandex.practicum.tracker.service.TaskStatus;
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

        taskManager.createEpic("Приготовить ужин");
        taskManager.createSubTask("Приготовить ужин", "Купить продукты");

        System.out.println("TASKS: " + taskManager.getTasks());
        System.out.println("EPICS: " + taskManager.getEpics());
        System.out.println("SUBTASKS: " + taskManager.getSubTasks());

        Task task = new Task("Съесть мандарин", "Дождаться Нового года");
        task.setStatus(TaskStatus.DONE);

        SubTask subTask1 = new SubTask("Накопить на взятку", null);
        subTask1.setStatus(TaskStatus.DONE);

        SubTask subTask2 = new SubTask("Научиться готовить", null);
        subTask2.setStatus(TaskStatus.IN_PROGRESS);

        taskManager.updateTask(task, 2);
        taskManager.updateSubTask(subTask1, "Получить права", 5);
        taskManager.updateSubTask(subTask2, "Приготовить ужин", 7);

        System.out.println("TASKS: " + taskManager.getTasks());
        System.out.println("EPICS: " + taskManager.getEpics());
        System.out.println("SUBTASKS: " + taskManager.getSubTasks());

        SubTask subTask3 = new SubTask("Покататься на жигулях на даче", null);
        subTask3.setStatus(TaskStatus.DONE);

        taskManager.updateSubTask(subTask3, "Получить права", 4);

        System.out.println("TASKS: " + taskManager.getTasks());
        System.out.println("EPICS: " + taskManager.getEpics());
        System.out.println("SUBTASKS: " + taskManager.getSubTasks());

        taskManager.deleteTaskById(2);
        taskManager.deleteEpicById(6);


        System.out.println("TASKS: " + taskManager.getTasks());
        System.out.println("EPICS: " + taskManager.getEpics());
        System.out.println("SUBTASKS: " + taskManager.getSubTasks());


        System.out.println(taskManager.getTaskById(1));
        System.out.println(taskManager.getHistoryManager().getHistory());

        System.out.println(taskManager.getTaskById(2));
        System.out.println(taskManager.getHistoryManager().getHistory());

        System.out.println(taskManager.getEpicById(3));
        System.out.println(taskManager.getHistoryManager().getHistory());

        System.out.println(taskManager.getEpicById(6));
        System.out.println(taskManager.getHistoryManager().getHistory());

        System.out.println(taskManager.getSubTaskById(4));
        System.out.println(taskManager.getHistoryManager().getHistory());

        System.out.println(taskManager.getSubTaskById(5));
        System.out.println(taskManager.getHistoryManager().getHistory());

        System.out.println(taskManager.getSubTaskById(7));
        System.out.println(taskManager.getHistoryManager().getHistory());
    }
}
