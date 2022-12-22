package ru.yandex.practicum.tracker.service.managers.task.fileBacked;

import ru.yandex.practicum.tracker.exception.ManagerLoadException;
import ru.yandex.practicum.tracker.exception.ManagerSaveException;
import ru.yandex.practicum.tracker.model.*;
import ru.yandex.practicum.tracker.service.managers.task.inMemory.InMemoryTaskManager;


import java.io.*;
import java.util.List;
import java.util.Optional;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private File backupFile;

    public FileBackedTasksManager() {
        backupFile = new File("C:\\Users\\lioze_0hcfbk4\\OneDrive\\Desktop\\папка с папками\\Я.П\\dev\\java-kanban\\src\\ru\\yandex\\practicum\\tracker\\service\\managers\\task\\fileBacked\\Backup.csv");
    }

    public static void main(String[] args) throws IOException {
        FileBackedTasksManager taskManager = new FileBackedTasksManager();

        taskManager.loadFromFile(taskManager.backupFile);
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

        FileBackedTasksManager backupManager = new FileBackedTasksManager();
        backupManager = loadFromFile(backupManager.backupFile);
        System.out.println("Восстановленная история" + backupManager.getHistory());
        System.out.println("Восстановленные таски" + backupManager.getTasks());
        System.out.println("Восстановленные эпики" + backupManager.getEpics());
        System.out.println("Восстановленные сабтаски" + backupManager.getSubTasks());
    }

    @Override
    public Task createTask(String name, String details, long minutesToComplete, String date) {
        Task task = super.createTask(name, details, minutesToComplete, date);
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
    public SubTask createSubTask(String epicName, String subTaskName, long minutesToComplete, String date) {
        SubTask subTask = super.createSubTask(epicName, subTaskName, minutesToComplete, date);
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

    public File getBackupFile() {
        return backupFile;
    }

    /**
     * Метод для сохранения задач/подзадач/эпиков и истории просмотров в backup файл
     */
    public void save() {
        try (FileWriter writer = new FileWriter(backupFile)) {
            writer.write("id,type,name,status,details,epicId,startTime,endTime,duration");
            for (Task task : getTasks()) {
                String row = CSVTaskFormatter.taskToString(task);
                writer.write("\n" + row);
            }

            for (Epic task : getEpics()) {
                String row = CSVTaskFormatter.taskToString(task);
                writer.write("\n" + row);
            }

            for (SubTask task : getSubTasks()) {
                String row = CSVTaskFormatter.taskToString(task);
                writer.write("\n" + row);
            }

            String history = CSVTaskFormatter.historyToString(getHistoryManager());
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
                    SubTask task = ((SubTask) CSVTaskFormatter.taskFromString(row));
                    fileBackedTasksManager.getMapOfSubTasks().put(task.getId(), task);

                } else if (row.contains("TASK")) {
                    Task task = CSVTaskFormatter.taskFromString(row);
                    fileBackedTasksManager.getMapOfTasks().put(task.getId(), task);

                } else if (row.contains("EPIC")) {
                    Epic task = ((Epic) CSVTaskFormatter.taskFromString(row));
                    fileBackedTasksManager.getMapOfEpics().put(task.getId(), task);
                }


            }

            for (int subId : fileBackedTasksManager.getMapOfSubTasks().keySet()) {
                for (int epicId : fileBackedTasksManager.getMapOfEpics().keySet()) {
                    if (fileBackedTasksManager.getMapOfSubTasks().get(subId).getEpicId() == epicId) {
                        fileBackedTasksManager.getMapOfEpics().get(epicId).getSubTasks().add(subId);
                    }
                }
            }

            if (row != null && !row.isEmpty()) {
                List<Integer> taskId = CSVTaskFormatter.historyFromString(row);
                for (int id : taskId) {
                    if (fileBackedTasksManager.getTaskById(id) != null) {
                        fileBackedTasksManager.getHistoryManager().getHistory().add(fileBackedTasksManager.getTaskById(id));
                    } else if (fileBackedTasksManager.getSubTaskById(id) != null) {
                        fileBackedTasksManager.getHistoryManager().getHistory().add(fileBackedTasksManager.getSubTaskById(id));
                    } else {
                        fileBackedTasksManager.getHistoryManager().getHistory().add(fileBackedTasksManager.getEpicById(id));
                    }
                }
            }

            return fileBackedTasksManager;
        } catch (IOException exception) {
            throw new ManagerLoadException("Ошибка восстановления из файла");
        }
    }
}
