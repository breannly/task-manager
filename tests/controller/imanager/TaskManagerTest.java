package controller.imanager;

import controller.exception.FormatException;
import controller.exception.IntersectionTimeException;
import controller.exception.ManagerSaveException;
import controller.manager.InMemoryTaskManager;
import controller.utility.Managers;
import model.entity.Epic;
import model.entity.Subtask;
import model.entity.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public abstract class TaskManagerTest<T extends TaskManager> {
    private static TaskManager taskManager;

    @BeforeEach
    void beforeEach() throws ManagerSaveException, IOException, InterruptedException {
        taskManager = new InMemoryTaskManager();
    }

    @Test
    void shouldAddTask()
            throws ManagerSaveException, FormatException, IntersectionTimeException, IOException, InterruptedException {
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
    void shouldAddEpic() throws ManagerSaveException, IntersectionTimeException, IOException, InterruptedException {
        Epic epic = new Epic("test", "test");
        taskManager.addEpic(epic);

        final List<Epic> epics = taskManager.getEpicsList();

        Assertions.assertEquals("NEW", epic.getStatus(), "Статус не совпадает");
        Assertions.assertEquals(1, epics.size(), "Неверное количество задач");
        Assertions.assertEquals(epic, epics.get(0), "Задачи не совпадают");
    }

    @Test
    void shouldAddSubtask()
            throws ManagerSaveException, IntersectionTimeException, FormatException, IOException, InterruptedException {
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
    void shouldReturnEmptyTaskList() {
        final List<Task> tasks = taskManager.getTasksList();

        Assertions.assertEquals(0, tasks.size(), "Список не пуст");
    }

    @Test
    void shouldReturnTaskList()
            throws ManagerSaveException, FormatException, IntersectionTimeException, IOException, InterruptedException {
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
    void shouldReturnEmptyEpicList() {
        final List<Epic> epics = taskManager.getEpicsList();

        Assertions.assertEquals(0, epics.size(), "Список не пуст");
    }

    @Test
    void shouldReturnEpicList()
            throws ManagerSaveException, IntersectionTimeException, IOException, InterruptedException {
        Epic epic = new Epic("test", "test");
        taskManager.addEpic(epic);

        final List<Epic> epics = taskManager.getEpicsList();

        Assertions.assertEquals("NEW", epic.getStatus(), "Статус не совпадает");
        Assertions.assertNotNull(epics, "Список пуст");
        Assertions.assertEquals(1, epics.size(), "Неверное количество задач");
        Assertions.assertEquals(epic, epics.get(0), "Задачи не совпадают");
    }

    @Test
    void shouldReturnEmptySubtaskList() {
        final List<Subtask> subtasks = taskManager.getSubtasksList();

        Assertions.assertEquals(0, subtasks.size(), "Список не пуст");
    }

    @Test
    void shouldReturnSubtaskList()
            throws ManagerSaveException, FormatException, IntersectionTimeException, IOException, InterruptedException {
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
    void shouldReturnEmptyTaskListProvidedThatNoTasksHaveBeenCreated()
            throws ManagerSaveException, IOException, InterruptedException {
        taskManager.deleteTasks();

        final List<Task> tasks = taskManager.getTasksList();

        Assertions.assertEquals(0, tasks.size(), "Список не пуст");
    }

    @Test
    void shouldReturnEmptyTaskListWhenDeletingAllTasks()
            throws ManagerSaveException, IntersectionTimeException, FormatException, IOException, InterruptedException {
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
    void shouldReturnEmptyEpicListProvidedThatNoEpicsHaveBeenCreated()
            throws ManagerSaveException, IOException, InterruptedException {
        taskManager.deleteEpics();

        final List<Epic> epics = taskManager.getEpicsList();

        Assertions.assertEquals(0, epics.size(), "Список не пуст");
    }

    @Test
    void shouldReturnEmptyEpicListWhenDeletingAllEpics()
            throws ManagerSaveException, IntersectionTimeException, IOException, InterruptedException {
        Epic epic1 = new Epic("test", "test");
        Epic epic2 = new Epic("test", "test");
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        taskManager.deleteEpics();

        final List<Epic> epics = taskManager.getEpicsList();
        Assertions.assertEquals(0, epics.size(), "Список не пуст");
    }

    @Test
    void shouldReturnEmptySubtaskListProvidedThatNoSubtasksHaveBeenCreated()
            throws ManagerSaveException, IOException, InterruptedException {
        taskManager.deleteSubtasks();

        final List<Subtask> subtasks = taskManager.getSubtasksList();

        Assertions.assertEquals(0, subtasks.size(), "Список не пуст");
    }

    @Test
    void shouldReturnEmptySubtaskListWhenDeletingAllSubtasks()
            throws ManagerSaveException, FormatException, IntersectionTimeException, IOException, InterruptedException {
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
    void shouldReturnTaskById()
            throws ManagerSaveException, IntersectionTimeException, FormatException, IOException, InterruptedException {
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
    void shouldReturnNullIfNonexistentTaskId()
            throws ManagerSaveException, IntersectionTimeException, FormatException, IOException, InterruptedException {
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
    void shouldReturnEpicById()
            throws ManagerSaveException, IntersectionTimeException, IOException, InterruptedException {
        Epic epic = new Epic("test", "test");
        taskManager.addEpic(epic);
        Epic foundEpic = taskManager.getEpicById(epic.getId());

        Assertions.assertNotNull(foundEpic, "Задача не возвращается");
        Assertions.assertEquals(epic, foundEpic, "Задачи не совпадают");
    }

    @Test
    void shouldReturnNullIfNonexistentEpicId()
            throws ManagerSaveException, IntersectionTimeException, IOException, InterruptedException {
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
    void shouldReturnSubtaskById()
            throws ManagerSaveException, IntersectionTimeException, FormatException, IOException, InterruptedException {
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
    void shouldReturnNullIfNonexistentSubtaskId()
            throws ManagerSaveException, IntersectionTimeException, FormatException, IOException, InterruptedException {
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
    void shouldUpdateTask()
            throws ManagerSaveException, IntersectionTimeException, FormatException, IOException, InterruptedException {
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
    void shouldUpdateEpic() throws ManagerSaveException, IntersectionTimeException, IOException, InterruptedException {
        Epic epic = new Epic("test", "test");
        taskManager.addEpic(epic);
        epic.setName("test1");
        epic.setDescription("test1");
        taskManager.updateEpic(epic);

        Assertions.assertEquals(epic, taskManager.getEpicById(epic.getId()), "Задача не обновилась");
    }

    @Test
    void shouldUpdateSubtask()
            throws ManagerSaveException, IntersectionTimeException, FormatException, IOException, InterruptedException {
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
    void shouldDeleteTaskById()
            throws ManagerSaveException, IntersectionTimeException, FormatException, IOException, InterruptedException {
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
    void shouldReturnUnchangedListIfPassingNonexistentTaskId()
            throws ManagerSaveException, IntersectionTimeException, FormatException, IOException, InterruptedException {
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
    void shouldDeleteEpicById()
            throws ManagerSaveException, IntersectionTimeException, IOException, InterruptedException {
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
    void shouldReturnUnchangedListIfPassingNonexistentEpicId()
            throws ManagerSaveException, IntersectionTimeException, IOException, InterruptedException {
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
    void shouldDeleteSubtaskById()
            throws ManagerSaveException, IntersectionTimeException, FormatException, IOException, InterruptedException {
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
    void shouldReturnUnchangedListIfPassingNonexistentSubtaskId()
            throws ManagerSaveException, IntersectionTimeException, FormatException, IOException, InterruptedException {
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
    void shouldReturnSortedListByStartTime()
            throws FormatException, IntersectionTimeException, ManagerSaveException, IOException, InterruptedException {
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