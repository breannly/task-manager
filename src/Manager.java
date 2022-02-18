import java.util.ArrayList;
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

    private void checkStatus(Epic epic) {
        int countNew = 0;
        int countDone = 0;
        int lengthSubtasks = epic.getSubtask().size();

        if (epic.getSubtask().isEmpty()) {
            epic.setStatus(StatusType.NEW.toString());
            return;
        }

        for (Subtask subtask : epic.getSubtask().values()){
            String status = subtask.getStatus();
            if (status.equals(StatusType.NEW.toString())) {
                countNew++;
            }
            if (status.equals(StatusType.DONE.toString())) {
                countDone++;
            }
        }

        if (countNew == lengthSubtasks) {
            epic.setStatus(StatusType.NEW.toString());
            return;
        }
        if (countDone == lengthSubtasks) {
            epic.setStatus(StatusType.DONE.toString());
            return;
        }

        epic.setStatus(StatusType.IN_PROGRESS.toString());
    }

    public ArrayList<Task> getTasks() {
        ArrayList<Task> tasks = new ArrayList<>(this.tasks.values());
        return tasks;
    }

    public ArrayList<Subtask> getSubtasks() {
        ArrayList<Subtask> subtasks = new ArrayList<>(this.subtasks.values());
        return subtasks;
    }

    public ArrayList<Epic> getEpics() {
        ArrayList<Epic> epics = new ArrayList<>(this.epics.values());
        return epics;
    }

    public void deleteTasks() {
        if (!tasks.isEmpty()) {
            tasks.clear();
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

    public void deleteEpics() {
        deleteSubtasks();
        if (!epics.isEmpty()) {
            epics.clear();
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

    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        checkStatus(getEpicById(subtask.getIdEpic()));
    }

    public Task createTask(Task task) {
        task.setId(getId());
        tasks.put(id, task);

        return task;
    }

    public Epic createEpic(Epic epic) {
        epic.setId(getId());
        checkStatus(epic);
        epics.put(id, epic);

        return epic;
    }

    public Subtask createSubtask(Subtask subtask) {
        subtask.setId(getId());
        subtasks.put(id, subtask);
        Epic epic = epics.get(subtask.getIdEpic());
        epic.addSubtask(subtask);

        return subtask;
    }

    public void deleteTaskById(Long id) {
        tasks.remove(id);
    }

    public void deleteEpicById(Long id) {
        Epic epic = getEpicById(id);
        for (Subtask subtask: epic.getSubtask().values()) {
            subtasks.remove(subtask.getId());
        }
        epics.remove(id);
    }

    public void deleteSubtaskById(Long id) {
        Subtask subtask = getSubtaskById(id);
        Epic epic = getEpicById(subtask.getIdEpic());
        epic.getSubtask().remove(id);
        epics.remove(id);
    }
}
