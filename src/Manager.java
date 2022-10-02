import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    private int id;
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, SubTask> subTasks;
    private HashMap<Integer, Epic> epics;

    public Manager() {
        id = 0;
        tasks = new HashMap<>();
        subTasks = new HashMap<>();
        epics = new HashMap<>();
    }

    public ArrayList<Task> getTasks() {
        ArrayList<Task> tasksList = new ArrayList<>();
        for (int id : tasks.keySet()) {
            tasksList.add(tasks.get(id));
        }
        return tasksList;
    }

    public ArrayList<SubTask> getSubTasks() {
        ArrayList<SubTask> subTasksList = new ArrayList<>();
        for (int id : subTasks.keySet()) {
            subTasksList.add(subTasks.get(id));
        }
        return subTasksList;
    }

    public ArrayList<Epic> getEpics() {
        ArrayList<Epic> epicList = new ArrayList<>();
        for (int id : epics.keySet()) {
            epicList.add(epics.get(id));
        }
        return epicList;
    }

    public int createId() {
        id++;
        return id;
    }

    /**
     * Метод, создающий задачи
     * методы createEpic и createSubTask выполнен по аналогии
     *
     * @param name    наименование задачи
     * @param details детали задачи
     * @return объект класса Task
     */
    public Task createTask(String name, String details) {
        Task task = new Task(name, details);
        task.setStatus("NEW");
        task.setId(createId());
        tasks.put(task.getId(), task);
        return task;
    }

    public Epic createEpic(String name) {
        Epic epic = new Epic(name, null);
        epic.setStatus("NEW");
        epic.setId(createId());
        epics.put(epic.getId(), epic);
        return epic;
    }

    public SubTask createSubTask(String epicName, String subTaskName) {
        SubTask subTask = new SubTask(subTaskName, null);
        subTask.setStatus("NEW");
        subTask.setId(createId());
        for (int id : epics.keySet()) {
            if (epics.get(id).getName().equals(epicName)) {
                subTasks.put(subTask.getId(), subTask);
                subTask.setEpicId(id);
                epics.get(id).setSubTasks(subTask.getId(), subTask);
            }
        }
        return subTask;
    }

    public void deleteTasks() {
        tasks.clear();
    }

    public void deleteSubTasks() {
        subTasks.clear();
    }

    public void deleteEpics() {
        epics.clear();
        subTasks.clear();
        for (int id : epics.keySet()) {
            epics.get(id).getSubTasks().clear();
        }
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public SubTask getSubTaskById(int id) {
        return subTasks.get(id);
    }

    public void updateTask(Task task, int id) {
        task.setId(id);
        tasks.put(id, task);
    }

    /**
     * метод для обновления SubTasks с логикой проверки +\
     * и изменения статуса эпика
     *
     * @param subTask  объект класса SubTask
     * @param epicName название эпика, к которому принадлежит данный subTask
     * @param id       идентификатор subTask
     */
    public void updateSubTask(SubTask subTask, String epicName, int id) {
        subTask.setId(id);
        subTasks.put(id, subTask);
        for (int uid : epics.keySet()) {
            if (epics.get(uid).getName().equals(epicName)) {
                subTask.setEpicId(uid);
                epics.get(uid).setSubTasks(id, subTask);
                if (subTask.getStatus().equals("IN_PROGRESS")) {
                    epics.get(uid).setStatus("IN_PROGRESS");
                }
            }
        }
        for (int uid : epics.keySet()) {
            int count = 0;
            for (int subId : epics.get(uid).getSubTasks().keySet()) {
                if (epics.get(uid).getSubTasks().get(subId).getStatus().equals("DONE")) {
                    count++;
                }
                if (count == epics.get(uid).getSubTasks().size()) {
                    epics.get(uid).setStatus("DONE");
                }

            }
        }

    }

    public void updateEpic(Epic epic, int id) {
        epic.setId(id);
        epics.put(id, epic);
    }

    public void deleteTaskById(int id) {
        tasks.remove(id);
    }

    public void deleteEpicById(int id) {
        epics.remove(id);
    }

    public void deleteSubTaskById(int id) {
        subTasks.remove(id);
        for (int epicId : epics.keySet()) {
            for (int subId : epics.get(epicId).getSubTasks().keySet()) {
                if (subId == id) {
                    epics.get(epicId).getSubTasks().remove(subId);
                }
            }
        }
    }

    public HashMap getListSubsOfEpic(int epicId) {
        return epics.get(id).getSubTasks();
    }

}
