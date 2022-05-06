package controller.utility;

import controller.imanager.HistoryManager;
import controller.manager.FileBackedTaskManager;
import controller.manager.HTTPTaskManager;
import controller.manager.InMemoryHistoryManager;
import controller.manager.InMemoryTaskManager;
import controller.imanager.TaskManager;
import server.KVServer;

import java.io.File;
import java.io.IOException;

public class Managers {
    private static HistoryManager historyManager;

    public static HTTPTaskManager getDefault() throws IOException, InterruptedException {
        return new HTTPTaskManager("http://localhost:" + KVServer.PORT + "/register");
    }

    public static TaskManager getDefaultBackedFileTaskManager() {
        final File file = new File("file");
        return new FileBackedTaskManager(file);
    }

    public static HistoryManager getDefaultHistory() {
        if (historyManager == null) {
            historyManager = new InMemoryHistoryManager();
        }
        return historyManager;
    }
}
