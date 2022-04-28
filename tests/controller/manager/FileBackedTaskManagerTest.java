package controller.manager;

import controller.exception.FormatException;
import controller.exception.IntersectionTimeException;
import controller.exception.ManagerSaveException;
import controller.imanager.TaskManager;
import controller.imanager.TaskManagerTest;
import controller.utility.ReaderFile;
import model.entity.Epic;
import model.entity.Subtask;
import model.entity.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {
    File file = new File("test.csv");

    @Test
    public void shouldSaveTasksAndHistoryToFile()
            throws ManagerSaveException, IntersectionTimeException, FormatException {
        TaskManager manager = new FileBackedTaskManager(file);
        Task task = new Task("test", "test","NEW", null, null);
        manager.addTask(task);
        Epic epic = new Epic("test", "test");
        manager.addEpic(epic);
        Subtask subtask = new Subtask("test",
                "test",
                "NEW",
                epic.getId(),
                null,
                null);
        manager.addSubtask(subtask);
        manager.getTaskById(task.getId());
        manager.getSubtaskById(subtask.getId());
        String expected =
                "id,type,name,status,description,epic,duration,startTime,endTime\n" +
                "1,TASK,test,NEW,test,null,null,null\n" +
                "2,EPIC,test,NEW,test,null,null,null\n" +
                "3,SUBTASK,test,NEW,test,2,null,null,null\n" +
                "\n" +
                "1,3";
        String actual = ReaderFile.readFileContents(file);

        Assertions.assertTrue(file.exists(), "Файл не создался");
        Assertions.assertEquals(expected, actual, "Значения не совпадают");
        Assertions.assertEquals(2,manager.getHistory().size(), "Значения не совпадают");
        Assertions.assertTrue(file.delete(), "Файл не удалился");
    }

    @Test
    public void shouldSaveTasksToFileWithoutHistoryToFile()
            throws ManagerSaveException, IntersectionTimeException, FormatException {
        TaskManager manager = new FileBackedTaskManager(file);
        Task task = new Task("test", "test", "NEW", null, null);
        manager.addTask(task);
        String expected =
                "id,type,name,status,description,epic,duration,startTime,endTime\n" +
                "1,TASK,test,NEW,test,null,null,null\n" +
                "\n" +
                " ";

        String actual = ReaderFile.readFileContents(file);

        Assertions.assertTrue(file.exists(), "Файл не создался");
        Assertions.assertEquals(expected, actual, "Значения не совпадают");
        Assertions.assertTrue(file.delete(), "Файл не удалился");
    }

    @Test
    public void shouldSaveEpicWithoutSubtasksToFile() throws ManagerSaveException, IntersectionTimeException {
        TaskManager manager = new FileBackedTaskManager(file);
        Epic epic = new Epic("test", "test");
        manager.addEpic(epic);
        String expected =
                "id,type,name,status,description,epic,duration,startTime,endTime\n" +
                "1,EPIC,test,NEW,test,null,null,null\n" +
                "\n" +
                " ";
        String actual = ReaderFile.readFileContents(file);

        Assertions.assertTrue(file.exists(), "Файл не создался");
        Assertions.assertEquals(expected, actual, "Значения не совпадают");
        Assertions.assertTrue(file.delete(), "Файл не удалился");
    }

    @Test
    public void shouldLoadTasksAndHistoryFromFile()
            throws ManagerSaveException, IntersectionTimeException, FormatException {
        TaskManager generalManager = new FileBackedTaskManager(file);
        Task task = new Task("test", "test","NEW",null,null);
        generalManager.addTask(task);
        Epic epic = new Epic("test", "test");
        generalManager.addEpic(epic);
        Subtask subtask1 = new Subtask("test",
                "test",
                "DONE",
                epic.getId(),
                null,
                null);
        Subtask subtask2 = new Subtask("test",
                "test",
                "IN_PROGRESS",
                epic.getId(),
                null,
                null);
        generalManager.addSubtask(subtask1);
        generalManager.addSubtask(subtask2);
        generalManager.getTaskById(task.getId());
        TaskManager manager = FileBackedTaskManager.loadFromFile(file);

        Assertions.assertTrue(file.exists(), "Файл не создался");
        Assertions.assertEquals(task, manager.getTasksList().get(0), "Task не восстановился");
        Assertions.assertEquals(epic, manager.getEpicsList().get(0), "Epic не восстановился");
        Assertions.assertEquals(2, manager.getSubtasksList().size(), "Subtaskи не восстановился");
        Assertions.assertEquals("IN_PROGRESS", epic.getStatus(), "Статус не изменился");
        Assertions.assertEquals(1L, manager.getHistory().get(0).getId(), "История не восстановилась");
        Assertions.assertTrue(file.delete(), "Файл не удалился");
    }

    @Test
    public void shouldLoadTasksWithoutHistoryFromFile()
            throws ManagerSaveException, IntersectionTimeException, FormatException {
        TaskManager generalManager = new FileBackedTaskManager(file);
        Task task = new Task("test", "test","NEW", null, null);
        generalManager.addTask(task);
        TaskManager manager = FileBackedTaskManager.loadFromFile(file);

        Assertions.assertTrue(file.exists(), "Файл не создался");
        Assertions.assertEquals(task, manager.getTasksList().get(0), "Задача не восстановилась");
        Assertions.assertEquals(0, manager.getHistory().size(), "История не пустая");
        Assertions.assertTrue(file.delete(), "Файл не удалился");
    }

    @Test
    public void shouldLoadEpicWithoutSubtasksFromFile()
            throws ManagerSaveException, FormatException, IntersectionTimeException {
        TaskManager generalManager = new FileBackedTaskManager(file);
        Epic epic = new Epic("test", "test");
        generalManager.addEpic(epic);
        TaskManager manager = FileBackedTaskManager.loadFromFile(file);

        Assertions.assertTrue(file.exists(), "Файл не создался");
        Assertions.assertEquals(1, manager.getEpicsList().size(), "Задача не восстановилась");
        Assertions.assertEquals(epic, manager.getEpicsList().get(0), "Задача не восстановилась");
        Assertions.assertTrue(file.delete(), "Файл не удалился");
    }
}