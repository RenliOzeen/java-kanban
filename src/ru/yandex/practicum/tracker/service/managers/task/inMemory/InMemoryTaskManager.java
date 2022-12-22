package ru.yandex.practicum.tracker.service.managers.task.inMemory;

import ru.yandex.practicum.tracker.exception.ValidateStartTimeException;
import ru.yandex.practicum.tracker.model.Epic;
import ru.yandex.practicum.tracker.model.SubTask;
import ru.yandex.practicum.tracker.model.Task;
import ru.yandex.practicum.tracker.model.TaskStatus;
import ru.yandex.practicum.tracker.service.managers.history.HistoryManager;
import ru.yandex.practicum.tracker.service.managers.history.InMemoryHistoryManager;
import ru.yandex.practicum.tracker.service.managers.task.TaskManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;


public class InMemoryTaskManager implements TaskManager {
    private int id;
    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, SubTask> subTasks;
    private final HashMap<Integer, Epic> epics;
    private InMemoryHistoryManager historyManager;

    public InMemoryTaskManager() {
        id = 0;
        tasks = new HashMap<>();
        subTasks = new HashMap<>();
        epics = new HashMap<>();
        historyManager = new InMemoryHistoryManager();
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    @Override
    public ArrayList<Task> getTasks() {
        ArrayList<Task> tasksList = new ArrayList<>();
        for (int id : tasks.keySet()) {
            tasksList.add(tasks.get(id));
        }
        return tasksList;
    }

    @Override
    public ArrayList<SubTask> getSubTasks() {
        ArrayList<SubTask> subTasksList = new ArrayList<>();
        for (int id : subTasks.keySet()) {
            subTasksList.add(subTasks.get(id));
        }
        return subTasksList;
    }

    @Override
    public ArrayList<Epic> getEpics() {
        ArrayList<Epic> epicList = new ArrayList<>();
        for (int id : epics.keySet()) {
            epicList.add(epics.get(id));
        }
        return epicList;
    }

    @Override
    public Task createTask(String name, String details, long minutesToComplete, String date) {
        if (!getPrioritizedTasks().isEmpty()) {
            for (Task dateTask : getPrioritizedTasks()) {
                if (!validationTaskCompleteInterval(LocalDateTime.parse(date, Task.formatter),
                        dateTask.getStartTime().get(),
                        LocalDateTime.parse(date, Task.formatter).plus(Duration.ofMinutes(minutesToComplete)),
                        dateTask.getEndTime().get())) {
                    throw new ValidateStartTimeException("Данные дата/время начала выполнения задачи заняты");
                } else {
                    Task task = new Task(name, details, minutesToComplete, Optional.of(date));
                    task.setId(createId());
                    tasks.put(task.getId(), task);
                    return task;
                }
            }
        }
        Task task = new Task(name, details, minutesToComplete, Optional.of(date));
        task.setId(createId());
        tasks.put(task.getId(), task);
        return task;

    }

    @Override
    public Epic createEpic(String name) {
        Epic epic = new Epic(name, null);
        epic.setId(createId());
        epics.put(epic.getId(), epic);
        return epic;
    }

    @Override
    public SubTask createSubTask(String epicName, String subTaskName,
                                 long minutesToComplete, String date) {
        if (!getPrioritizedTasks().isEmpty()) {
            for (Task dateTask : getPrioritizedTasks()) {
                if (!validationTaskCompleteInterval(LocalDateTime.parse(date, Task.formatter),
                        dateTask.getStartTime().get(),
                        LocalDateTime.parse(date, Task.formatter).plus(Duration.ofMinutes(minutesToComplete)),
                        dateTask.getEndTime().get())) {
                    throw new ValidateStartTimeException("Данные дата/время начала выполнения задачи заняты");
                } else {
                    SubTask subTask = new SubTask(subTaskName, null, minutesToComplete, Optional.of(date));
                    subTask.setId(createId());
                    for (int id : epics.keySet()) {
                        if (epics.get(id).getName().equals(epicName)) {
                            if (epics.get(id).getSubTasks().isEmpty()) {
                                epics.get(id).setStartTime(subTask.getStartTime().get());
                                epics.get(id).setEndTime(subTask.getEndTime().get());
                                epics.get(id).setDuration(subTask.getDuration());
                            } else {
                                if (epics.get(id).getStartTime().get().isAfter(subTask.getStartTime().get())) {
                                    epics.get(id).setStartTime(subTask.getStartTime().get());
                                }
                                if (epics.get(id).getEndTime().get().isBefore(subTask.getEndTime().get())) {
                                    epics.get(id).setEndTime(subTask.getEndTime().get());
                                }
                                epics.get(id).setDuration(epics.get(id).getDuration().plus(subTask.getDuration()));
                            }
                            subTasks.put(subTask.getId(), subTask);
                            subTask.setEpicId(id);
                            epics.get(id).setSubTasks(subTask.getId());

                        }
                    }
                    subTask.setEpicName(epicName);
                    return subTask;
                }
            }
        }
        SubTask subTask = new SubTask(subTaskName, null, minutesToComplete, Optional.of(date));
        subTask.setId(createId());
        for (int id : epics.keySet()) {
            if (epics.get(id).getName().equals(epicName)) {
                if (epics.get(id).getSubTasks().isEmpty()) {
                    epics.get(id).setStartTime(subTask.getStartTime().get());
                    epics.get(id).setEndTime(subTask.getEndTime().get());
                    epics.get(id).setDuration(subTask.getDuration());
                } else {
                    if (epics.get(id).getStartTime().get().isAfter(subTask.getStartTime().get())) {
                        epics.get(id).setStartTime(subTask.getStartTime().get());
                    }
                    if (epics.get(id).getEndTime().get().isBefore(subTask.getEndTime().get())) {
                        epics.get(id).setEndTime(subTask.getEndTime().get());
                    }
                    epics.get(id).setDuration(epics.get(id).getDuration().plus(subTask.getDuration()));
                }
                subTasks.put(subTask.getId(), subTask);
                subTask.setEpicId(id);
                epics.get(id).setSubTasks(subTask.getId());

            }
        }
        subTask.setEpicName(epicName);
        return subTask;
    }

    @Override
    public void deleteTasks() {
        tasks.clear();
    }

    @Override
    public void deleteSubTasks() {
        for (SubTask task : subTasks.values()) {
            if (task.getEpicId() != 0) {
                epics.get(task.getEpicId()).getSubTasks().clear();
                epics.get(task.getEpicId()).setStatus(TaskStatus.NEW);
            }
        }
        subTasks.clear();
    }

    @Override
    public void deleteEpics() {
        epics.clear();
        for (int id : subTasks.keySet()) {
            subTasks.get(id).setEpicId(0);
        }
    }

    @Override
    public Task getTaskById(int id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Epic getEpicById(int id) {
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    public SubTask getSubTaskById(int id) {
        historyManager.add(subTasks.get(id));
        return subTasks.get(id);
    }

    @Override
    public void updateTask(Task task, int id) {
        deleteTaskById(id);
        if (!(subTasks.isEmpty() && tasks.isEmpty())) {
            for (Task dateTask : getPrioritizedTasks()) {
                if (!validationTaskCompleteInterval(task.getStartTime().get(),
                        dateTask.getStartTime().get(),
                        task.getEndTime().get(),
                        dateTask.getEndTime().get())) {
                    throw new ValidateStartTimeException("Данные дата/время начала выполнения задачи заняты");
                } else {
                    task.setId(id);
                    tasks.put(id, task);
                }
            }
        } else {
            task.setId(id);
            tasks.put(id, task);
        }
    }

    @Override
    public void updateSubTask(SubTask subTask, String epicName, int id) {
        deleteSubTaskById(id);
        if (!(subTasks.isEmpty() && tasks.isEmpty())) {
            for (Task dateTask : getPrioritizedTasks()) {
                if (!validationTaskCompleteInterval(subTask.getStartTime().get(),
                        dateTask.getStartTime().get(),
                        subTask.getEndTime().get(),
                        dateTask.getEndTime().get())) {
                    throw new ValidateStartTimeException("Данные дата/время начала выполнения задачи заняты");
                } else {
                    subTask.setId(id);
                    subTasks.put(id, subTask);
                    for (int uid : epics.keySet()) {
                        if (epics.get(uid).getName().equals(epicName)) {
                            subTask.setEpicId(uid);
                            epics.get(uid).setSubTasks(id);
                            if (subTask.getStatus() == TaskStatus.IN_PROGRESS || subTask.getStatus() == TaskStatus.DONE) {
                                epics.get(uid).setStatus(TaskStatus.IN_PROGRESS);
                            }
                            if (epics.get(uid).getStartTime().get().isBefore(subTask.getStartTime().get())) {
                                epics.get(uid).setStartTime(subTask.getStartTime().get());
                            }
                            if (epics.get(uid).getEndTime().get().isAfter(subTask.getEndTime().get())) {
                                epics.get(uid).setEndTime(subTask.getEndTime().get());
                            }
                            epics.get(uid).setDuration(epics.get(uid).getDuration().
                                    minus(subTasks.get(id).getDuration()));
                            epics.get(uid).setDuration(epics.get(uid).getDuration().plus(subTask.getDuration()));
                        }
                    }
                    for (int uid : epics.keySet()) {
                        int count = 0;
                        for (int subId : epics.get(uid).getSubTasks()) {
                            if (subTasks.get(subId).getStatus() == TaskStatus.DONE) {
                                count++;
                            }
                            if (count == epics.get(uid).getSubTasks().size()) {
                                epics.get(uid).setStatus(TaskStatus.DONE);
                            }

                        }
                    }
                }
            }
        } else {
            subTask.setId(id);
            subTasks.put(id, subTask);
            for (int uid : epics.keySet()) {
                if (epics.get(uid).getName().equals(epicName)) {
                    subTask.setEpicId(uid);
                    epics.get(uid).setSubTasks(id);
                    if (subTask.getStatus() == TaskStatus.IN_PROGRESS || subTask.getStatus() == TaskStatus.DONE) {
                        epics.get(uid).setStatus(TaskStatus.IN_PROGRESS);
                    }
                    if (epics.get(uid).getStartTime().get().isBefore(subTask.getStartTime().get())) {
                        epics.get(uid).setStartTime(subTask.getStartTime().get());
                    }
                    if (epics.get(uid).getEndTime().get().isAfter(subTask.getEndTime().get())) {
                        epics.get(uid).setEndTime(subTask.getEndTime().get());
                    }
                    epics.get(uid).setDuration(epics.get(uid).getDuration().
                            minus(subTasks.get(id).getDuration()));
                    epics.get(uid).setDuration(epics.get(uid).getDuration().plus(subTask.getDuration()));
                }
            }
            for (int uid : epics.keySet()) {
                int count = 0;
                for (int subId : epics.get(uid).getSubTasks()) {
                    if (subTasks.get(subId).getStatus() == TaskStatus.DONE) {
                        count++;
                    }
                    if (count == epics.get(uid).getSubTasks().size()) {
                        epics.get(uid).setStatus(TaskStatus.DONE);
                    }

                }
            }
        }
    }

    @Override
    public void updateEpic(Epic epic, int id) {
        epic.setId(id);
        epics.put(id, epic);
    }

    @Override
    public void deleteTaskById(int id) {
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteEpicById(int id) {
        historyManager.remove(id);
        List<Integer> epicSubs = epics.get(id).getSubTasks();
        for (int num : epicSubs) {
            subTasks.get(num).setEpicId(0);
            historyManager.remove(num);
        }
        epics.remove(id);
    }

    @Override
    public void deleteSubTaskById(int id) {
        epics.get(subTasks.get(id).getEpicId()).getSubTasks().remove((Integer) subTasks.get(id).getId());
        subTasks.remove(id);
        historyManager.remove(id);

    }

    @Override
    public List<SubTask> getListSubsOfEpic(int epicId) {
        List<SubTask> listSubsOfEpic = new ArrayList<>();
        for (int subId : epics.get(epicId).getSubTasks()) {
            listSubsOfEpic.add(subTasks.get(subId));
        }
        return listSubsOfEpic;
    }

    @Override
    public HashMap<Integer, Task> getMapOfTasks() {
        return tasks;
    }

    @Override
    public HashMap<Integer, SubTask> getMapOfSubTasks() {
        return subTasks;
    }

    @Override
    public HashMap<Integer, Epic> getMapOfEpics() {
        return epics;
    }

    /**
     * Метод, генерирующий уникальные идентификаторы для задач
     *
     * @return id типа int
     */
    private int createId() {
        id++;
        return id;
    }

    public List<Task> getPrioritizedTasks() {
        TreeSet<Task> prioritizedTasks = new TreeSet<>((o1, o2) -> {
            if (o1.getStartTime().get().equals(Task.EMPTY_START_TIME_VALUE)) {
                return 1;
            } else if (o2.getStartTime().get().equals(Task.EMPTY_START_TIME_VALUE)) {
                return -1;
            } else {
                if (o1.getStartTime().get().isBefore(o2.getStartTime().get())) {
                    return -1;
                } else if (o1.getStartTime().get().isAfter(o2.getStartTime().get())) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });
        prioritizedTasks.addAll(getTasks());
        prioritizedTasks.addAll(getSubTasks());
        return new ArrayList<>(prioritizedTasks);
    }

    /**
     * Метод для проверки "незанятости" временного интервала для новой задачи
     *
     * @param start1  - время начала выполнения новой задачи
     * @param start2  - время начала выполнения задачи, с которой ведется сравнение
     * @param finish1 - время окончания выполнения новой задачи
     * @param finish2 - время окончания выполнения задачи, с которой ведется сравнение
     * @return - boolean результат(true or false) --> свободен интервал для новой задачи или же нет
     */
    public boolean validationTaskCompleteInterval(LocalDateTime start1, LocalDateTime start2,
                                                  LocalDateTime finish1, LocalDateTime finish2) {
        if (start1.isAfter(start2) && start1.isBefore(finish2) ||
                finish1.isAfter(start2) && finish1.isBefore(finish2) ||
                start1.isBefore(start2) && finish1.isAfter(finish2) ||
                start1.isEqual(start2) && finish1.isEqual(finish2)) {
            return false;
        } else {
            return true;
        }
    }
}
