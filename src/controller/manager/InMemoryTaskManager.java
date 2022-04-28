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

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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

    protected void checkStatus(Epic epic) {
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

    protected void checkEndTimeEpic(Epic epic) {
        Optional<Duration> sumDuration = getSubtasksList().stream()
                .map(Task::getDuration)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .reduce(Duration::plus);

        List<LocalDateTime> startTimeList = getSubtasksList().stream()
                .map(Task::getStartTime)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        Optional<LocalDateTime> minStartTime = startTimeList.stream().min(Comparator.naturalOrder());
        Optional<LocalDateTime> maxStartTime = startTimeList.stream().max(Comparator.naturalOrder());

        sumDuration.ifPresentOrElse(x -> epic.setDuration(sumDuration), () -> epic.setDuration(Optional.empty()));
        sumDuration.ifPresent(x -> maxStartTime
                .ifPresentOrElse(y -> epic.setEndTime(maxStartTime), () -> epic.setEndTime(Optional.empty())));
        minStartTime.ifPresentOrElse(x -> epic.setStartTime(minStartTime), () -> epic.setStartTime(Optional.empty()));
    }

    private void checkIntersectionTime(Task checkTask) throws IntersectionTimeException {
        if (getPrioritizedTasks().size() < 2) {
            return;
        }

        if (checkTask.getStartTime().isEmpty()) {
            return;
        }

        for (Task task : getPrioritizedTasks()) {
            if (task instanceof Epic || task.equals(checkTask) || task.getEndTime().isEmpty())
                continue;

            boolean isTaskBetween = task.getStartTime().get().isBefore(checkTask.getStartTime().get())
                    && task.getEndTime().get().isAfter(checkTask.getStartTime().get());
            if (isTaskBetween) {
                throw new IntersectionTimeException("Задача с такой продолжительностью уже есть");
            }

            if (checkTask.getEndTime().isEmpty()) break;

            boolean isTaskStartTimeAfter = task.getStartTime().get().isAfter(checkTask.getEndTime().get());
            boolean isTaskEndTimeBefore = task.getEndTime().get().isBefore(checkTask.getStartTime().get());
            if (!(isTaskStartTimeAfter || isTaskEndTimeBefore))
                throw new IntersectionTimeException("Задача с такой продолжительностью уже есть");

        }
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
    public Set<Task> getPrioritizedTasks() {
        Set<Task> sortedTask = new TreeSet<Task>(startTimeComparator);
        sortedTask.addAll(this.tasks.values());
        sortedTask.addAll(this.epics.values());
        sortedTask.addAll(this.subtasks.values());

        return sortedTask;
    }

    Comparator<Task> startTimeComparator = new Comparator<Task>() {
        @Override
        public int compare(Task o1, Task o2) {
            if (o1.getStartTime().isEmpty()) {
                return (o2.getStartTime().isEmpty()) ? -1 : 1;
            }
            if (o2.getStartTime().isEmpty()) {
                return -1;
            }

            if (o1.getStartTime().get().isBefore(o2.getStartTime().get())) {
                return -1;
            } else {
                return 1;
            }
        }
    };

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
    public void updateTask(Task task) throws ManagerSaveException, IntersectionTimeException {
        checkIntersectionTime(task);
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateEpic(Epic epic) throws ManagerSaveException, IntersectionTimeException {
        epics.put(epic.getId(), epic);
    }

    @Override
    public void updateSubtask(Subtask subtask) throws ManagerSaveException, IntersectionTimeException {
        checkIntersectionTime(subtask);
        subtasks.put(subtask.getId(), subtask);
        Epic epic = getEpicById(subtask.getIdEpic());
        checkStatus(epic);
        checkEndTimeEpic(epic);
    }

    @Override
    public Task addTask(Task task) throws ManagerSaveException, IntersectionTimeException {
        if (task.getClass() == Task.class) {
            task.setId(idGenerator.generateID());
            checkIntersectionTime(task);
            tasks.put(idGenerator.getId(), task);
        }
        return task;
    }

    @Override
    public Epic addEpic(Epic epic) throws ManagerSaveException, IntersectionTimeException {
        epic.setId(idGenerator.generateID());
        checkStatus(epic);
        epics.put(idGenerator.getId(), epic);

        return epic;
    }

    @Override
    public Subtask addSubtask(Subtask subtask) throws ManagerSaveException, IntersectionTimeException {
        subtask.setId(idGenerator.generateID());
        checkIntersectionTime(subtask);
        subtasks.put(idGenerator.getId(), subtask);
        Epic epic = epics.get(subtask.getIdEpic());
        epic.addSubtask(subtask);
        checkStatus(epic);
        checkEndTimeEpic(epic);

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
