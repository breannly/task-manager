package controller.utility;

import controller.imanager.HistoryManager;
import controller.manager.InMemoryHistoryManager;
import controller.manager.InMemoryTaskManager;
import controller.imanager.TaskManager;

public class Managers {
    private static HistoryManager historyManager;

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        if (historyManager == null) {
            historyManager = new InMemoryHistoryManager();
        }
        return historyManager;
    }
}
