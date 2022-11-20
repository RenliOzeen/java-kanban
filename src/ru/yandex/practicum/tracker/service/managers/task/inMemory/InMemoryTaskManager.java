package ru.yandex.practicum.tracker.service.managers.task.inMemory;

import ru.yandex.practicum.tracker.model.Epic;
import ru.yandex.practicum.tracker.model.SubTask;
import ru.yandex.practicum.tracker.model.Task;
import ru.yandex.practicum.tracker.model.TaskStatus;
import ru.yandex.practicum.tracker.service.managers.Managers;
import ru.yandex.practicum.tracker.service.managers.history.HistoryManager;
import ru.yandex.practicum.tracker.service.managers.task.TaskManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class InMemoryTaskManager implements TaskManager {
    private int id;
    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, SubTask> subTasks;
    private final HashMap<Integer, Epic> epics;
    private final HistoryManager historyManager;

    public InMemoryTaskManager() {
        id = 0;
        tasks = new HashMap<>();
        subTasks = new HashMap<>();
        epics = new HashMap<>();
        historyManager = Managers.getDefaultHistory();
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
    public Task createTask(String name, String details) {
        Task task = new Task(name, details);
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
    public SubTask createSubTask(String epicName, String subTaskName) {
        SubTask subTask = new SubTask(subTaskName, null);
        subTask.setId(createId());
        for (int id : epics.keySet()) {
            if (epics.get(id).getName().equals(epicName)) {
                subTasks.put(subTask.getId(), subTask);
                subTask.setEpicId(id);
                epics.get(id).setSubTasks(subTask.getId());
            }
        }
        return subTask;
    }

    @Override
    public void deleteTasks() {
        tasks.clear();
    }

    @Override
    public void deleteSubTasks() {
        subTasks.clear();
    }

    @Override
    public void deleteEpics() {
        epics.clear();
        subTasks.clear();
        for (int id : epics.keySet()) {
            epics.get(id).getSubTasks().clear();
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
        task.setId(id);
        tasks.put(id, task);
    }

    @Override
    public void updateSubTask(SubTask subTask, String epicName, int id) {
        subTask.setId(id);
        subTasks.put(id, subTask);
        for (int uid : epics.keySet()) {
            if (epics.get(uid).getName().equals(epicName)) {
                subTask.setEpicId(uid);
                epics.get(uid).setSubTasks(id);
                if (subTask.getStatus() == TaskStatus.IN_PROGRESS) {
                    epics.get(uid).setStatus(TaskStatus.IN_PROGRESS);
                }
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
            historyManager.remove(num);
        }
        epics.remove(id);
    }

    @Override
    public void deleteSubTaskById(int id) {
        subTasks.remove(id);
        historyManager.remove(id);
        epics.get(subTasks.get(id).getEpicId()).getSubTasks().remove(id);
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


}
