package model;

import java.util.HashMap;

public class Epic extends Task {
    private final HashMap<Long, Subtask> subtasks;

    public Epic(String name, String description) {
        super(name, description);
        subtasks = new HashMap<>();
    }

    @Override
    public String toString() {
        return String.join(",",
                getId().toString(),
                TaskType.EPIC.name(),
                getName(),
                getStatus(),
                getDescription());
    }

    public HashMap<Long, Subtask> getSubtask() {
        return subtasks;
    }

    public void addSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
    }
}
