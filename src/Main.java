public class Main {

    public static void main(String[] args) {
        //различные тесты:
        Manager manager = new Manager();

        manager.createTask("Погулять с собакой", "Одеться\n Выйти с собакой на улицу");

        manager.createTask("Купить продукты", "Написать список продуктов\n Пойти в магазин");

        manager.createEpic("Получить права");
        manager.createSubTask("Получить права", "Пройти обучение в автошколе");
        manager.createSubTask("Получить права", "сдать экзамен");

        manager.createEpic("Приготовить ужин");
        manager.createSubTask("Приготовить ужин", "Купить продукты");

        System.out.println("TASKS: " + manager.getTasks());
        System.out.println("EPICS: " + manager.getEpics());
        System.out.println("SUBTASKS: " + manager.getSubTasks());

        Task task = new Task("Съесть мандарин", "Дождаться Нового года");
        task.setStatus("DONE");

        SubTask subTask1 = new SubTask("Накопить на взятку", null);
        subTask1.setStatus("DONE");

        SubTask subTask2 = new SubTask("Научиться готовить", null);
        subTask2.setStatus("IN_PROGRESS");

        manager.updateTask(task, 2);
        manager.updateSubTask(subTask1, "Получить права", 5);
        manager.updateSubTask(subTask2, "Приготовить ужин", 7);

        System.out.println("TASKS: " + manager.getTasks());
        System.out.println("EPICS: " + manager.getEpics());
        System.out.println("SUBTASKS: " + manager.getSubTasks());

        SubTask subTask3 = new SubTask("Покататься на жигулях на даче", null);
        subTask3.setStatus("DONE");

        manager.updateSubTask(subTask3, "Получить права", 4);

        System.out.println("TASKS: " + manager.getTasks());
        System.out.println("EPICS: " + manager.getEpics());
        System.out.println("SUBTASKS: " + manager.getSubTasks());

        manager.deleteTaskById(2);
        manager.deleteEpicById(6);


        System.out.println("TASKS: " + manager.getTasks());
        System.out.println("EPICS: " + manager.getEpics());
        System.out.println("SUBTASKS: " + manager.getSubTasks());

    }
}
