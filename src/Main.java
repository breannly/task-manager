import controller.Managers;
import controller.TaskManager;
import model.Epic;
import model.Subtask;
import model.Task;

public class Main {
    public static void main(String[] args) {
        final TaskManager taskManager = Managers.getDefault();

        Task task1 = new Task("Уборка", "10 минут", "NEW");
        taskManager.createTask(task1);
        Task task2 = new Task("Учеба", "физика", "NEW");
        taskManager.createTask(task2);
        Epic epic1 = new Epic("Подарок", "Маше");
        taskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("Ткань", "Купить", "NEW", epic1.getId());
        taskManager.createSubtask(subtask1);
        Subtask subtask2 = new Subtask("Рукоделие", "Cшить сумку", "NEW", epic1.getId());
        taskManager.createSubtask(subtask2);
        Epic epic2 = new Epic("Поучиться", "взять ноут"); // 6
        taskManager.createEpic(epic2);
        Subtask subtask3 = new Subtask("Cходить в кафе", "Поесть перед", "NEW", epic2.getId());
        taskManager.createSubtask(subtask3);

        taskManager.getTaskById(2L);
        taskManager.getTaskById(1L);
        taskManager.getTaskById(2L);
        System.out.println(taskManager.history());
        taskManager.getEpicById(3L);
        taskManager.getTaskById(1L);
        taskManager.getTaskById(2L);
        System.out.println(taskManager.history());
        taskManager.getSubtaskById(7L);
        taskManager.getTaskById(2L);
        taskManager.getTaskById(1L);
        taskManager.getTaskById(2L);
        taskManager.getTaskById(1L);
        System.out.println(taskManager.history());
    }
}