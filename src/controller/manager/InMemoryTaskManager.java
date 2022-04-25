package controller.manager;

import controller.exception.ManagerSaveException;
import controller.imanager.HistoryManager;
import controller.imanager.TaskManager;
import model.enums.StatusType;
import controller.utility.Managers;
import model.entity.Epic;
import model.entity.Subtask;
import model.entity.Task;

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
    public List<Task> getTasksList() {
        List<Task> tasks = new ArrayList<>(this.tasks.values());
        return tasks;
    }

    @Override
    public List<Subtask> getSubtasksList() {
        List<Subtask> subtasks = new ArrayList<>(this.subtasks.values());
        return subtasks;
    }

    @Override
    public List<Epic> getEpicsList() {
        List<Epic> epics = new ArrayList<>(this.epics.values());
        return epics;
    }

    @Override
    public void deleteTasks() throws ManagerSaveException {
        removeTasksFromHistory(tasks);

        if (!tasks.isEmpty()) {
            tasks.clear();
        }
    }

    @Override
    public void deleteSubtasks() throws ManagerSaveException {
        removeTasksFromHistory(subtasks);
        for (Epic epic : epics.values()) {
            epic.setStatus(StatusType.NEW.toString());
            epic.getSubtask().clear();
        }
        if (!subtasks.isEmpty()) {
            subtasks.clear();
        }
    }

    @Override
    public void deleteEpics() throws ManagerSaveException {
        removeTasksFromHistory(epics);
        deleteSubtasks();
        if (!epics.isEmpty()) {
            epics.clear();
        }
    }

    @Override
    public Task getTaskById(Long id) throws ManagerSaveException {
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
    public Epic getEpicById(Long id) throws ManagerSaveException {
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
    public Subtask getSubtaskById(Long id) throws ManagerSaveException {
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
    public void updateTask(Task task) throws ManagerSaveException {
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateEpic(Epic epic) throws ManagerSaveException {
        epics.put(epic.getId(), epic);
    }

    @Override
    public void updateSubtask(Subtask subtask) throws ManagerSaveException {
        subtasks.put(subtask.getId(), subtask);
        checkStatus(getEpicById(subtask.getIdEpic()));
    }

    @Override
    public Task addTask(Task task) throws ManagerSaveException {
        if (task.getClass() == Task.class) {
            task.setId(idGenerator.generateID());
            tasks.put(idGenerator.getId(), task);
        }
        return task;
    }

    @Override
    public Epic addEpic(Epic epic) throws ManagerSaveException {
        epic.setId(idGenerator.generateID());
        checkStatus(epic);
        epics.put(idGenerator.getId(), epic);

        return epic;
    }

    @Override
    public Subtask addSubtask(Subtask subtask) throws ManagerSaveException {
        subtask.setId(idGenerator.generateID());
        subtasks.put(idGenerator.getId(), subtask);
        Epic epic = epics.get(subtask.getIdEpic());
        epic.addSubtask(subtask);
        checkStatus(epic);

        return subtask;
    }

    @Override
    public void deleteTaskById(Long id) throws ManagerSaveException {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
            historyManager.remove(id);
        }
    }

    @Override
    public void deleteEpicById(Long id) throws ManagerSaveException {
        if (epics.containsKey(id)) {
            Epic epic = getEpicById(id);
            for (Subtask subtask : epic.getSubtask().values()) {
                subtasks.remove(subtask.getId());
                historyManager.remove(subtask.getId());
            }
            epics.remove(id);
            historyManager.remove(id);
        }
    }

    @Override
    public void deleteSubtaskById(Long id) throws ManagerSaveException {
        if (subtasks.containsKey(id)) {
            Subtask subtask = getSubtaskById(id);
            Epic epic = getEpicById(subtask.getIdEpic());
            epic.getSubtask().remove(id);
            checkStatus(epic);
            subtasks.remove(id);
            historyManager.remove(id);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    protected Map<Long, Task> getTasks() {
        return tasks;
    }

    protected Map<Long, Subtask> getSubtasks() {
        return subtasks;
    }

    protected Map<Long, Epic> getEpics() {
        return epics;
    }
}
