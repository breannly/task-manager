package controller.utility;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import controller.adapter.LocalDateTimeAdapter;
import controller.imanager.HistoryManager;
import controller.manager.FileBackedTaskManager;
import controller.manager.HttpTaskManager;
import controller.manager.InMemoryHistoryManager;
import controller.imanager.TaskManager;
import controller.manager.InMemoryTaskManager;
import server.KVServer;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

public class Managers {
    private static HistoryManager historyManager;

    public static HttpTaskManager getDefault() throws IOException, InterruptedException {
        return new HttpTaskManager("http://localhost:" + KVServer.PORT + "/register");
    }

    public static TaskManager getDefaultBackedFileTaskManager() {
        final File file = new File("file");
        return new FileBackedTaskManager(file);
    }

    public static TaskManager getDefaultInMemoryTaskManager() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        if (historyManager == null) {
            historyManager = new InMemoryHistoryManager();
        }
        return historyManager;
    }

    public static Gson getGson() {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
    }
}
