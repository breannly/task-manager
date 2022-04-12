package controller.manager;

import controller.exception.ManagerSaveException;
import controller.imanager.HistoryManager;
import controller.imanager.TaskManager;
import controller.utility.Managers;
import model.entity.Epic;
import model.entity.Subtask;
import model.entity.Task;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {
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
    public void updateTask(Task task) throws ManagerSaveException {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) throws ManagerSaveException {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) throws ManagerSaveException {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public Task addTask(Task task) throws ManagerSaveException {
        super.addTask(task);
        save();

        return task;
    }

    @Override
    public Epic addEpic(Epic epic) throws ManagerSaveException {
        super.addEpic(epic);
        save();

        return epic;
    }

    @Override
    public Subtask addSubtask(Subtask subtask) throws ManagerSaveException {
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
        if (!Files.exists(Path.of(file.getPath()))) throw new ManagerSaveException();

        try (Writer fileWriter = new OutputStreamWriter(new FileOutputStream(file.getPath()), "UTF8")) {
            HistoryManager historyManager = Managers.getDefaultHistory();
            var sb = new StringBuilder();
            sb.append("id,type,name,status,description,epic").append("\n");
            for (Task task : super.getTasksList()) {
                sb.append(task.toString()).append(",").append("\n");
            }
            for (Epic epic : super.getEpicsList()) {
                sb.append(epic.toString()).append(",").append("\n");
            }
            for (Subtask subtask : super.getSubtasksList()) {
                sb.append(subtask.toString()).append(",").append("\n");
            }
            sb.append("\n");
            sb.append(InMemoryHistoryManager.toString(historyManager));
            fileWriter.write(sb.toString());
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
    }
}
