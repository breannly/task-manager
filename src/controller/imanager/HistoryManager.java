package controller.imanager;

import model.entity.Task;

import java.util.List;

public interface HistoryManager {
    void add(Task task);

    void remove(Long id);

    List<Task> getHistory();
}
