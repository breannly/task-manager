package controller.imanager;

import controller.exception.IntersectionTimeException;
import controller.exception.ManagerSaveException;
import model.entity.Epic;
import model.entity.Subtask;
import model.entity.Task;

import java.util.List;
import java.util.Set;

public interface TaskManager {

    public List<Task> getTasksList();

    public List<Subtask> getSubtasksList();

    public List<Epic> getEpicsList();

    public void deleteTasks() throws ManagerSaveException;

    public void deleteSubtasks() throws ManagerSaveException;

    public void deleteEpics() throws ManagerSaveException;

    public Task getTaskById(Long id) throws ManagerSaveException;

    public Epic getEpicById(Long id) throws ManagerSaveException;

    public Subtask getSubtaskById(Long id) throws ManagerSaveException;

    public void updateTask(Task task) throws ManagerSaveException, IntersectionTimeException;

    public void updateEpic(Epic epic) throws ManagerSaveException, IntersectionTimeException;

    public void updateSubtask(Subtask subtask) throws ManagerSaveException, IntersectionTimeException;

    public Task addTask(Task task) throws ManagerSaveException, IntersectionTimeException;

    public Epic addEpic(Epic epic) throws ManagerSaveException, IntersectionTimeException;

    public Subtask addSubtask(Subtask subtask) throws ManagerSaveException, IntersectionTimeException;

    public void deleteTaskById(Long id) throws ManagerSaveException;

    public void deleteEpicById(Long id) throws ManagerSaveException;

    public void deleteSubtaskById(Long id) throws ManagerSaveException;

    public List<Task> getHistory();

    public Set<Task> getPrioritizedTasks();
}
