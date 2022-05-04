package model.entity;

import controller.exception.FormatException;
import controller.exception.IntersectionTimeException;
import controller.exception.ManagerSaveException;
import controller.imanager.TaskManager;
import controller.utility.Managers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EpicTest {
    private static TaskManager manager;
    private static Epic testEpic;

    @BeforeEach
    void beforeAll() throws ManagerSaveException, IntersectionTimeException {
        manager = Managers.getDefault();
        testEpic = new Epic("test", "test");
        manager.addEpic(testEpic);
    }

    @Test
    void shouldReturnNEWStatusWhenNoOneSubtasks() {
        Assertions.assertEquals("NEW", testEpic.getStatus());
    }

    @Test
    void shouldReturnNEWStatusWhenAllSubtasksStatusAreNEW()
            throws ManagerSaveException, IntersectionTimeException, FormatException {
        Subtask testSubtask1 = new Subtask("test",
                "test",
                "NEW",
                testEpic.getId(),
                0,
                null);
        Subtask testSubtask2 = new Subtask("test",
                "test",
                "NEW",
                testEpic.getId(),
                0,
                null);
        Subtask testSubtask3 = new Subtask("test",
                "test",
                "NEW",
                testEpic.getId(),
                0,
                null);
        manager.addSubtask(testSubtask1);
        manager.addSubtask(testSubtask2);
        manager.addSubtask(testSubtask3);

        Assertions.assertEquals("NEW", testEpic.getStatus());
    }

    @Test
    void shouldReturnDONEStatusWhenAllSubtasksStatusAreDONE()
            throws ManagerSaveException, IntersectionTimeException, FormatException {
        Subtask testSubtask1 = new Subtask("test",
                "test",
                "DONE",
                testEpic.getId(),
                0,
                null);
        Subtask testSubtask2 = new Subtask("test",
                "test",
                "DONE",
                testEpic.getId(),
                0,
                null);
        Subtask testSubtask3 = new Subtask("test",
                "test",
                "DONE",
                testEpic.getId(),
                0,
                null);
        manager.addSubtask(testSubtask1);
        manager.addSubtask(testSubtask2);
        manager.addSubtask(testSubtask3);

        Assertions.assertEquals("DONE", testEpic.getStatus());
    }

    @Test
    void shouldReturnINPROGRESSStatusWhenAllSubtasksStatusAreINPROGRESS()
            throws ManagerSaveException, IntersectionTimeException, FormatException {
        Subtask testSubtask1 = new Subtask("test",
                "test",
                "IN_PROGRESS",
                testEpic.getId(),
                0,
                null);
        Subtask testSubtask2 = new Subtask("test",
                "test",
                "IN_PROGRESS",
                testEpic.getId(),
                0,
                null);
        Subtask testSubtask3 = new Subtask("test",
                "test",
                "IN_PROGRESS",
                testEpic.getId(),
                0,
                null);
        manager.addSubtask(testSubtask1);
        manager.addSubtask(testSubtask2);
        manager.addSubtask(testSubtask3);

        Assertions.assertEquals("IN_PROGRESS", testEpic.getStatus());
    }

    @Test
    void shouldReturnINPROGRESSSTATUSWhenAllSubtasksStatusAreNEWAndDONE()
            throws ManagerSaveException, IntersectionTimeException, FormatException {
        Subtask testSubtask1 = new Subtask("test",
                "test",
                "NEW",
                testEpic.getId(),
                0,
                null);
        Subtask testSubtask2 = new Subtask("test",
                "test",
                "DONE",
                testEpic.getId(),
                0,
                null);
        Subtask testSubtask3 = new Subtask("test",
                "test",
                "DONE",
                testEpic.getId(),
                0,
                null);
        manager.addSubtask(testSubtask1);
        manager.addSubtask(testSubtask2);
        manager.addSubtask(testSubtask3);

        Assertions.assertEquals("IN_PROGRESS", testEpic.getStatus());
    }
}