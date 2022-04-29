package controller.manager;

import controller.exception.FormatException;
import controller.exception.IntersectionTimeException;
import controller.exception.ManagerSaveException;
import controller.imanager.HistoryManager;
import controller.imanager.TaskManager;
import controller.utility.Managers;
import controller.utility.ReaderFile;
import model.entity.Epic;
import model.entity.Subtask;
import model.entity.Task;
import model.enums.TaskType;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {
    private static final String LINE_DELIMITER = "\n";
    private static final String VALUE_DELIMITER = ",";

    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    @Override
    public List<Task> getTasksList() {
        return super.getTasksList();
    }

    @Override
    public List<Subtask> getSubtasksList() {
        return super.getSubtasksList();
    }

    @Override
    public List<Epic> getEpicsList() {
        return super.getEpicsList();
    }

    @Override
    public Set<Task> getPrioritizedTasks() {
        return super.getPrioritizedTasks();
    }

    @Override
    public void deleteTasks() throws ManagerSaveException {
        super.deleteTasks();
        save();
    }

    @Override
    public void deleteSubtasks() throws ManagerSaveException {
        super.deleteSubtasks();
        save();
    }

    @Override
    public void deleteEpics() throws ManagerSaveException {
        super.deleteEpics();
        save();
    }

    @Override
    public Task getTaskById(Long id) throws ManagerSaveException {
        Task task = super.getTaskById(id);
        save();

        return task;
    }

    @Override
    public Epic getEpicById(Long id) throws ManagerSaveException {
        Epic epic = super.getEpicById(id);
        save();

        return epic;
    }

    @Override
    public Subtask getSubtaskById(Long id) throws ManagerSaveException {
        Subtask subtask = super.getSubtaskById(id);
        save();

        return subtask;
    }

    @Override
    public void updateTask(Task task) throws ManagerSaveException, IntersectionTimeException {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) throws ManagerSaveException, IntersectionTimeException {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) throws ManagerSaveException, IntersectionTimeException {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public Task addTask(Task task) throws ManagerSaveException, IntersectionTimeException {
        super.addTask(task);
        save();

        return task;
    }

    @Override
    public Epic addEpic(Epic epic) throws ManagerSaveException, IntersectionTimeException {
        super.addEpic(epic);
        save();

        return epic;
    }

    @Override
    public Subtask addSubtask(Subtask subtask) throws ManagerSaveException, IntersectionTimeException {
        super.addSubtask(subtask);
        save();

        return subtask;
    }

    @Override
    public void deleteTaskById(Long id) throws ManagerSaveException {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(Long id) throws ManagerSaveException {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteSubtaskById(Long id) throws ManagerSaveException {
        super.deleteSubtaskById(id);
        save();
    }

    @Override
    public List<Task> getHistory() {
        return super.getHistory();
    }

    private void save() throws ManagerSaveException {

        try (Writer fileWriter = new OutputStreamWriter(new FileOutputStream(file.getPath()), "UTF8")) {
            if (!Files.exists(Path.of(file.getPath()))) throw new ManagerSaveException();
            HistoryManager historyManager = Managers.getDefaultHistory();
            var sb = new StringBuilder();
            sb.append("id,type,name,status,description,epic,duration,startTime,endTime").append(LINE_DELIMITER);
            for (Task task : getTasksList()) {
                sb.append(task.toString()).append(LINE_DELIMITER);
            }
            for (Epic epic : getEpicsList()) {
                sb.append(epic.toString()).append(LINE_DELIMITER);
            }
            for (Subtask subtask : getSubtasksList()) {
                sb.append(subtask.toString()).append(LINE_DELIMITER);
            }
            sb.append(LINE_DELIMITER);
            sb.append(InMemoryHistoryManager.toString(historyManager));
            fileWriter.write(sb.toString());
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
    }

    public static FileBackedTaskManager loadFromFile(File file)
            throws ManagerSaveException, FormatException, IntersectionTimeException {
        String fileContent = ReaderFile.readFileContents(file);
        String[] arrayContent = fileContent.split(LINE_DELIMITER);
        int length = arrayContent.length;
        String history = arrayContent[length - 1];
        String[] arrayTaskContent = Arrays.copyOfRange(arrayContent, 1, length - 2);

        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);

        for (String task : arrayTaskContent) {
            fileBackedTaskManager.fromString(task);
        }

        if (!history.isBlank()) {
            List<Long> historyListId = InMemoryHistoryManager.fromString(arrayContent[length - 1]);
            for (Long id : historyListId) {
                if (fileBackedTaskManager.getTasks().get(id) != null) {
                    fileBackedTaskManager.getTaskById(id);
                } else if (fileBackedTaskManager.getEpics().get(id) != null) {
                    fileBackedTaskManager.getEpicById(id);
                } else {
                    fileBackedTaskManager.getSubtaskById(id);
                }
            }
        }
        return fileBackedTaskManager;
    }

    private void fromString(String value)
            throws ManagerSaveException, FormatException, IntersectionTimeException {
        String[] data = value.split(VALUE_DELIMITER);
        addTask(TaskType.valueOf(data[1]), data);
    }

    private void addTask(TaskType type, String[] data)
            throws ManagerSaveException, FormatException, IntersectionTimeException {
        switch (type) {
            case TASK:
                Task task = new Task(data[2], data[4], data[3], Long.parseLong(data[5]), checkData(data[6]));
                task.setId(Long.parseLong(data[0]));
                addTask(task);
                return;
            case EPIC:
                Epic epic = new Epic(data[2], data[4]);
                epic.setId(Long.parseLong(data[0]));
                epic.setStatus(data[3]);
                addEpic(epic);
                return;
            case SUBTASK:
                Subtask subtask = new Subtask(data[2],
                        data[4],
                        data[3],
                        Long.parseLong(data[5]),
                        Long.parseLong(data[6]),
                        checkData(data[7]));
                subtask.setId(Long.parseLong(data[0]));
                addSubtask(subtask);
        }
    }

    private String checkData(String data) {
        if (data.equals("null")) {
            data = null;
        }
        return data;
    }
}
