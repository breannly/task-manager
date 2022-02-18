import controller.Manager;
import model.Epic;
import model.Subtask;
import model.Task;

public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();

        Task task1 = new Task("Уборка", "10 минут", "NEW");
        manager.createTask(task1);
        Task task2 = new Task("Учеба", "физика", "NEW");
        manager.createTask(task2);
        Epic epic1 = new Epic("Подарок", "Маше");
        manager.createEpic(epic1);
        Subtask subtask1 = new Subtask("Ткань", "Купить", "NEW", epic1.getId());
        manager.createSubtask(subtask1);
        Subtask subtask2 = new Subtask("Рукоделие", "Cшить сумку", "NEW", epic1.getId());
        manager.createSubtask(subtask2);
        Epic epic2 = new Epic("Поучиться", "взять ноут");
        manager.createEpic(epic2);
        Subtask subtask3 = new Subtask("Cходить в кафе", "Поесть перед", "NEW", epic2.getId());
        manager.createSubtask(subtask3);

        System.out.println(manager.getTasks());
        System.out.println(manager.getEpics());
        System.out.println(manager.getSubtasks());

        task1.setStatus("DONE");
        task2.setStatus("IN_PROGRESS");
        subtask1.setStatus("DONE");
        subtask2.setStatus("IN_PROGRESS");
        subtask3.setStatus("DONE");

        manager.updateTask(task1);
        manager.updateTask(task2);
        manager.updateSubtask(subtask1);
        manager.updateSubtask(subtask2);
        manager.updateSubtask(subtask3);

        System.out.println(manager.getTasks());
        System.out.println(manager.getEpics());
        System.out.println(manager.getSubtasks());

        manager.deleteEpicById(epic1.getId());
        manager.deleteTaskById(task1.getId());

        System.out.println(manager.getTasks());
        System.out.println(manager.getEpics());
        System.out.println(manager.getSubtasks());
    }
}