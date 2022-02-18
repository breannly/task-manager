package model;

import java.util.HashMap;

public class Epic extends Task {
    private HashMap<Long, Subtask> subtasks;

    public Epic(String name, String description) {
        super(name, description);
        subtasks = new HashMap<>();
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name= " + getName() +
                ", id=" + getId() +
                ", status=" + getStatus() +
                ", subtasks=" + subtasks.values() + "}";
    }

    public HashMap<Long, Subtask> getSubtask() {
        return subtasks;
    }

    public void addSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
    }
}
