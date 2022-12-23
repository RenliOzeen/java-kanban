package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.tracker.model.Epic;
import ru.yandex.practicum.tracker.model.SubTask;
import ru.yandex.practicum.tracker.model.TaskStatus;
import ru.yandex.practicum.tracker.service.managers.Managers;
import ru.yandex.practicum.tracker.service.managers.task.TaskManager;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    Epic epic;
    SubTask sub1;
    SubTask sub2;
    SubTask sub3;
    TaskManager manager = Managers.getDefault();

    @BeforeEach
    public void setup() {
        epic = manager.createEpic("name");
        sub1 = manager.createSubTask("name", "subName", 1300, "2022-09-01T12:15");
        sub2 = manager.createSubTask("name", "subName2", 180, "2022-09-04T12:15");
        sub3 = manager.createSubTask("name", "subName3", 120, "2022-09-11T12:15");

    }

    /**
     * Данный блок тестов проверяет корректность рассчета и изменения статуса у Epic при
     * различных граничных условиях
     */
    @Test
    public void shouldBeNewWithoutSubTasks() {
        Assertions.assertEquals(TaskStatus.NEW, epic.getStatus());
    }

    @Test
    public void shouldBeNewWithAllSubTasksIsNew() {
        Assertions.assertEquals(TaskStatus.NEW, epic.getStatus());
    }

    @Test
    public void shouldBeDoneWithAllSubTasksIsDone() {
        sub1.setStatus(TaskStatus.DONE);
        manager.updateSubTask(sub1, epic.getName(), 2);
        sub2.setStatus(TaskStatus.DONE);
        manager.updateSubTask(sub2, epic.getName(), 3);
        sub3.setStatus(TaskStatus.DONE);
        manager.updateSubTask(sub3, epic.getName(), 4);
        Assertions.assertEquals(TaskStatus.DONE, epic.getStatus());
    }

    @Test
    public void shouldBeInProgressWithSubTasksIsNewAndDone() {
        sub1.setStatus(TaskStatus.NEW);
        manager.updateSubTask(sub1, epic.getName(), 2);
        sub2.setStatus(TaskStatus.DONE);
        manager.updateSubTask(sub2, epic.getName(), 3);
        Assertions.assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus());
    }

    @Test
    public void shouldBeInProgressWithSubTasksIsInProgress() {
        sub1.setStatus(TaskStatus.IN_PROGRESS);
        manager.updateSubTask(sub1, epic.getName(), 2);
        sub2.setStatus(TaskStatus.IN_PROGRESS);
        manager.updateSubTask(sub2, epic.getName(), 3);
        sub3.setStatus(TaskStatus.IN_PROGRESS);
        manager.updateSubTask(sub3, epic.getName(), 4);
        Assertions.assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus());
    }

}