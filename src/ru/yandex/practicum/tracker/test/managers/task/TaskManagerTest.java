package ru.yandex.practicum.tracker.test.managers.task;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.yandex.practicum.tracker.exception.ValidateStartTimeException;
import ru.yandex.practicum.tracker.model.Epic;
import ru.yandex.practicum.tracker.model.SubTask;
import ru.yandex.practicum.tracker.model.Task;
import ru.yandex.practicum.tracker.model.TaskStatus;
import ru.yandex.practicum.tracker.service.managers.Managers;
import ru.yandex.practicum.tracker.service.managers.task.TaskManager;
import ru.yandex.practicum.tracker.service.managers.task.inMemory.InMemoryTaskManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


public abstract class TaskManagerTest<T extends TaskManager> {
    public TaskManager manager;

    public abstract TaskManager getType();

    @BeforeEach
    public void setup() {
        manager = getType();
    }

    /**
     * Блок тестов по методам getTasks/SubTasks/Epics
     */
    @Test
    public void shouldReturnListOfTasks() {
        Task task1 = manager.createTask("task1", null, 0, "2022-09-01T12:15");
        Task task2 = manager.createTask("task2", null, 0, "2022-08-17T12:19");
        Task task3 = manager.createTask("task3", null, 0, "2022-09-04T12:15");
        Task task4 = manager.createTask("task4", null, 0, "2022-09-11T12:15");
        List<Task> tasksList = List.of(task1, task2, task3, task4);

        assertEquals(tasksList, manager.getTasks());
    }

    @Test
    public void shouldReturnEmptyListWithEmptyTasksList() {
        assertTrue(manager.getTasks().isEmpty());

    }

    @Test
    public void shouldReturnListOfSubTasks() {
        Epic epic = manager.createEpic("Получить права");
        SubTask sub1 = manager.createSubTask("Получить права", "Пройти обучение в автошколе", 1300, "2022-09-01T12:15");
        SubTask sub2 = manager.createSubTask("Получить права", "сдать экзамен", 180, "2022-09-04T12:15");
        SubTask sub3 = manager.createSubTask("Получить права", "отпраздновать", 120, "2022-09-11T12:15");
        List<SubTask> list = List.of(sub1, sub2, sub3);
        assertEquals(sub1.getEpicId(), manager.getSubTasks().get(0).getEpicId(), "У Сабтаски неверный id эпика, к которому она привязана");
        assertEquals(epic.getStatus(), manager.getEpicById(1).getStatus());
        assertEquals(list, manager.getSubTasks(), "Неверно выводится список подзадач");
    }

    @Test
    public void shouldReturnEmptyListWithEmptySubTasksList() {
        assertTrue(manager.getSubTasks().isEmpty());
    }

    @Test
    public void shouldReturnListOfEpics() {
        Epic epic1 = manager.createEpic("Получить права");
        Epic epic2 = manager.createEpic("kakoi-to ewe epic");
        List<Epic> list = List.of(epic1, epic2);
        assertEquals(list, manager.getEpics());
    }

    @Test
    public void shouldReturnEmptyListWithEmptyEpicList() {
        assertTrue(manager.getEpics().isEmpty());
    }

    /**
     * Блок тестов по методам createTask/SubTask/Epic, предназначенных для создания новых задач
     */
    @Test
    public void shouldCreateTask() {
        Task task1 = new Task("Погулять с собакой", "Одеться; Выйти с собакой на улицу", 25, Optional.of("2022-08-25T12:15"));
        task1.setId(1);
        Task task2 = manager.createTask("Погулять с собакой", "Одеться; Выйти с собакой на улицу", 25, "2022-08-25T12:15");

        Task savedTask = manager.getTaskById(task2.getId());

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task1, task2, "Задачи не совпадают.");

        final List<Task> tasks = manager.getTasks();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task2, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    public void shouldCreateEpic() {
        Epic task1 = new Epic("Погулять с собакой", null);
        task1.setId(1);
        Epic task2 = manager.createEpic("Погулять с собакой");

        Epic savedTask = manager.getEpicById(task2.getId());

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task1, task2, "Задачи не совпадают.");

        final List<Epic> tasks = manager.getEpics();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task2, tasks.get(0), "Задачи не совпадают.");
        assertEquals(TaskStatus.NEW, task2.getStatus(), "Статусы не совпадают");

    }

    @Test
    public void shouldCreateSubTask() {
        Epic epic1 = manager.createEpic("Получить права");
        SubTask task1 = new SubTask("Погулять с собакой", null, 25, Optional.of("2022-08-25T12:15"));
        task1.setId(2);
        task1.setEpicId(1);
        SubTask task2 = manager.createSubTask("Получить права", "Погулять с собакой", 25, "2022-08-25T12:15");

        SubTask savedTask = manager.getSubTaskById(task2.getId());

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task1, task2, "Задачи не совпадают.");

        final List<SubTask> tasks = manager.getSubTasks();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task2, tasks.get(0), "Задачи не совпадают.");
        assertEquals(epic1.getId(), task2.getEpicId(), "У подзадачи неверный EpicId");
    }

    /**
     * Блок тестов по методам deleteTasks/SubTasks/Epics
     */
    @Test
    public void shouldDeleteAllTasks() {
        Task task1 = manager.createTask("task1", null, 0, "2022-09-01T12:15");
        Task task2 = manager.createTask("task2", null, 0, "2022-08-17T12:19");
        Task task3 = manager.createTask("task3", null, 0, "2022-09-04T12:15");
        Task task4 = manager.createTask("task4", null, 0, "2022-09-11T12:15");

        List<Task> tasksList = List.of(task1, task2, task3, task4);

        assertEquals(tasksList, manager.getTasks(), "Неверно выводится список задач");
        manager.deleteTasks();
        assertTrue(manager.getTasks().isEmpty(), "Не были удалены все задачи");
    }

    @Test
    public void shouldDeleteAllSubTasks() {
        Epic epic = manager.createEpic("Получить права");
        SubTask sub1 = manager.createSubTask("Получить права", "Пройти обучение в автошколе", 1300, "2022-09-01T12:15");
        sub1.setStatus(TaskStatus.DONE);
        manager.updateSubTask(sub1, epic.getName(), 2);
        SubTask sub2 = manager.createSubTask("Получить права", "сдать экзамен", 180, "2022-09-04T12:15");
        sub2.setStatus(TaskStatus.DONE);
        manager.updateSubTask(sub2, epic.getName(), 3);
        SubTask sub3 = manager.createSubTask("Получить права", "отпраздновать", 120, "2022-09-11T12:15");
        sub3.setStatus(TaskStatus.DONE);
        manager.updateSubTask(sub3, epic.getName(), 4);
        List<SubTask> list = List.of(sub1, sub2, sub3);

        assertEquals(list, manager.getSubTasks(), "Неверно выводится список подзадач");
        manager.deleteSubTasks();
        assertTrue(manager.getSubTasks().isEmpty(), "Не были удалены все подзадачи");
        assertTrue(manager.getEpicById(1).getSubTasks().isEmpty(), "Подзадачи не были удалены из эпика");
        assertEquals(TaskStatus.NEW, epic.getStatus());
    }

    @Test
    public void shouldDeleteAllEpics() {
        Epic epic1 = manager.createEpic("Получить права");
        SubTask sub1 = manager.createSubTask("Получить права", "Пройти обучение в автошколе", 1300, "2022-09-01T12:15");
        SubTask sub2 = manager.createSubTask("Получить права", "сдать экзамен", 180, "2022-09-04T12:15");
        SubTask sub3 = manager.createSubTask("Получить права", "отпраздновать", 120, "2022-09-11T12:15");

        Epic epic2 = manager.createEpic("Приготовить ужин");
        List<Epic> epics = List.of(epic1, epic2);
        assertEquals(epics, manager.getEpics(), "Неверно выводится список эпиков");
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
        Task task1 = manager.createTask("task1", null, 0, "2022-09-01T12:15");
        Task task2 = manager.createTask("task2", null, 0, "2022-08-17T12:19");
        Task task3 = manager.createTask("task3", null, 0, "2022-09-04T12:15");
        Task task4 = manager.createTask("task4", null, 0, "2022-09-11T12:15");
        assertNull(manager.getTaskById(9999));
    }

    @Test
    public void shouldReturnTaskById() {
        Task task1 = manager.createTask("task1", null, 0, "2022-09-01T12:15");
        assertEquals(task1, manager.getTaskById(1), "Задачи не равны");
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
        SubTask sub1 = manager.createSubTask("Получить права", "Пройти обучение в автошколе", 1300, "2022-09-01T12:15");
        assertNull(manager.getSubTaskById(999));
    }

    @Test
    public void shouldReturnSubTaskById() {
        Epic epic1 = manager.createEpic("Получить права");
        SubTask sub1 = manager.createSubTask("Получить права", "Пройти обучение в автошколе", 1300, "2022-09-01T12:15");
        assertEquals(sub1, manager.getSubTaskById(2), "Подзадачи не равны");
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
        Epic epic1 = manager.createEpic("Получить права");
        assertNull(manager.getEpicById(222));
    }

    @Test
    public void shouldReturnEpicById() {
        Epic epic1 = manager.createEpic("Получить права");
        assertEquals(epic1, manager.getEpicById(1), "Эпики не равны");
    }

    /**
     * Блок тестов по методам updateTask,updateSubTask, updateEpic
     * для эпиков дополнительно протестировано изменение статуса при изменении статуса у подзадач
     * для подзадач дополнительно протестировано наличие корректного epicId
     */
    @Test
    public void shouldUpdateTask() {
        Task task1 = manager.createTask("task1", null, 0, "2022-09-01T12:15");
        Task task2 = new Task("task2", null, 0, Optional.of("2022-08-17T12:19"));
        assertNotEquals(task2, manager.getTaskById(1));
        manager.updateTask(task2, 1);
        assertEquals(task2, manager.getTaskById(1));
    }

    @Test
    public void shouldUpdateSubTask() {
        Epic epic = manager.createEpic("Получить права");
        SubTask sub1 = manager.createSubTask("Получить права", "Пройти обучение в автошколе", 1300, "2022-09-01T12:15");
        SubTask sub2 = manager.createSubTask("Получить права", "сдать экзамен", 180, "2022-09-04T12:15");
        SubTask sub3 = manager.createSubTask("Получить права", "отпраздновать", 120, "2022-09-11T12:15");
        sub1.setStatus(TaskStatus.DONE);
        manager.updateSubTask(sub1, "Получить права", 2);
        assertEquals(sub1, manager.getSubTaskById(2), "Некорректно заменена подзадача");
        assertTrue(manager.getEpicById(1).getStatus().equals(TaskStatus.IN_PROGRESS), "Неверно отображается статус эпика");
        sub2.setStatus(TaskStatus.DONE);
        sub3.setStatus(TaskStatus.DONE);
        manager.updateSubTask(sub2, "Получить права", 3);
        manager.updateSubTask(sub3, "Получить права", 4);
        assertTrue(manager.getEpicById(1).getStatus().equals(TaskStatus.DONE), "Неверно отображается статус эпика");
        assertEquals(sub1.getEpicId(), manager.getSubTaskById(2).getEpicId(), "Неверный epicId у подзадачи");
    }

    @Test
    public void shouldUpdateEpic() {
        Epic epic = manager.createEpic("Получить права");
        Epic epic2 = manager.createEpic("Приготовить ужин");
        assertEquals(TaskStatus.NEW, manager.getEpicById(1).getStatus(), "Статусы не совпадают");

        manager.updateEpic(epic2, 1);
        assertEquals(epic2, manager.getEpicById(1), "Эпики не совпадают");
    }

    /**
     * Блок тестов по методам deleteTask/SubTask/EpicById с тестами корректности,
     * хрянащяхся в эпиках подзадач и хранящихся в подзадачах epicId
     */
    @Test
    public void shouldDeleteTaskById() {
        Task task1 = manager.createTask("task1", null, 0, "2022-09-01T12:15");
        assertFalse(manager.getTasks().isEmpty(), "Список задач пуст");
        manager.deleteTaskById(1);
        assertTrue(manager.getTasks().isEmpty(), "Задача не была удалена, список задач не пуст");
    }

    @Test
    public void shouldDeleteSubTaskById() {
        Epic epic = manager.createEpic("Получить права");
        SubTask sub1 = manager.createSubTask("Получить права", "Пройти обучение в автошколе", 1300, "2022-09-01T12:15");
        SubTask sub2 = manager.createSubTask("Получить права", "сдать экзамен", 180, "2022-09-04T12:15");
        SubTask sub3 = manager.createSubTask("Получить права", "отпраздновать", 120, "2022-09-11T12:15");
        assertEquals(3, manager.getEpicById(1).getSubTasks().size(), "Внутри эпика хранятся не все его подзадачи");
        assertEquals(1, manager.getSubTaskById(2).getEpicId(), "У подзадачи неверный epicId");
        manager.deleteSubTaskById(2);
        assertEquals(2, manager.getEpicById(1).getSubTasks().size(), "У эпика не удалилась подзадача");
        assertNull(manager.getSubTaskById(2), "Подзадача не была удалена");
    }

    @Test
    public void shouldDeleteEpicById() {
        Epic epic = manager.createEpic("Получить права");
        SubTask sub1 = manager.createSubTask("Получить права", "Пройти обучение в автошколе", 1300, "2022-09-01T12:15");
        SubTask sub2 = manager.createSubTask("Получить права", "сдать экзамен", 180, "2022-09-04T12:15");
        SubTask sub3 = manager.createSubTask("Получить права", "отпраздновать", 120, "2022-09-11T12:15");
        manager.deleteEpicById(1);
        assertTrue(manager.getEpics().isEmpty(), "Эпик не был удален");
        assertTrue(manager.getSubTaskById(2).getEpicId() == 0, "Информация об epicId не была удалена у его подзадач");
    }

    /**
     * Блок тестов по методу getListSubsOfEpic
     */
    @Test
    public void shouldReturnListSubsOfEpic() {
        Epic epic = manager.createEpic("Получить права");
        SubTask sub1 = manager.createSubTask("Получить права", "Пройти обучение в автошколе", 1300, "2022-09-01T12:15");
        SubTask sub2 = manager.createSubTask("Получить права", "сдать экзамен", 180, "2022-09-04T12:15");
        SubTask sub3 = manager.createSubTask("Получить права", "отпраздновать", 120, "2022-09-11T12:15");
        List<SubTask> list = List.of(sub1, sub2, sub3);
        assertEquals(list, manager.getListSubsOfEpic(1), "Списки не совпадают");
    }

    @Test
    public void shouldReturnEmptyListWithoutSubs() {
        Epic epic = manager.createEpic("Получить права");
        assertTrue(manager.getListSubsOfEpic(1).isEmpty(), "Лист подзадач не пуст");
    }

    /**
     * Блок тестов для метода getHistory
     */
    @Test
    public void shouldReturnHistoryList() {
        Epic epic = manager.createEpic("Получить права");
        SubTask sub1 = manager.createSubTask("Получить права", "Пройти обучение в автошколе", 1300, "2022-09-01T12:15");
        SubTask sub2 = manager.createSubTask("Получить права", "сдать экзамен", 180, "2022-09-04T12:15");
        SubTask sub3 = manager.createSubTask("Получить права", "отпраздновать", 120, "2022-09-11T12:15");
        List<Task> list = List.of(epic, sub1, sub2, sub3);
        manager.getEpicById(1);
        manager.getSubTaskById(2);
        manager.getSubTaskById(3);
        manager.getSubTaskById(4);
        assertEquals(list, manager.getHistory(), "Неверно отображается история просмотров");
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
        Epic epic = manager.createEpic("Получить права");
        SubTask sub1 = manager.createSubTask("Получить права", "Пройти обучение в автошколе", 1300, "2022-09-01T12:15");
        SubTask sub2 = manager.createSubTask("Получить права", "сдать экзамен", 180, "2022-09-04T12:15");
        SubTask sub3 = manager.createSubTask("Получить права", "отпраздновать", 120, "2022-09-11T12:15");
        manager.getEpicById(1);
        manager.getSubTaskById(2);
        manager.getSubTaskById(3);
        manager.getSubTaskById(4);
        assertEquals(manager.getHistory(), manager.getHistoryManager().getHistory(), "Возвращен некорректный экземпляр менеджера истории");
    }

    /**
     * Блок тестов для методов getMapOfTasks/SubTasks/Epics
     */
    @Test
    public void shouldReturnMapOfTasksOrEmptyMap() {
        assertTrue(manager.getMapOfTasks().isEmpty(), "Мапа не пуста");
        Task task1 = manager.createTask("task1", null, 0, "2022-09-01T12:15");
        Task task2 = manager.createTask("task2", null, 0, "2022-08-17T12:19");
        Task task3 = manager.createTask("task3", null, 0, "2022-09-04T12:15");
        Task task4 = manager.createTask("task4", null, 0, "2022-09-11T12:15");
        assertTrue(manager.getMapOfTasks().size() == 4);
    }

    @Test
    public void shouldReturnMapOfSubTasksOrEmptyMap() {
        assertTrue(manager.getMapOfSubTasks().isEmpty(), "Мапа не пуста");
        Epic epic = manager.createEpic("Получить права");
        SubTask sub1 = manager.createSubTask("Получить права", "Пройти обучение в автошколе", 1300, "2022-09-01T12:15");
        SubTask sub2 = manager.createSubTask("Получить права", "сдать экзамен", 180, "2022-09-04T12:15");
        SubTask sub3 = manager.createSubTask("Получить права", "отпраздновать", 120, "2022-09-11T12:15");
        assertTrue(manager.getMapOfSubTasks().size() == 3);
    }

    @Test
    public void shouldReturnMapOfEpicsOrEmptyMap() {
        assertTrue(manager.getMapOfEpics().isEmpty(), "Мапа не пуста");
        Epic epic = manager.createEpic("Получить права");
        assertTrue(manager.getMapOfEpics().size() == 1);

    }

    /**
     * Тесты для полей startTime, endTime, Duration и их рассчетов
     */
    @Test
    public void shouldWorkCorrectlyWithStartTimeAndDurationAndEndTime() {
        Task task1 = manager.createTask("Погулять с собакой", "Одеться; Выйти с собакой на улицу", 25, "2022-08-25T12:15");
        assertEquals("2022-08-25T12:40", manager.getTaskById(1).getEndTime().get().toString());

        final ValidateStartTimeException exception = assertThrows(
                ValidateStartTimeException.class,
                () -> {
                    manager.createTask("Купить продукты", "Написать список продуктов; Пойти в магазин", 60, "2022-08-25T12:19");
                }
        );

        Epic epic1 = manager.createEpic("Получить права");
        SubTask sub1 = manager.createSubTask("Получить права", "Пройти обучение в автошколе", 1300, "2022-09-01T12:15");
        SubTask sub2 = manager.createSubTask("Получить права", "сдать экзамен", 180, "2022-09-04T12:15");
        SubTask sub3 = manager.createSubTask("Получить права", "отпраздновать", 120, "2022-09-11T12:15");
        assertEquals("2022-09-01T12:15", manager.getEpicById(2).getStartTime().get().toString());
        assertEquals("2022-09-11T14:15", manager.getEpicById(2).getEndTime().get().toString());
        assertEquals(1600, manager.getEpicById(2).getDuration().toMinutes());

        Epic epic2 = manager.createEpic("Приготовить ужин");
        assertEquals(Task.EMPTY_START_TIME_VALUE, manager.getEpicById(6).getStartTime().get());
        assertEquals(Task.EMPTY_START_TIME_VALUE, manager.getEpicById(6).getEndTime().get());
        assertEquals(0, manager.getEpicById(6).getDuration().toMinutes());
    }
}