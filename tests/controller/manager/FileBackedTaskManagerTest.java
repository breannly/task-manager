package controller.manager;

import controller.exception.ManagerSaveException;
import controller.imanager.TaskManager;
import controller.imanager.TaskManagerTest;
import controller.utility.ReaderFile;
import model.entity.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {
    File file = new File("test.csv");

    @Test
    public void shouldSaveTasksAndHistoryToFile() throws ManagerSaveException {
        TaskManager manager = new FileBackedTaskManager(file);
        Task task = new Task("test", "test","NEW");
        manager.addTask(task);
        manager.getTaskById(task.getId());
        String expected =
                "id,type,name,status,description,epic\n" +
                "1,TASK,test,NEW,test,\n" +
                "\n" +
                "1";
        String actual = ReaderFile.readFileContents(file);

        Assertions.assertTrue(file.exists(), "Файл не создался");
        Assertions.assertEquals(expected, actual, "Значения не совпадают");
        Assertions.assertTrue(file.delete(), "Файл не удалился");
    }

}