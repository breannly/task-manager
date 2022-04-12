package controller.imanager;

import controller.exception.ManagerSaveException;
import model.entity.Epic;
import model.entity.Subtask;
import model.entity.Task;

import java.util.List;

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

    public void updateTask(Task task) throws ManagerSaveException;

    public void updateEpic(Epic epic) throws ManagerSaveException;

    public void updateSubtask(Subtask subtask) throws ManagerSaveException;

    public Task addTask(Task task) throws ManagerSaveException;

    public Epic addEpic(Epic epic) throws ManagerSaveException;

    public Subtask addSubtask(Subtask subtask) throws ManagerSaveException;

    public void deleteTaskById(Long id) throws ManagerSaveException;

    public void deleteEpicById(Long id) throws ManagerSaveException;

    public void deleteSubtaskById(Long id) throws ManagerSaveException;

    public List<Task> getHistory();
}
