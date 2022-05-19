package controller.manager;

import com.google.gson.Gson;
import controller.utility.Managers;
import server.KVTaskClient;

import java.io.IOException;
import java.net.URI;

public class HttpTaskManager extends FileBackedTaskManager {
    private static final String TASK_KEY = "TASKS";
    private static final String SUBTASK_KEY = "SUBTASKS";
    private static final String EPIC_KEY = "EPICS";
    private static final String HISTORY_KEY = "HISTORY";

    private final KVTaskClient client;
    private final Gson gson;

    public HttpTaskManager(String uri) throws IOException, InterruptedException {
        super(uri);
        client = new KVTaskClient(URI.create(uri));
        gson = Managers.getGson();
    }

    @Override
    protected void save() throws IOException, InterruptedException {
        if (!getTasksList().isEmpty())
            client.put(TASK_KEY, gson.toJson(getTasksList()));

        if (!getSubtasksList().isEmpty())
            client.put(SUBTASK_KEY, gson.toJson(getSubtasksList()));

        if (!getEpicsList().isEmpty())
            client.put(EPIC_KEY, gson.toJson(getEpicsList()));

        if (!getHistory().isEmpty())
            client.put(HISTORY_KEY, gson.toJson(getHistory()));
    }
}
