package controller.imanager;

import model.entity.Epic;
import model.entity.Subtask;
import model.entity.Task;

import java.util.List;

public interface TaskManager {

    public List<Task> getTasks();

    public List<Subtask> getSubtasks();

    public List<Epic> getEpics();

    public void deleteTasks();

    public void deleteSubtasks();

    public void deleteEpics();

    public Task getTaskById(Long id);

    public Epic getEpicById(Long id);

    public Subtask getSubtaskById(Long id);

    public void updateTask(Task task);

    public void updateEpic(Epic epic);

    public void updateSubtask(Subtask subtask);

    public Task addTask(Task task);

    public Epic addEpic(Epic epic);

    public Subtask addSubtask(Subtask subtask);

    public void deleteTaskById(Long id);

    public void deleteEpicById(Long id);

    public void deleteSubtaskById(Long id);

    public List<Task> getHistory();
}
