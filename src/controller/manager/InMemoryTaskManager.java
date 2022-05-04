package controller.manager;

import controller.exception.IntersectionTimeException;
import controller.exception.ManagerSaveException;
import controller.imanager.HistoryManager;
import controller.imanager.TaskManager;
import model.enums.StatusType;
import controller.utility.Managers;
import model.entity.Epic;
import model.entity.Subtask;
import model.entity.Task;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    private final Map<Long, Task> tasks;
    private final Map<Long, Subtask> subtasks;
    private final Map<Long, Epic> epics;
    private final Set<Task> sortedByStartTimeTasks;
    private final HistoryManager historyManager;
    private final IdGenerator idGenerator;

    public InMemoryTaskManager() {
        tasks = new HashMap<>();
        subtasks = new HashMap<>();
        epics = new HashMap<>();
        sortedByStartTimeTasks = new TreeSet<>(startTimeComparator);
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

    private void checkEndTimeEpic(Epic epic) {
        long sumDuration = getSubtasksList().stream()
                .map(Task::getDuration)
                .filter(Objects::nonNull)
                .reduce(0L, Long::sum);
        epic.setDuration(sumDuration);

        List<LocalDateTime> startTimeList = getSubtasksList().stream()
                .map(Task::getStartTime)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (!startTimeList.isEmpty()) {
            LocalDateTime minStartTime = startTimeList.stream().max(LocalDateTime::compareTo).get();
            LocalDateTime maxStartTime = startTimeList.stream().max(LocalDateTime::compareTo).get();
            epic.setStartTime(minStartTime);
            epic.setEndTime(maxStartTime);
        }
    }

    private void checkIntersectionTime(Task checkTask) throws IntersectionTimeException {
        if (getPrioritizedTasks().isEmpty()) {
            return;
        }

        if (checkTask.getStartTime() == null) {
            return;
        }

        for (Task task : getPrioritizedTasks()) {
            if (task.equals(checkTask) || task.getEndTime() == null) continue;
            boolean isTaskBetween = task.getStartTime().isBefore(checkTask.getStartTime())
                    && task.getEndTime().isAfter(checkTask.getStartTime());

            if (isTaskBetween) {
                throw new IntersectionTimeException("Задача с такой продолжительностью уже есть");
            }

            if (checkTask.getEndTime() == null) break;
            boolean isTaskStartTimeAfter = task.getStartTime().isAfter(checkTask.getEndTime());
            boolean isTaskEndTimeBefore = task.getEndTime().isBefore(checkTask.getStartTime());

            if (!(isTaskStartTimeAfter || isTaskEndTimeBefore))
                throw new IntersectionTimeException("Задача с такой продолжительностью уже есть");
        }
    }


    @Override
    public List<Task> getTasksList() {
        return new ArrayList<>(this.tasks.values());
    }

    @Override
    public Set<Task> getPrioritizedTasks() {
        return sortedByStartTimeTasks;
    }

    Comparator<Task> startTimeComparator = (o1, o2) -> {
        if (o1.getStartTime() == null) {
            return (o2.getStartTime() == null) ? -1 : 1;
        }
        if (o2.getStartTime() == null) {
            return -1;
        }

        if (o1.getStartTime().isBefore(o2.getStartTime())) {
            return -1;
        } else {
            return 1;
        }
    };

    @Override
    public List<Subtask> getSubtasksList() {
        return new ArrayList<>(this.subtasks.values());
    }

    @Override
    public List<Epic> getEpicsList() {
        return new ArrayList<>(this.epics.values());
    }

    @Override
    public void deleteTasks() throws ManagerSaveException {
        if (!tasks.isEmpty()) {
            deleteTasksFromPrioritizedSet(tasks.values());
            removeTasksFromHistory(tasks.values());
            tasks.clear();
        }
    }

    private void removeTasksFromHistory(Collection<? extends Task> tasks) {
        for (Task task : tasks) {
            historyManager.remove(task.getId());
        }
    }

    private void deleteTasksFromPrioritizedSet(Collection<Task> tasks) {
        tasks.forEach(sortedByStartTimeTasks::remove);
    }

    @Override
    public void deleteSubtasks() throws ManagerSaveException {
        if (!subtasks.isEmpty()) {
            deleteSubtasksFromPrioritizedSet(subtasks.values());
            deleteAllSubtasksFromEpics(epics.values());
            removeTasksFromHistory(subtasks.values());
            subtasks.clear();
        }
    }

    private void deleteAllSubtasksFromEpics(Collection<Epic> epics) {
        epics.forEach(epic -> {
            epic.getSubtask().clear();
            epic.setStatus(StatusType.NEW.toString());
        });
    }

    private void deleteSubtasksFromPrioritizedSet(Collection<Subtask> subtasks) {
        subtasks.forEach(sortedByStartTimeTasks::remove);
    }

    @Override
    public void deleteEpics() throws ManagerSaveException {
        if (!epics.isEmpty()) {
            deleteSubtasksFromPrioritizedSet(subtasks.values());
            removeTasksFromHistory(epics.values());
            deleteSubtasks();
            epics.clear();
        }
    }

    @Override
    public Task getTaskById(Long id) throws ManagerSaveException {
        if (!tasks.isEmpty()) {
            for (Long ID : tasks.keySet()) {
                if (ID.equals(id)) {
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
                if (ID.equals(id)) {
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
                if (ID.equals(id)) {
                    historyManager.add(subtasks.get(id));
                    return subtasks.get(id);
                }
            }
        }
        return null;
    }

    @Override
    public void updateTask(Task task) throws ManagerSaveException, IntersectionTimeException {
        if (tasks.containsKey(task.getId())) {
            checkIntersectionTime(task);
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public void updateEpic(Epic epic) throws ManagerSaveException, IntersectionTimeException {
        if (epics.containsKey(epic.getId()))
            epics.put(epic.getId(), epic);
    }

    @Override
    public void updateSubtask(Subtask subtask) throws ManagerSaveException, IntersectionTimeException {
        if (subtasks.containsKey(subtask.getId())) {
            checkIntersectionTime(subtask);
            subtasks.put(subtask.getId(), subtask);
            Epic epic = getEpicById(subtask.getIdEpic());
            checkStatus(epic);
            checkEndTimeEpic(epic);
        }
    }

    @Override
    public Task addTask(Task task) throws ManagerSaveException, IntersectionTimeException {
        if (task.getId() == null) {
            task.setId(idGenerator.generateID());
        }
        checkIntersectionTime(task);
        sortedByStartTimeTasks.add(task);
        tasks.put(task.getId(), task);

        return task;
    }

    @Override
    public Epic addEpic(Epic epic) throws ManagerSaveException, IntersectionTimeException {
        if (epic.getId() == null) {
            epic.setId(idGenerator.generateID());
            checkStatus(epic);
        }
        epics.put(epic.getId(), epic);

        return epic;
    }

    @Override
    public Subtask addSubtask(Subtask subtask) throws ManagerSaveException, IntersectionTimeException {
        if (subtask.getId() == null) {
            subtask.setId(idGenerator.generateID());
        }
        checkIntersectionTime(subtask);
        sortedByStartTimeTasks.add(subtask);
        Epic epic = epics.get(subtask.getIdEpic());
        epic.addSubtask(subtask);
        checkStatus(epic);
        checkEndTimeEpic(epic);
        subtasks.put(subtask.getId(), subtask);

        return subtask;
    }

    @Override
    public void deleteTaskById(Long id) throws ManagerSaveException {
        if (tasks.containsKey(id)) {
            sortedByStartTimeTasks.remove(getTaskById(id));
            historyManager.remove(id);
            tasks.remove(id);
        }
    }

    @Override
    public void deleteEpicById(Long id) throws ManagerSaveException {
        if (epics.containsKey(id)) {
            Collection<Subtask> subtaskCollection = getEpicById(id).getSubtask().values();
            deleteSubtasksFromPrioritizedSet(subtaskCollection);

            for (Subtask subtask : subtaskCollection) {
                subtasks.remove(subtask.getId());
                historyManager.remove(subtask.getId());
            }

            historyManager.remove(id);
            epics.remove(id);
        }
    }

    @Override
    public void deleteSubtaskById(Long id) throws ManagerSaveException {
        if (subtasks.containsKey(id)) {
            Subtask subtask = getSubtaskById(id);
            Epic epic = getEpicById(subtask.getIdEpic());
            epic.getSubtask().remove(id);
            checkStatus(epic);
            sortedByStartTimeTasks.remove(subtask);
            historyManager.remove(id);
            subtasks.remove(id);
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
