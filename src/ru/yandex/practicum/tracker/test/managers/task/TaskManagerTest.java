package managers.task;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.tracker.exception.ValidateStartTimeException;
import ru.yandex.practicum.tracker.model.Epic;
import ru.yandex.practicum.tracker.model.SubTask;
import ru.yandex.practicum.tracker.model.Task;
import ru.yandex.practicum.tracker.model.TaskStatus;
import ru.yandex.practicum.tracker.service.managers.task.TaskManager;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


public abstract class TaskManagerTest<T extends TaskManager> {
    public T manager;

    public abstract void getType();

    public void initTask(){
        manager.createTask("task1", null, 0, "2022-09-01T12:15");
        manager.createTask("task2", null, 0, "2022-08-25T12:19");
        manager.createTask("task3", null, 0, "2022-09-04T12:15");
        manager.createEpic("Получить права");
        manager.createSubTask("Получить права", "Пройти обучение в автошколе", 1300, "2022-09-03T12:15");
        manager.createSubTask("Получить права", "сдать экзамен", 180, "2022-09-07T12:15");
        manager.createSubTask("Получить права", "отпраздновать", 120, "2022-09-11T12:15");
        manager.createEpic("kakoi-to ewe epic");
    }

    @BeforeEach
    public void setup() {
        getType();
        initTask();
    }

    /**
     * Блок тестов по методам getTasks/SubTasks/Epics
     */
    @Test
    public void shouldReturnListOfTasks() {
        List<Task> tasksList = List.of(manager.getTaskById(1),manager.getTaskById(2),manager.getTaskById(3));
        assertEquals(tasksList, manager.getTasks());
    }

    @Test
    public void shouldReturnEmptyListWithEmptyTasksList() {
        manager.deleteTasks();
        assertTrue(manager.getTasks().isEmpty());

    }

    @Test
    public void shouldReturnListOfSubTasks() {
        List<SubTask> list = List.of(manager.getSubTaskById(5),manager.getSubTaskById(6), manager.getSubTaskById(7));
        assertEquals(4, manager.getSubTasks().get(0).getEpicId(), "У Сабтаски неверный id эпика, к которому она привязана");
        assertEquals(TaskStatus.NEW,manager.getEpicById(4).getStatus());
        assertEquals(list, manager.getSubTasks(), "Неверно выводится список подзадач");
    }

    @Test
    public void shouldReturnEmptyListWithEmptySubTasksList() {
        manager.deleteSubTasks();
        assertTrue(manager.getSubTasks().isEmpty());
    }

    @Test
    public void shouldReturnListOfEpics() {
        List<Epic> list = List.of(manager.getEpicById(4),manager.getEpicById(8));
        assertEquals(list, manager.getEpics());
    }

    @Test
    public void shouldReturnEmptyListWithEmptyEpicList() {
        manager.deleteEpics();
        assertTrue(manager.getEpics().isEmpty());
    }

    /**
     * Блок тестов по методам createTask/SubTask/Epic, предназначенных для создания новых задач
     */
    @Test
    public void shouldCreateTask() {
        Task task1 = new Task("task1", null, 0, Optional.of("2022-09-01T12:15"));
        task1.setId(1);
        assertNotNull(manager.getTaskById(1), "Задача не найдена.");
        assertEquals(task1, manager.getTaskById(1), "Задачи не совпадают.");

        final List<Task> tasks = manager.getTasks();
        assertNotNull(tasks, "Задачи на возвращаются.");
    }

    @Test
    public void shouldCreateEpic() {
        assertNotNull(manager.getEpicById(4), "Задача не найдена.");

        final List<Epic> tasks = manager.getEpics();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(manager.getEpicById(4), tasks.get(0), "Задачи не совпадают.");
        assertEquals(TaskStatus.NEW, manager.getEpicById(4).getStatus(), "Статусы не совпадают");

    }

    @Test
    public void shouldCreateSubTask() {
        assertNotNull(manager.getSubTaskById(5), "Задача не найдена.");
        final List<SubTask> tasks = manager.getSubTasks();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(3, tasks.size(), "Неверное количество задач.");
        assertEquals(4, manager.getSubTaskById(5).getEpicId(), "У подзадачи неверный EpicId");
    }

    /**
     * Блок тестов по методам deleteTasks/SubTasks/Epics
     */
    @Test
    public void shouldDeleteAllTasks() {

        List<Task> tasksList = List.of(manager.getTaskById(1),manager.getTaskById(2),manager.getTaskById(3));

        assertEquals(tasksList, manager.getTasks(), "Неверно выводится список задач");
        manager.deleteTasks();
        assertTrue(manager.getTasks().isEmpty(), "Не были удалены все задачи");
    }

    @Test
    public void shouldDeleteAllSubTasks() {
        manager.getSubTaskById(5).setStatus(TaskStatus.DONE);
        manager.updateSubTask(manager.getSubTaskById(5), "Получить права", 5);
        manager.getSubTaskById(6).setStatus(TaskStatus.DONE);
        manager.updateSubTask(manager.getSubTaskById(6), "Получить права", 6);
        manager.getSubTaskById(7).setStatus(TaskStatus.DONE);
        manager.updateSubTask(manager.getSubTaskById(7), "Получить права", 7);

        assertFalse(manager.getSubTasks().isEmpty(), "Список подзадач пуст");
        manager.deleteSubTasks();
        assertTrue(manager.getSubTasks().isEmpty(), "Не были удалены все подзадачи");
        assertTrue(manager.getEpicById(4).getSubTasks().isEmpty(), "Подзадачи не были удалены из эпика");
        assertEquals(TaskStatus.NEW, manager.getEpicById(4).getStatus());
    }

    @Test
    public void shouldDeleteAllEpics() {
        assertFalse(manager.getEpics().isEmpty(), "Список эпиков пуст");
        manager.deleteEpics();
        assertTrue(manager.getEpics().isEmpty(), "Не были удалены все эпики");
        for (SubTask task : manager.getSubTasks()) {
            assertTrue(task.getEpicId() == 0, "Не у всех подзадач были сброшены epicId");
        }
    }

    /**
     * Блок тестов метода getTaskById()
     * в тесте shouldReturnNullWithNonExistentTaskId() представлены тесты с пустым
     * списком задач и с несуществующим идентификатором;
     * <p>
     * в тесте shouldReturnTaskById() представлен тест с нормальным поведением
     */
    @Test
    public void shouldReturnNullWithNonExistentTaskId() {
        assertNull(manager.getTaskById(222));
        assertNull(manager.getTaskById(9999));
    }

    @Test
    public void shouldReturnTaskById() {

        assertNotNull(manager.getTaskById(1), "Задачи не равны");
    }

    /**
     * Блок тестов метода getSubTaskById()
     * в тесте shouldReturnNullWithNonExistentSubTaskId() представлены тесты с пустым
     * списком задач и с несуществующим идентификатором;
     * <p>
     * в тесте shouldReturnSubTaskById() представлен тест с нормальным поведением
     */
    @Test
    public void shouldReturnNullWithNonExistentSubTaskId() {
        assertNull(manager.getSubTaskById(222));
        assertNull(manager.getSubTaskById(999));
    }

    @Test
    public void shouldReturnSubTaskById() {
        assertNotNull(manager.getSubTaskById(5), "Подзадача не была создана");
    }

    /**
     * Блок тестов метода getEpicById()
     * в тесте shouldReturnNullWithNonExistentEpicId() представлены тесты с пустым
     * списком задач и с несуществующим идентификатором;
     * <p>
     * в тесте shouldReturnEpicById() представлен тест с нормальным поведением
     */
    @Test
    public void shouldReturnNullWithNonExistentEpicId() {
        assertNull(manager.getEpicById(1945));
    }

    @Test
    public void shouldReturnEpicById() {
        assertNotNull(manager.getEpicById(4), "Эпик не был создан");
    }

    /**
     * Блок тестов по методам updateTask,updateSubTask, updateEpic
     * для эпиков дополнительно протестировано изменение статуса при изменении статуса у подзадач
     * для подзадач дополнительно протестировано наличие корректного epicId
     */
    @Test
    public void shouldUpdateTask() {
        Task task2 = new Task("task2", null, 0, Optional.of("2022-08-17T12:19"));
        assertNotEquals(task2, manager.getTaskById(1));
        manager.updateTask(task2, 1);
        assertEquals(task2, manager.getTaskById(1));
    }

    @Test
    public void shouldUpdateSubTask() {
        manager.getSubTaskById(5).setStatus(TaskStatus.DONE);
        manager.updateSubTask(manager.getSubTaskById(5), "Получить права", 5);
        assertTrue(manager.getEpicById(4).getStatus().equals(TaskStatus.IN_PROGRESS), "Неверно отображается статус эпика");
        manager.getSubTaskById(6).setStatus(TaskStatus.DONE);
        manager.getSubTaskById(7).setStatus(TaskStatus.DONE);
        manager.updateSubTask(manager.getSubTaskById(6), "Получить права", 6);
        manager.updateSubTask(manager.getSubTaskById(7), "Получить права", 7);
        assertTrue(manager.getEpicById(4).getStatus().equals(TaskStatus.DONE), "Неверно отображается статус эпика");
        assertEquals(4, manager.getSubTaskById(5).getEpicId(), "Неверный epicId у подзадачи");
    }

    @Test
    public void shouldUpdateEpic() {
        assertEquals(TaskStatus.NEW, manager.getEpicById(4).getStatus(), "Статусы не совпадают");
        manager.getEpicById(4).setStatus(TaskStatus.DONE);
        manager.updateEpic(manager.getEpicById(4), 4);
        assertEquals(TaskStatus.DONE, manager.getEpicById(4).getStatus(), "Обновление произведено некорректно");
    }

    /**
     * Блок тестов по методам deleteTask/SubTask/EpicById с тестами корректности,
     * хрянащяхся в эпиках подзадач и хранящихся в подзадачах epicId
     */
    @Test
    public void shouldDeleteTaskById() {
        assertFalse(manager.getTasks().isEmpty(), "Список задач пуст");
        manager.deleteTaskById(1);
        assertNull(manager.getTaskById(1), "Задача не была удалена, список задач не пуст");
    }

    @Test
    public void shouldDeleteSubTaskById() {
        assertEquals(3, manager.getEpicById(4).getSubTasks().size(), "Внутри эпика хранятся не все его подзадачи");
        assertEquals(4, manager.getSubTaskById(5).getEpicId(), "У подзадачи неверный epicId");
        manager.deleteSubTaskById(5);
        assertEquals(2, manager.getEpicById(4).getSubTasks().size(), "У эпика не удалилась подзадача");
        assertNull(manager.getSubTaskById(5), "Подзадача не была удалена");
    }

    @Test
    public void shouldDeleteEpicById() {
        manager.deleteEpicById(4);
        assertTrue(manager.getSubTaskById(5).getEpicId() == 0, "Информация об epicId не была удалена у его подзадач");
    }

    /**
     * Блок тестов по методу getListSubsOfEpic
     */
    @Test
    public void shouldReturnListSubsOfEpic() {
        List<SubTask> list = List.of(manager.getSubTaskById(5), manager.getSubTaskById(6), manager.getSubTaskById(7));
        assertEquals(list, manager.getListSubsOfEpic(4), "Списки не совпадают");
    }

    @Test
    public void shouldReturnEmptyListWithoutSubs() {
        manager.deleteSubTasks();
        assertTrue(manager.getListSubsOfEpic(4).isEmpty(), "Лист подзадач не пуст");
    }

    /**
     * Блок тестов для метода getHistory
     */
    @Test
    public void shouldReturnHistoryList() {
        manager.getEpicById(4);
        manager.getSubTaskById(5);
        manager.getSubTaskById(6);
        manager.getSubTaskById(7);
        assertFalse(manager.getHistory().isEmpty(), "Неверно отображается история просмотров");
    }

    @Test
    public void shouldReturnEmptyList() {
        assertTrue(manager.getHistory().isEmpty(), "HistoryList не пуст");
    }

    /**
     * Тест для метода getHistoryManager
     */
    @Test
    public void shouldReturnHistoryManager() {
        manager.getEpicById(4);
        manager.getSubTaskById(5);
        manager.getSubTaskById(7);
        manager.getSubTaskById(6);
        assertEquals(manager.getHistory(), manager.getHistoryManager().getHistory(), "Возвращен некорректный экземпляр менеджера истории");
    }

    /**
     * Блок тестов для методов getMapOfTasks/SubTasks/Epics
     */
    @Test
    public void shouldReturnMapOfTasksOrEmptyMap() {
        assertFalse(manager.getMapOfTasks().isEmpty());
        manager.deleteTasks();
        assertTrue(manager.getMapOfTasks().isEmpty());
    }

    @Test
    public void shouldReturnMapOfSubTasksOrEmptyMap() {
        assertFalse(manager.getMapOfSubTasks().isEmpty());
        manager.deleteSubTasks();
        assertTrue(manager.getMapOfSubTasks().isEmpty());
    }

    @Test
    public void shouldReturnMapOfEpicsOrEmptyMap() {
        assertFalse(manager.getMapOfEpics().isEmpty());
        manager.deleteEpics();
        assertTrue(manager.getMapOfEpics().isEmpty());
    }

    /**
     * Тесты для полей startTime, endTime, Duration и их рассчетов
     */
    @Test
    public void shouldWorkCorrectlyWithStartTimeAndDurationAndEndTime() {
        assertEquals("2022-09-01T12:15", manager.getTaskById(1).getEndTime().get().toString());

        final ValidateStartTimeException exception = assertThrows(
                ValidateStartTimeException.class,
                () -> {
                    manager.createTask("Купить продукты", "Написать список продуктов; Пойти в магазин", 60, "2022-08-25T12:15");
                }
        );

        assertEquals("2022-09-03T12:15", manager.getEpicById(4).getStartTime().get().toString());
        assertEquals("2022-09-11T14:15", manager.getEpicById(4).getEndTime().get().toString());
        assertEquals(1600, manager.getEpicById(4).getDuration().toMinutes());

        assertEquals(Task.EMPTY_START_TIME_VALUE, manager.getEpicById(8).getStartTime().get());
        assertEquals(Task.EMPTY_START_TIME_VALUE, manager.getEpicById(8).getEndTime().get());
        assertEquals(0, manager.getEpicById(8).getDuration().toMinutes());
    }
}