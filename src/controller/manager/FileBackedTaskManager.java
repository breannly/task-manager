package controller.manager;

import controller.imanager.TaskManager;
import model.entity.Epic;
import model.entity.Subtask;
import model.entity.Task;

import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {

    @Override
    public List<Task> getTasks() {
        return super.getTasks();
    }

    @Override
    public List<Subtask> getSubtasks() {
        return super.getSubtasks();
    }

    @Override
    public List<Epic> getEpics() {
        return super.getEpics();
    }

    @Override
    public void deleteTasks() {
        super.deleteTasks();
    }

    @Override
    public void deleteSubtasks() {
        super.deleteSubtasks();
    }

    @Override
    public void deleteEpics() {
        super.deleteEpics();
    }

    @Override
    public Task getTaskById(Long id) {
        return super.getTaskById(id);
    }

    @Override
    public Epic getEpicById(Long id) {
        return super.getEpicById(id);
    }

    @Override
    public Subtask getSubtaskById(Long id) {
        return super.getSubtaskById(id);
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
    }

    @Override
    public Task addTask(Task task) {
        return super.addTask(task);
    }

    @Override
    public Epic addEpic(Epic epic) {
        return super.addEpic(epic);
    }

    @Override
    public Subtask addSubtask(Subtask subtask) {
        return super.addSubtask(subtask);
    }

    @Override
    public void deleteTaskById(Long id) {
        super.deleteTaskById(id);
    }

    @Override
    public void deleteEpicById(Long id) {
        super.deleteEpicById(id);
    }

    @Override
    public void deleteSubtaskById(Long id) {
        super.deleteSubtaskById(id);
    }

    @Override
    public List<Task> getHistory() {
        return super.getHistory();
    }
}
