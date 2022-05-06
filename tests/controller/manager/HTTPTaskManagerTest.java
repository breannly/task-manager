package controller.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import controller.adapter.LocalDateTimeAdapter;
import controller.exception.FormatException;
import controller.exception.IntersectionTimeException;
import controller.exception.ManagerSaveException;
import controller.utility.Managers;
import model.entity.Epic;
import model.entity.Subtask;
import model.entity.Task;
import org.junit.jupiter.api.*;

import server.KVServer;
import server.KVTaskClient;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;

class HTTPTaskManagerTest {
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();
    private static KVTaskClient client;
    private static HTTPTaskManager manager;
    private static KVServer server;


    @BeforeEach
    public void beforeEach() throws IOException, InterruptedException {
        server = new KVServer();
        server.start();
        manager = Managers.getDefault();
        client = new KVTaskClient(URI.create("http://localhost:" + KVServer.PORT + "/register"));
    }

    @AfterEach
    public void afterEach() {
        server.close();
    }

    @Test
    void shouldReturnTaskFromServer()
            throws FormatException, IntersectionTimeException, ManagerSaveException, IOException, InterruptedException {
        Task task = new Task("test", "test", "NEW", 0, null);
        manager.addTask(task);
        Assertions.assertEquals(gson.toJson(task), client.load(task.getId().toString()));
    }

    @Test
    void shouldReturnEpicFromServer()
            throws FormatException, IntersectionTimeException, ManagerSaveException, IOException, InterruptedException {
        Epic epic = new Epic("test", "test");
        manager.addEpic(epic);
        manager.deleteEpicById(epic.getId());
        Assertions.assertEquals(gson.toJson(epic), client.load(epic.getId().toString()));
    }

    @Test
    void shouldReturnSubtaskFromServer()
            throws FormatException, IntersectionTimeException, ManagerSaveException, IOException, InterruptedException {
        Epic epic = new Epic("test", "test");
        manager.addEpic(epic);
        Subtask subtask = new Subtask("test",
                "test",
                "NEW",
                epic.getId(),
                0,
                null);
        manager.addSubtask(subtask);
        Assertions.assertEquals(gson.toJson(subtask), client.load(subtask.getId().toString()));
    }

    @Test
    void shouldReturnUpdateTaskFromServer()
            throws FormatException, IntersectionTimeException, ManagerSaveException, IOException, InterruptedException {
        Task task = new Task("test", "test", "NEW", 0, null);
        manager.addTask(task);
        Task updateTask = new Task("updateTest",
                "updateTest",
                "NEW",
                47,
                "05.06.22 15:29");
        updateTask.setId(task.getId());
        manager.updateTask(updateTask);
        Assertions.assertEquals(gson.toJson(updateTask), client.load(task.getId().toString()));
    }

    @Test
    void shouldReturnUpdateEpicFromServer()
            throws FormatException, IntersectionTimeException, ManagerSaveException, IOException, InterruptedException {
        Epic epic = new Epic("test", "test");
        manager.addEpic(epic);
        Epic updateEpic = new Epic("updateTest",
                "updateTest");
        updateEpic.setId(epic.getId());
        updateEpic.setStatus("DONE");
        manager.updateEpic(updateEpic);
        Assertions.assertEquals(gson.toJson(updateEpic), client.load(epic.getId().toString()));
    }

    @Test
    void shouldReturnUpdateSubtaskFromServer()
            throws FormatException, IntersectionTimeException, ManagerSaveException, IOException, InterruptedException {
        Epic epic = new Epic("test", "test");
        manager.addEpic(epic);
        Subtask subtask = new Subtask("test",
                "test",
                "NEW",
                epic.getId(),
                0,
                null);
        manager.addSubtask(subtask);
        Subtask updateSubtask = new Subtask("updateTest",
                "updateTest",
                "IN_PROGRESS",
                epic.getId(),
                56,
                "06.05.22 15:32");
        updateSubtask.setId(subtask.getId());
        manager.updateSubtask(updateSubtask);
        Assertions.assertEquals(gson.toJson(updateSubtask), client.load(subtask.getId().toString()));
    }

}