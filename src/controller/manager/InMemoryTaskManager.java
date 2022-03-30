package controller.manager;

import controller.imanager.HistoryManager;
import controller.imanager.TaskManager;
import model.StatusType;
import controller.utility.Managers;
import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    private final Map<Long, Task> tasks;
    private final Map<Long, Subtask> subtasks;
    private final Map<Long, Epic> epics;
    private final HistoryManager historyManager;
    private final IdGenerator idGenerator;

    public InMemoryTaskManager() {
        tasks = new HashMap<>();
        subtasks = new HashMap<>();
        epics = new HashMap<>();
        historyManager = Managers.getDefaultHistory();
        idGenerator = new IdGenerator();
    }

    private void checkStatus(Epic epic) {
        int countNew = 0;
        int countDone = 0;
        int lengthSubtasks = epic.getSubtask().size();

        if (epic.getSubtask().isEmpty()) {
            epic.setStatus(StatusType.NEW.toString());
            return;
        }

        for (Subtask subtask : epic.getSubtask().values()) {
            String status = subtask.getStatus();
            if (status.equals(StatusType.NEW.toString())) {
                countNew++;
            } else if (status.equals(StatusType.DONE.toString())) {
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

    private void removeTasksFromHistory(Map<Long, ? extends Task> taskList) {
        for (Task task : taskList.values()) {
            historyManager.remove(task.getId());
        }
    }

    @Override
    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>(this.tasks.values());
        return tasks;
    }

    @Override
    public List<Subtask> getSubtasks() {
        List<Subtask> subtasks = new ArrayList<>(this.subtasks.values());
        return subtasks;
    }

    @Override
    public List<Epic> getEpics() {
        List<Epic> epics = new ArrayList<>(this.epics.values());
        return epics;
    }

    @Override
    public void deleteTasks() {
        removeTasksFromHistory(tasks);

        if (!tasks.isEmpty()) {
            tasks.clear();
        }
    }

    @Override
    public void deleteSubtasks() {
        removeTasksFromHistory(subtasks);
        for (Epic epic : epics.values()) {
            epic.setStatus(StatusType.NEW.toString());
        }
        if (!subtasks.isEmpty()) {
            subtasks.clear();
        }
    }

    @Override
    public void deleteEpics() {
        removeTasksFromHistory(epics);
        deleteSubtasks();
        if (!epics.isEmpty()) {
            epics.clear();
        }
    }

    @Override
    public Task getTaskById(Long id) {
        if (!tasks.isEmpty()) {
            for (Long ID : tasks.keySet()) {
                if (ID == id) {
                    historyManager.add(tasks.get(id));
                    return tasks.get(id);
                }
            }
        }
        return null;
    }

    @Override
    public Epic getEpicById(Long id) {
        if (!epics.isEmpty()) {
            for (Long ID : epics.keySet()) {
                if (ID == id) {
                    historyManager.add(epics.get(id));
                    return epics.get(id);
                }
            }
        }
        return null;
    }

    @Override
    public Subtask getSubtaskById(Long id) {
        if (!subtasks.isEmpty()) {
            for (Long ID : subtasks.keySet()) {
                if (ID == id) {
                    historyManager.add(subtasks.get(id));
                    return subtasks.get(id);
                }
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
        task.setId(idGenerator.generateID());
        tasks.put(idGenerator.getId(), task);

        return task;
    }

    @Override
    public Epic createEpic(Epic epic) {
        epic.setId(idGenerator.generateID());
        checkStatus(epic);
        epics.put(idGenerator.getId(), epic);

        return epic;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        subtask.setId(idGenerator.generateID());
        subtasks.put(idGenerator.getId(), subtask);
        Epic epic = epics.get(subtask.getIdEpic());
        epic.addSubtask(subtask);

        return subtask;
    }

    @Override
    public void deleteTaskById(Long id) {
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteEpicById(Long id) {
        Epic epic = getEpicById(id);
        for (Subtask subtask : epic.getSubtask().values()) {
            subtasks.remove(subtask.getId());
            historyManager.remove(subtask.getId());
        }
        epics.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteSubtaskById(Long id) {
        Subtask subtask = getSubtaskById(id);
        Epic epic = getEpicById(subtask.getIdEpic());
        checkStatus(epic);
        epic.getSubtask().remove(id);
        historyManager.remove(id);
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}
