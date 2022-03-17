package controller.manager;

import controller.imanager.HistoryManager;
import model.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final List<Task> history;
    private static final int HISTORY_SIZE = 10;

    public InMemoryHistoryManager() {
        history = new LinkedList<>();
    }

    private boolean isFullHistory() {
        if (history.size() == HISTORY_SIZE) {
            return true;
        }

        return false;
    }

    @Override
    public List<Task> getHistory() {
        return history;
    }

    @Override
    public void add(Task task) {
        if (isFullHistory()) {
            history.remove(0);
        }

        history.add(task);
    }

    @Override
    public void remove(Long id) {

    }


}
