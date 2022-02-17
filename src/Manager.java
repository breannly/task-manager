import java.util.HashMap;
import java.util.Scanner;

public class Manager {
    private HashMap<Long, Task> tasks;
    private HashMap<Long, Subtask> subtasks;
    private HashMap<Long, Epic> epics;
    private Long id;

    Scanner scanner;

    public Manager() {
        id = 0L;
        tasks = new HashMap<>();
        subtasks = new HashMap<>();
        epics = new HashMap<>();
    }

    private Long getId() {
        id++;
        return id;
    }

    public Object getTasks() {
        return tasks.values();
    }

    public Object getSubtasks() {
        return subtasks.values();
    }

    public Object getEpics() {
        return epics.values();
    }

    public void deleteTasks() {
        if (!tasks.isEmpty()) {
            tasks.clear();
        }
    }

    public void deleteEpics() {
        if (!epics.isEmpty()) {
            epics.clear();
        }
    }

    public void deleteSubtasks() {
        for (Epic epic : epics.values()) {
            epic.setStatus(StatusType.NEW.toString());
        }
        if (!subtasks.isEmpty()) {
            subtasks.clear();
        }
    }

    public Task getTaskById(Long id) {
        if (tasks.isEmpty()) {
            return null;
        }
        for (Long ID : epics.keySet()) {
            if (ID == id) {
                return epics.get(id);
            }
        }
        return null;
    }

    public Epic getEpicById(Long id) {
        if (epics.isEmpty()) {
            return null;
        }
        for (Long ID : epics.keySet()) {
            if (ID == id) {
                return epics.get(id);
            }
        }
        return null;
    }

    public Subtask getSubtaskById(Long id) {
        if (subtasks.isEmpty()) {
            return null;
        }
        for (Long ID : subtasks.keySet()) {
            if (ID == id) {
                return subtasks.get(id);
            }
        }
        return null;
    }
}
