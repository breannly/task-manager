package entity;

import controller.exception.ManagerSaveException;
import controller.imanager.TaskManager;
import controller.utility.Managers;
import model.entity.Epic;
import model.entity.Subtask;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EpicTest {
    private static TaskManager manager;
    private static Epic testEpic;

    @BeforeEach
    public void beforeAll() throws ManagerSaveException {
        manager = Managers.getDefault();
        testEpic = new Epic("test", "test");
        manager.addEpic(testEpic);
    }

    @Test
    public void shouldReturnNEWStatusWhenNoOneSubtasks() {
        Assertions.assertEquals("NEW", testEpic.getStatus());
    }

    @Test
    public void shouldReturnNEWStatusWhenAllSubtasksStatusAreNEW() throws ManagerSaveException {
        Subtask testSubtask1 = new Subtask("test", "test", "NEW", testEpic.getId());
        Subtask testSubtask2 = new Subtask("test", "test", "NEW", testEpic.getId());
        Subtask testSubtask3 = new Subtask("test", "test", "NEW", testEpic.getId());
        manager.addSubtask(testSubtask1);
        manager.addSubtask(testSubtask2);
        manager.addSubtask(testSubtask3);

        Assertions.assertEquals("NEW", testEpic.getStatus());
    }

    @Test
    public void shouldReturnDONEStatusWhenAllSubtasksStatusAreDONE() throws ManagerSaveException {
        Subtask testSubtask1 = new Subtask("test", "test", "DONE", testEpic.getId());
        Subtask testSubtask2 = new Subtask("test", "test", "DONE", testEpic.getId());
        Subtask testSubtask3 = new Subtask("test", "test", "DONE", testEpic.getId());
        manager.addSubtask(testSubtask1);
        manager.addSubtask(testSubtask2);
        manager.addSubtask(testSubtask3);

        Assertions.assertEquals("DONE", testEpic.getStatus());
    }

    @Test
    public void shouldReturnINPROGRESSStatusWhenAllSubtasksStatusAreINPROGRESS() throws ManagerSaveException {
        Subtask testSubtask1 = new Subtask("test", "test", "IN_PROGRESS", testEpic.getId());
        Subtask testSubtask2 = new Subtask("test", "test", "IN_PROGRESS", testEpic.getId());
        Subtask testSubtask3 = new Subtask("test", "test", "IN_PROGRESS", testEpic.getId());
        manager.addSubtask(testSubtask1);
        manager.addSubtask(testSubtask2);
        manager.addSubtask(testSubtask3);

        Assertions.assertEquals("IN_PROGRESS", testEpic.getStatus());
    }

    @Test
    public void shouldReturnINPROGRESSSTATUSWhenAllSubtasksStatusAreNEWAndDONE() throws ManagerSaveException {
        Subtask testSubtask1 = new Subtask("test", "test", "NEW", testEpic.getId());
        Subtask testSubtask2 = new Subtask("test", "test", "DONE", testEpic.getId());
        Subtask testSubtask3 = new Subtask("test", "test", "DONE", testEpic.getId());
        manager.addSubtask(testSubtask1);
        manager.addSubtask(testSubtask2);
        manager.addSubtask(testSubtask3);

        Assertions.assertEquals("IN_PROGRESS", testEpic.getStatus());
    }
}