package controller.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import controller.adapter.LocalDateTimeAdapter;
import controller.exception.IntersectionTimeException;
import controller.exception.ManagerSaveException;
import controller.utility.Managers;
import model.entity.Epic;
import model.entity.Subtask;
import model.entity.Task;
import server.KVTaskClient;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class HTTPTaskManager extends FileBackedTaskManager {
    private final KVTaskClient client;
    private final Gson gson;

    public HTTPTaskManager(String uri) throws IOException, InterruptedException {
        super(uri);
        client = new KVTaskClient(URI.create(uri));
        gson = Managers.getGson();
    }

    @Override
    protected void save() throws IOException, InterruptedException {
        for (Task task : getTasks().values()) {
            client.put(task.getId().toString(), gson.toJson(task));
        }
        for (Epic epic : getEpics().values()) {
            client.put(epic.getId().toString(), gson.toJson(epic));
        }
        for (Subtask subtask : getSubtasks().values()) {
            client.put(subtask.getId().toString(), gson.toJson(subtask));
        }
    }
}
