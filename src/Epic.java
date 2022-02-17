import java.util.HashMap;

public class Epic extends Task {
    private HashMap<Integer, Subtask> subtasks;

    public Epic(String name, String description) {
        super (name, description);

    }

    @Override
    public String toString() {
        return "Epic{" +
                "name= " + getName() +
                ", id=" + getId() +
                ", status=" + getStatus() +
                "subtasks=" + subtasks.values() + "}";
    }
}
