package controller.manager;

import controller.imanager.HistoryManager;
import model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private List<Task> history;

    public InMemoryHistoryManager() {
        history = new ArrayList<>();
    }

    @Override
    public List<Task> getHistory() {
        return history;
    }

    @Override
    public void add(Task task) {
        if (isFullHistory()) {
            history.remove(0);
            history.add(task);
            return;
        }

        history.add(task);
    }

    private boolean isFullHistory() {
        if (history.size() == 10) {
            return true;
        }

        return false;
    }
}
