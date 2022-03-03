import controller.InMemoryTaskManager;
import model.Epic;
import model.Subtask;
import model.Task;

public class Main {
    public static void main(String[] args) {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

        Task task1 = new Task("Уборка", "10 минут", "NEW");
        inMemoryTaskManager.createTask(task1);
        Task task2 = new Task("Учеба", "физика", "NEW");
        inMemoryTaskManager.createTask(task2);
        Epic epic1 = new Epic("Подарок", "Маше");
        inMemoryTaskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("Ткань", "Купить", "NEW", epic1.getId());
        inMemoryTaskManager.createSubtask(subtask1);
        Subtask subtask2 = new Subtask("Рукоделие", "Cшить сумку", "NEW", epic1.getId());
        inMemoryTaskManager.createSubtask(subtask2);
        Epic epic2 = new Epic("Поучиться", "взять ноут");
        inMemoryTaskManager.createEpic(epic2);
        Subtask subtask3 = new Subtask("Cходить в кафе", "Поесть перед", "NEW", epic2.getId());
        inMemoryTaskManager.createSubtask(subtask3);

        System.out.println(inMemoryTaskManager.getTasks());
        System.out.println(inMemoryTaskManager.getEpics());
        System.out.println(inMemoryTaskManager.getSubtasks());

        task1.setStatus("DONE");
        task2.setStatus("IN_PROGRESS");
        subtask1.setStatus("DONE");
        subtask2.setStatus("IN_PROGRESS");
        subtask3.setStatus("DONE");

        inMemoryTaskManager.updateTask(task1);
        inMemoryTaskManager.updateTask(task2);
        inMemoryTaskManager.updateSubtask(subtask1);
        inMemoryTaskManager.updateSubtask(subtask2);
        inMemoryTaskManager.updateSubtask(subtask3);

        System.out.println(inMemoryTaskManager.getTasks());
        System.out.println(inMemoryTaskManager.getEpics());
        System.out.println(inMemoryTaskManager.getSubtasks());

        inMemoryTaskManager.deleteEpicById(epic1.getId());
        inMemoryTaskManager.deleteTaskById(task1.getId());

        System.out.println(inMemoryTaskManager.getTasks());
        System.out.println(inMemoryTaskManager.getEpics());
        System.out.println(inMemoryTaskManager.getSubtasks());
    }
}