package controller.imanager;

import controller.exception.FormatException;
import controller.exception.IntersectionTimeException;
import controller.exception.ManagerSaveException;
import controller.utility.Managers;
import model.entity.Epic;
import model.entity.Subtask;
import model.entity.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

public abstract class TaskManagerTest<T extends TaskManager> {
    private static TaskManager taskManager;

    @BeforeEach
    public void beforeEach() throws ManagerSaveException {
        taskManager = Managers.getDefault();
    }

    @Test
    public void shouldAddTask() throws ManagerSaveException, FormatException, IntersectionTimeException {
        Task task = new Task("test",
                "test",
                "NEW",
                0,
                null);
        taskManager.addTask(task);

        final List<Task> tasks = taskManager.getTasksList();

        Assertions.assertEquals(1, tasks.size(), "Неверное количество задач");
        Assertions.assertEquals(task, tasks.get(0), "Задачи не совпадают");
    }

    @Test
    public void shouldAddEpic() throws ManagerSaveException, IntersectionTimeException {
        Epic epic = new Epic("test", "test");
        taskManager.addEpic(epic);

        final List<Epic> epics = taskManager.getEpicsList();

        Assertions.assertEquals("NEW", epic.getStatus(), "Статус не совпадает");
        Assertions.assertEquals(1, epics.size(), "Неверное количество задач");
        Assertions.assertEquals(epic, epics.get(0), "Задачи не совпадают");
    }

    @Test
    public void shouldAddSubtask() throws ManagerSaveException, IntersectionTimeException, FormatException {
        Epic epic = new Epic("test", "test");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("test",
                "test",
                "NEW",
                epic.getId(),
                0,
                null);
        taskManager.addSubtask(subtask);

        final List<Subtask> subtasks = taskManager.getSubtasksList();

        Assertions.assertNotNull(subtask.getIdEpic());
        Assertions.assertEquals(epic.getId(), subtask.getIdEpic());
        Assertions.assertEquals(1, subtasks.size(), "Неверное количество задач");
        Assertions.assertEquals(subtask, subtasks.get(0), "Задачи не совпадают");
    }

    @Test
    public void shouldReturnEmptyTaskList() {
        final List<Task> tasks = taskManager.getTasksList();

        Assertions.assertEquals(0, tasks.size(), "Список не пуст");
    }

    @Test
    public void shouldReturnTaskList() throws ManagerSaveException, FormatException, IntersectionTimeException {
        Task task = new Task("test",
                "test",
                "NEW",
                0,
                null);
        taskManager.addTask(task);

        final List<Task> tasks = taskManager.getTasksList();

        Assertions.assertNotNull(tasks, "Список пуст");
        Assertions.assertEquals(1, tasks.size(), "Неверное количество задач");
        Assertions.assertEquals(task, tasks.get(0), "Задачи не совпадают");
    }

    @Test
    public void shouldReturnEmptyEpicList() {
        final List<Epic> epics = taskManager.getEpicsList();

        Assertions.assertEquals(0, epics.size(), "Список не пуст");
    }

    @Test
    public void shouldReturnEpicList() throws ManagerSaveException, IntersectionTimeException {
        Epic epic = new Epic("test", "test");
        taskManager.addEpic(epic);

        final List<Epic> epics = taskManager.getEpicsList();

        Assertions.assertEquals("NEW", epic.getStatus(), "Статус не совпадает");
        Assertions.assertNotNull(epics, "Список пуст");
        Assertions.assertEquals(1, epics.size(), "Неверное количество задач");
        Assertions.assertEquals(epic, epics.get(0), "Задачи не совпадают");
    }

    @Test
    public void shouldReturnEmptySubtaskList() {
        final List<Subtask> subtasks = taskManager.getSubtasksList();

        Assertions.assertEquals(0, subtasks.size(), "Список не пуст");
    }

    @Test
    public void shouldReturnSubtaskList() throws ManagerSaveException, FormatException, IntersectionTimeException {
        Epic epic = new Epic("test", "test");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("test",
                "test",
                "NEW",
                epic.getId(),
                0,
                null);
        taskManager.addSubtask(subtask);

        final List<Subtask> subtasks = taskManager.getSubtasksList();

        Assertions.assertNotNull(subtask.getIdEpic());
        Assertions.assertEquals(epic.getId(), subtask.getIdEpic());
        Assertions.assertEquals(1, subtasks.size(), "Неверное количество задач");
        Assertions.assertEquals(subtask, subtasks.get(0), "Задачи не совпадают");
    }

    @Test
    public void shouldReturnEmptyTaskListProvidedThatNoTasksHaveBeenCreated() throws ManagerSaveException {
        taskManager.deleteTasks();

        final List<Task> tasks = taskManager.getTasksList();

        Assertions.assertEquals(0, tasks.size(), "Список не пуст");
    }

    @Test
    public void shouldReturnEmptyTaskListWhenDeletingAllTasks()
            throws ManagerSaveException, IntersectionTimeException, FormatException {
        Task task1 = new Task("test",
                "test",
                "NEW",
                0,
                null);
        Task task2 = new Task("test", "test", "NEW", 0, null);
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.deleteTasks();

        final List<Task> tasks = taskManager.getTasksList();

        Assertions.assertEquals(0, tasks.size(), "Список не пуст");
    }

    @Test
    public void shouldReturnEmptyEpicListProvidedThatNoEpicsHaveBeenCreated() throws ManagerSaveException {
        taskManager.deleteEpics();

        final List<Epic> epics = taskManager.getEpicsList();

        Assertions.assertEquals(0, epics.size(), "Список не пуст");
    }

    @Test
    public void shouldReturnEmptyEpicListWhenDeletingAllEpics() throws ManagerSaveException, IntersectionTimeException {
        Epic epic1 = new Epic("test", "test");
        Epic epic2 = new Epic("test", "test");
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        taskManager.deleteEpics();

        final List<Epic> epics = taskManager.getEpicsList();
        Assertions.assertEquals(0, epics.size(), "Список не пуст");
    }

    @Test
    public void shouldReturnEmptySubtaskListProvidedThatNoSubtasksHaveBeenCreated() throws ManagerSaveException {
        taskManager.deleteSubtasks();

        final List<Subtask> subtasks = taskManager.getSubtasksList();

        Assertions.assertEquals(0, subtasks.size(), "Список не пуст");
    }

    @Test
    public void shouldReturnEmptySubtaskListWhenDeletingAllSubtasks()
            throws ManagerSaveException, FormatException, IntersectionTimeException {
        Epic epic = new Epic("test", "test");
        taskManager.addEpic(epic);
        Subtask subtask1 = new Subtask("test",
                "test",
                "DONE",
                epic.getId(),
                0,
                null);
        Subtask subtask2 = new Subtask("test",
                "test",
                "NEW",
                epic.getId(),
                0,
                null);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        taskManager.deleteSubtasks();

        final List<Subtask> subtasks = taskManager.getSubtasksList();

        Assertions.assertEquals("NEW", epic.getStatus(), "Статус не обновился");
        Assertions.assertEquals(0, subtasks.size(), "Список не пуст");
        Assertions.assertEquals(0, epic.getSubtask().size(), "Подзадачи не удалились");
    }

    @Test
    public void shouldReturnTaskById() throws ManagerSaveException, IntersectionTimeException, FormatException {
        Task task = new Task("test",
                "test",
                "NEW",
                0,
                null);
        taskManager.addTask(task);
        Task foundTask = taskManager.getTaskById(task.getId());

        Assertions.assertNotNull(foundTask, "Задача не возвращается");
        Assertions.assertEquals(task, foundTask, "Задачи не совпадают");
    }

    @Test
    public void shouldReturnNullIfNonexistentTaskId()
            throws ManagerSaveException, IntersectionTimeException, FormatException {
        Task task = new Task("test",
                "test",
                "NEW",
                0,
                null);
        taskManager.addTask(task);
        Task foundTask1 = taskManager.getTaskById(2L);
        Task foundTask2 = taskManager.getTaskById(null);

        Assertions.assertNull(foundTask1);
        Assertions.assertNull(foundTask2);
        Assertions.assertNotEquals(task, foundTask1);
        Assertions.assertNotEquals(task, foundTask2);
    }

    @Test
    public void shouldReturnEpicById() throws ManagerSaveException, IntersectionTimeException {
        Epic epic = new Epic("test", "test");
        taskManager.addEpic(epic);
        Epic foundEpic = taskManager.getEpicById(epic.getId());

        Assertions.assertNotNull(foundEpic, "Задача не возвращается");
        Assertions.assertEquals(epic, foundEpic, "Задачи не совпадают");
    }

    @Test
    public void shouldReturnNullIfNonexistentEpicId() throws ManagerSaveException, IntersectionTimeException {
        Epic epic = new Epic("test", "test");
        taskManager.addEpic(epic);
        Epic foundEpic1 = taskManager.getEpicById(2L);
        Epic foundEpic2 = taskManager.getEpicById(null);

        Assertions.assertNull(foundEpic1);
        Assertions.assertNull(foundEpic2);
        Assertions.assertNotEquals(epic, foundEpic1);
        Assertions.assertNotEquals(epic, foundEpic2);
    }

    @Test
    public void shouldReturnSubtaskById() throws ManagerSaveException, IntersectionTimeException, FormatException {
        Epic epic = new Epic("test", "test");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("test",
                "test",
                "NEW",
                epic.getId(),
                0,
                null);
        taskManager.addSubtask(subtask);
        Subtask foundSubtask = taskManager.getSubtaskById(subtask.getId());

        Assertions.assertNotNull(foundSubtask, "Задача не возвращается");
        Assertions.assertEquals(subtask, foundSubtask, "Задачи не совпадают");
    }

    @Test
    public void shouldReturnNullIfNonexistentSubtaskId()
            throws ManagerSaveException, IntersectionTimeException, FormatException {
        Epic epic = new Epic("test", "test");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("test",
                "test",
                "NEW",
                epic.getId(),
                0,
                null);
        taskManager.addSubtask(subtask);
        Subtask foundSubtask1 = taskManager.getSubtaskById(3L);
        Subtask foundSubtask2 = taskManager.getSubtaskById(null);

        Assertions.assertNull(foundSubtask1);
        Assertions.assertNull(foundSubtask2);
        Assertions.assertNotEquals(subtask, foundSubtask1);
        Assertions.assertNotEquals(subtask, foundSubtask2);
    }

    @Test
    public void shouldUpdateTask() throws ManagerSaveException, IntersectionTimeException, FormatException {
        Task task = new Task("test",
                "test",
                "NEW",
                0,
                null);
        taskManager.addTask(task);
        task.setName("test1");
        task.setDescription("test1");
        task.setStatus("DONE");
        taskManager.updateTask(task);

        Assertions.assertEquals(task, taskManager.getTaskById(task.getId()), "Задача не обновилась");
    }

    @Test
    public void shouldUpdateEpic() throws ManagerSaveException, IntersectionTimeException {
        Epic epic = new Epic("test", "test");
        taskManager.addEpic(epic);
        epic.setName("test1");
        epic.setDescription("test1");
        taskManager.updateEpic(epic);

        Assertions.assertEquals(epic, taskManager.getEpicById(epic.getId()), "Задача не обновилась");
    }

    @Test
    public void shouldUpdateSubtask() throws ManagerSaveException, IntersectionTimeException, FormatException {
        Epic epic = new Epic("test", "test");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("test",
                "test",
                "NEW",
                epic.getId(),
                0,
                null);
        taskManager.addSubtask(subtask);
        subtask.setName("test1");
        subtask.setDescription("test1");
        subtask.setStatus("DONE");
        taskManager.updateSubtask(subtask);

        Assertions.assertEquals("DONE", epic.getStatus(), "Статус не поменялся");
        Assertions.assertEquals(subtask, taskManager.getSubtaskById(subtask.getId()), "Задача не обновилась");
    }

    @Test
    public void shouldDeleteTaskById() throws ManagerSaveException, IntersectionTimeException, FormatException {
        Task task1 = new Task("test", "test", "NEW", 0, null);
        Task task2 = new Task("test", "test", "NEW", 0, null);
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.deleteTaskById(task1.getId());

        final List<Task> tasks = taskManager.getTasksList();

        Assertions.assertNotEquals(2, tasks.size(), "Задача не удалилась");
        Assertions.assertEquals(task2, tasks.get(0), "Задача не удалилась");
    }

    @Test
    public void shouldReturnUnchangedListIfPassingNonexistentTaskId()
            throws ManagerSaveException, IntersectionTimeException, FormatException {
        Task task1 = new Task("test", "test", "NEW", 0, null);
        Task task2 = new Task("test", "test", "NEW", 0, null);
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.deleteTaskById(3L);
        taskManager.deleteTaskById(null);

        final List<Task> tasks = taskManager.getTasksList();

        Assertions.assertEquals(2, tasks.size());
    }

    @Test
    public void shouldDeleteEpicById() throws ManagerSaveException, IntersectionTimeException {
        Epic epic1 = new Epic("test", "test");
        Epic epic2 = new Epic("test", "test");
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        taskManager.deleteEpicById(epic1.getId());

        final List<Epic> epics = taskManager.getEpicsList();

        Assertions.assertNotEquals(2, epics.size(), "Задача не удалилась");
        Assertions.assertEquals(epic2, epics.get(0), "Задача не удалилась");
    }

    @Test
    public void shouldReturnUnchangedListIfPassingNonexistentEpicId()
            throws ManagerSaveException, IntersectionTimeException {
        Epic epic1 = new Epic("test", "test");
        Epic epic2 = new Epic("test", "test");
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        taskManager.deleteEpicById(3L);
        taskManager.deleteEpicById(null);

        final List<Epic> epics = taskManager.getEpicsList();

        Assertions.assertEquals(2, epics.size());
    }

    @Test
    public void shouldDeleteSubtaskById() throws ManagerSaveException, IntersectionTimeException, FormatException {
        Epic epic = new Epic("test", "test");
        taskManager.addEpic(epic);
        Subtask subtask1 = new Subtask("test",
                "test",
                "IN_PROGRESS",
                epic.getId(),
                0,
                null);
        Subtask subtask2 = new Subtask("test",
                "test",
                "DONE",
                epic.getId(),
                0,
                null);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        taskManager.deleteSubtaskById(subtask1.getId());

        final List<Subtask> subtasks = taskManager.getSubtasksList();

        Assertions.assertEquals(1, epic.getSubtask().size(), "Задача не удалилась у эпика");
        Assertions.assertNull(epic.getSubtask().get(subtask1.getId()), "Задача не удалилась у эпика");
        Assertions.assertNotEquals(2, subtasks.size(), "Задача не удалилась в списке");
        Assertions.assertEquals(subtask2, subtasks.get(0), "Задача не удалилась в списке");
        Assertions.assertEquals("DONE", epic.getStatus(), "Статус не изменился");
    }

    @Test
    public void shouldReturnUnchangedListIfPassingNonexistentSubtaskId()
            throws ManagerSaveException, IntersectionTimeException, FormatException {
        Epic epic = new Epic("test", "test");
        taskManager.addEpic(epic);
        Subtask subtask1 = new Subtask("test",
                "test",
                "IN_PROGRESS",
                epic.getId(),
                0,
                null);
        Subtask subtask2 = new Subtask("test",
                "test",
                "DONE",
                epic.getId(),
                0,
                null);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        taskManager.deleteSubtaskById(4L);
        taskManager.deleteSubtaskById(null);

        final List<Subtask> subtasks = taskManager.getSubtasksList();

        Assertions.assertEquals(2, epic.getSubtask().size());
        Assertions.assertEquals(2, subtasks.size());
    }

    @Test
    public void shouldReturnSortedListByStartTime()
            throws FormatException, IntersectionTimeException, ManagerSaveException {
        Task task1 = new Task("test", "test", "NEW", 0, null);
        Task task2 = new Task("test", "test", "NEW", 90, "30.04.22 12:00");
        Task task3 = new Task("test", "test", "NEW", 90, "29.04.22 21:00");
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addTask(task3);
        Set<Task> sortedTasks = taskManager.getPrioritizedTasks();
        Task[] tasks = sortedTasks.toArray(new Task[0]);

        Assertions.assertEquals(task3, tasks[0], "Первый элемент не совпадает");
        Assertions.assertEquals(task2, tasks[1], "Второй элемент не совпадает");
        Assertions.assertEquals(task1, tasks[2], "Третий элемент не совпадает");
    }
}