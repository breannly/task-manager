package controller.imanager;

import controller.exception.IntersectionTimeException;
import controller.exception.ManagerSaveException;
import model.entity.Epic;
import model.entity.Subtask;
import model.entity.Task;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface TaskManager {

    public List<Task> getTasksList();

    public List<Subtask> getSubtasksList();

    public List<Epic> getEpicsList();

    public void deleteTasks() throws ManagerSaveException, IOException, InterruptedException;

    public void deleteSubtasks() throws ManagerSaveException, IOException, InterruptedException;

    public void deleteEpics() throws ManagerSaveException, IOException, InterruptedException;

    public Task getTaskById(Long id) throws ManagerSaveException, IOException, InterruptedException;

    public Epic getEpicById(Long id) throws ManagerSaveException, IOException, InterruptedException;

    public Subtask getSubtaskById(Long id) throws ManagerSaveException, IOException, InterruptedException;

    public void updateTask(Task task)
            throws ManagerSaveException, IntersectionTimeException, IOException, InterruptedException;

    public void updateEpic(Epic epic)
            throws ManagerSaveException, IntersectionTimeException, IOException, InterruptedException;

    public void updateSubtask(Subtask subtask)
            throws ManagerSaveException, IntersectionTimeException, IOException, InterruptedException;

    public Task addTask(Task task)
            throws ManagerSaveException, IntersectionTimeException, IOException, InterruptedException;

    public Epic addEpic(Epic epic)
            throws ManagerSaveException, IntersectionTimeException, IOException, InterruptedException;

    public Subtask addSubtask(Subtask subtask)
            throws ManagerSaveException, IntersectionTimeException, IOException, InterruptedException;

    public void deleteTaskById(Long id) throws ManagerSaveException, IOException, InterruptedException;

    public void deleteEpicById(Long id) throws ManagerSaveException, IOException, InterruptedException;

    public void deleteSubtaskById(Long id) throws ManagerSaveException, IOException, InterruptedException;

    public List<Task> getHistory();

    public Set<Task> getPrioritizedTasks();
}
