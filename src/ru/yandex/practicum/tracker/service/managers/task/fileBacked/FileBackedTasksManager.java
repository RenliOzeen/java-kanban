package ru.yandex.practicum.tracker.service.managers.task.fileBacked;

import ru.yandex.practicum.tracker.model.*;
import ru.yandex.practicum.tracker.service.managers.history.HistoryManager;
import ru.yandex.practicum.tracker.service.managers.task.inMemory.InMemoryTaskManager;


import java.io.*;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {

    public File backupFile;

    public FileBackedTasksManager() {
        backupFile = new File("C:\\Users\\lioze_0hcfbk4\\OneDrive\\Desktop\\папка с папками\\Я.П\\dev\\java-kanban\\src\\ru\\yandex\\practicum\\tracker\\service\\managers\\task\\fileBacked\\Backup.csv");
    }

    public static void main(String[] args) {
        FileBackedTasksManager taskManager = new FileBackedTasksManager();


        taskManager.createTask("Погулять с собакой", "Одеться; Выйти с собакой на улицу");

        taskManager.createTask("Купить продукты", "Написать список продуктов; Пойти в магазин");

        taskManager.createEpic("Получить права");
        taskManager.createSubTask("Получить права", "Пройти обучение в автошколе");
        taskManager.createSubTask("Получить права", "сдать экзамен");
        taskManager.createSubTask("Получить права", "отпраздновать");

        taskManager.createEpic("Приготовить ужин");
        System.out.println("Эпики" + taskManager.getEpics());
        System.out.println("Таски" + taskManager.getTasks());
        System.out.println("Сабтаски" + taskManager.getSubTasks());

        taskManager.getEpicById(3);
        taskManager.getSubTaskById(5);
        taskManager.getSubTaskById(4);
        taskManager.getSubTaskById(6);
        taskManager.getTaskById(2);
        taskManager.getEpicById(7);
        taskManager.getTaskById(1);
        taskManager.getSubTaskById(6);
        taskManager.getTaskById(2);
        System.out.println("история" + taskManager.getHistory());

        FileBackedTasksManager backupManager = new FileBackedTasksManager();
        backupManager = loadFromFile(backupManager.backupFile);
        System.out.println("Восстановленная история" + backupManager.getHistory());
        System.out.println("Восстановленные таски" + backupManager.getTasks());
        System.out.println("Восстановленные эпики" + backupManager.getEpics());
        System.out.println("Восстановленные сабтаски" + backupManager.getSubTasks());
    }

    @Override
    public Task createTask(String name, String details) {
        Task task = super.createTask(name, details);
        save();
        return task;
    }

    @Override
    public Epic createEpic(String name) {
        Epic epic = super.createEpic(name);
        save();
        return epic;
    }

    @Override
    public SubTask createSubTask(String epicName, String subTaskName) {
        SubTask subTask = super.createSubTask(epicName, subTaskName);
        save();
        return subTask;
    }

    @Override
    public void deleteTasks() {
        super.deleteTasks();
        save();
    }

    @Override
    public void deleteSubTasks() {
        super.deleteSubTasks();
        save();
    }

    @Override
    public void deleteEpics() {
        super.deleteEpics();
        save();
    }

    @Override
    public void updateTask(Task task, int id) {
        super.updateTask(task, id);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask, String epicName, int id) {
        super.updateSubTask(subTask, epicName, id);
        save();
    }

    @Override
    public void updateEpic(Epic epic, int id) {
        super.updateEpic(epic, id);
        save();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteSubTaskById(int id) {
        super.deleteSubTaskById(id);
        save();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public SubTask getSubTaskById(int id) {
        SubTask subTask = super.getSubTaskById(id);
        save();
        return subTask;
    }

    /**
     * Метод для перевода объекта задачи(Task) в строку
     *
     * @param task объект типа Task
     * @return строковое представление задачи
     */
    public String taskToString(Task task) {
        if (task.getType().equals(TaskType.SUBTASK)) {
            return task.getId() + "," +
                    task.getType() + "," +
                    task.getName() + "," +
                    task.getStatus() + "," +
                    task.getDetails() + "," +
                    ((SubTask) task).getEpicId();
        } else {
            return task.getId() + "," +
                    task.getType() + "," +
                    task.getName() + "," +
                    task.getStatus() + "," +
                    task.getDetails();
        }
    }

    /**
     * Метод для обратного преобразования строки в объект задачи(Task)
     *
     * @param task задача(Task) в формате строки
     * @return объект типа Task
     */
    public Task taskFromString(String task) {
        String[] taskParts = task.split(",");
        if (task.contains("SUBTASK")) {
            SubTask typeOfTask = new SubTask(taskParts[2], taskParts[4]);
            typeOfTask.setId(Integer.parseInt(taskParts[0]));
            typeOfTask.setName(taskParts[2]);
            typeOfTask.setStatus(TaskStatus.valueOf(taskParts[3]));
            typeOfTask.setDetails(taskParts[4]);
            typeOfTask.setEpicId(Integer.parseInt(taskParts[5]));
            return typeOfTask;
        } else if (task.contains("EPIC")) {
            Epic typeOfTask = new Epic(taskParts[2], taskParts[4]);
            typeOfTask.setId(Integer.parseInt(taskParts[0]));
            typeOfTask.setStatus(TaskStatus.valueOf(taskParts[3]));
            return typeOfTask;
        } else {
            Task typeOfTask = new Task(taskParts[2], taskParts[4]);
            typeOfTask.setId(Integer.parseInt(taskParts[0]));
            typeOfTask.setStatus(TaskStatus.valueOf(taskParts[3]));
            return typeOfTask;
        }
    }

    /**
     * Метод для преобразования истории просмотров в строку
     *
     * @param manager экземпляр менеджера истории просмотров
     * @return строковое представление(String) истории просмотров
     */
    public static String historyToString(HistoryManager manager) {
        StringBuilder historyRow = new StringBuilder();
        for (Task task : manager.getHistory()) {
            if (task == manager.getHistory().get(manager.getHistory().size() - 1)) {
                historyRow.append(task.getId());
            } else {
                historyRow.append(task.getId() + ",");
            }
        }
        return historyRow.toString();
    }

    /**
     * Метод для обратного преобразования строки в историю просмотров
     *
     * @param value строковое представление истории просмотров
     * @return List истории просмотров
     */
    public static List<Integer> historyFromString(String value) {
        String[] historyRows = value.split(",");
        Integer[] historyId = new Integer[historyRows.length];
        for (int i = 0; i < historyRows.length; i++) {
            historyId[i] = Integer.parseInt(historyRows[i]);
        }
        return List.of(historyId);
    }

    /**
     * Метод для сохранения задач/подзадач/эпиков и истории просмотров в backup файл
     */
    public void save() {
        try (FileWriter writer = new FileWriter(backupFile)) {
            writer.write("id,type,name,status,details,epicId");
            for (Task task : getTasks()) {
                String row = taskToString(task);
                writer.write("\n" + row);
            }

            for (Epic task : getEpics()) {
                String row = taskToString(task);
                writer.write("\n" + row);
            }

            for (SubTask task : getSubTasks()) {
                String row = taskToString(task);
                writer.write("\n" + row);
            }

            String history = historyToString(getHistoryManager());
            writer.write("\n\n" + history);
        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка сохранения в файл");
        }
    }


    /**
     * Метод для восстановления экземпляра менеджера из backup файла
     *
     * @param file файл backup-а
     * @return восстановленный экземпляр менеджера
     */
    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager();
        String row = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (reader.ready()) {
                row = reader.readLine();

                if (row.contains("SUBTASK")) {
                    SubTask task = ((SubTask) fileBackedTasksManager.taskFromString(row));
                    fileBackedTasksManager.getMapOfSubTasks().put(task.getId(), task);

                } else if (row.contains("TASK")) {
                    Task task = fileBackedTasksManager.taskFromString(row);
                    fileBackedTasksManager.getMapOfTasks().put(task.getId(), task);

                } else if (row.contains("EPIC")) {
                    Epic task = ((Epic) fileBackedTasksManager.taskFromString(row));
                    fileBackedTasksManager.getMapOfEpics().put(task.getId(), task);
                }

            }
            List<Integer> taskId = historyFromString(row);
            for (int id : taskId) {
                if (fileBackedTasksManager.getTaskById(id) != null) {
                    fileBackedTasksManager.getHistoryManager().getHistory().add(fileBackedTasksManager.getTaskById(id));
                } else if (fileBackedTasksManager.getSubTaskById(id) != null) {
                    fileBackedTasksManager.getHistoryManager().getHistory().add(fileBackedTasksManager.getSubTaskById(id));
                } else {
                    fileBackedTasksManager.getHistoryManager().getHistory().add(fileBackedTasksManager.getEpicById(id));
                }
            }

            return fileBackedTasksManager;
        } catch (IOException exception) {
            throw new ManagerLoadException("Ошибка восстановления из файла");
        }
    }
}
