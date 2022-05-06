package model.entity;

import controller.exception.FormatException;
import controller.exception.IntersectionTimeException;
import controller.imanager.TaskManager;
import controller.manager.InMemoryTaskManager;
import controller.utility.Managers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

class TaskTest {
    private static TaskManager manager;

    @BeforeEach
    void beforeEach() {
        manager = new InMemoryTaskManager();
    }

    @Test
    void shouldThrowFormatExceptionWhenIncorrectFormatStartTime() throws FormatException {
        final FormatException exception = Assertions.assertThrows(
                FormatException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        Task task = new Task("test", "test", "NEW", 0, " ");
                    }
                }
        );

        Assertions.assertEquals("Неверный формат данных", exception.getMessage());
    }

    @Test
    void shouldThrowIntersectionTimeExceptionWhenAddIntersectionByTime() throws IntersectionTimeException {
        final IntersectionTimeException exception = Assertions.assertThrows(
                IntersectionTimeException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        Task task = new Task("test",
                                "test",
                                "NEW",
                                20,
                                "29.04.22 12:00");
                        manager.addTask(task);
                        Epic epic = new Epic("test", "test");
                        manager.addEpic(epic);
                        Subtask subtask = new Subtask("test",
                                "test",
                                "NEW",
                                epic.getId(),
                                30,
                                "29.04.22 12:19");
                        manager.addSubtask(subtask);
                    }
                }
        );

        Assertions.assertEquals("Задача с такой продолжительностью уже есть", exception.getMessage());
    }

    @Test
    void shouldThrowIntersectionTimeExceptionWhenUpdateIntersectionByTime() throws IntersectionTimeException {
        final IntersectionTimeException exception = Assertions.assertThrows(
                IntersectionTimeException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        Task task = new Task("test",
                                "test",
                                "NEW",
                                20,
                                "29.04.22 12:00");
                        manager.addTask(task);
                        Epic epic = new Epic("test", "test");
                        manager.addEpic(epic);
                        Subtask subtask = new Subtask("test",
                                "test",
                                "NEW",
                                epic.getId(),
                                20,
                                "29.04.22 11:30");
                        manager.addSubtask(subtask);
                        subtask.setStartTime("29.04.22 12:19");
                        manager.updateSubtask(subtask);
                    }
                }
        );

        Assertions.assertEquals("Задача с такой продолжительностью уже есть", exception.getMessage());
    }
}