package controller;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {
    private HashMap<Long, Task> tasks;
    private HashMap<Long, Subtask> subtasks;
    private HashMap<Long, Epic> epics;
    private Long id;

    public InMemoryTaskManager() {
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

    @Override
    public ArrayList<Task> getTasks() {
        ArrayList<Task> tasks = new ArrayList<>(this.tasks.values());
        return tasks;
    }

    @Override
    public ArrayList<Subtask> getSubtasks() {
        ArrayList<Subtask> subtasks = new ArrayList<>(this.subtasks.values());
        return subtasks;
    }

    @Override
    public ArrayList<Epic> getEpics() {
        ArrayList<Epic> epics = new ArrayList<>(this.epics.values());
        return epics;
    }

    @Override
    public void deleteTasks() {
        if (!tasks.isEmpty()) {
            tasks.clear();
        }
    }

    @Override
    public void deleteSubtasks() {
        for (Epic epic : epics.values()) {
            epic.setStatus(StatusType.NEW.toString());
        }
        if (!subtasks.isEmpty()) {
            subtasks.clear();
        }
    }

    @Override
    public void deleteEpics() {
        deleteSubtasks();
        if (!epics.isEmpty()) {
            epics.clear();
        }
    }

    @Override
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

    @Override
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

    @Override
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

    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        checkStatus(getEpicById(subtask.getIdEpic()));
    }

    @Override
    public Task createTask(Task task) {
        task.setId(getId());
        tasks.put(id, task);

        return task;
    }

    @Override
    public Epic createEpic(Epic epic) {
        epic.setId(getId());
        checkStatus(epic);
        epics.put(id, epic);

        return epic;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        subtask.setId(getId());
        subtasks.put(id, subtask);
        Epic epic = epics.get(subtask.getIdEpic());
        epic.addSubtask(subtask);

        return subtask;
    }

    @Override
    public void deleteTaskById(Long id) {
        tasks.remove(id);
    }

    @Override
    public void deleteEpicById(Long id) {
        Epic epic = getEpicById(id);
        for (Subtask subtask: epic.getSubtask().values()) {
            subtasks.remove(subtask.getId());
        }
        epics.remove(id);
    }

    @Override
    public void deleteSubtaskById(Long id) {
        Subtask subtask = getSubtaskById(id);
        Epic epic = getEpicById(subtask.getIdEpic());
        checkStatus(epic);
        epic.getSubtask().remove(id);
    }
}
