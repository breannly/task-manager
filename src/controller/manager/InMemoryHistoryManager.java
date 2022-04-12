package controller.manager;

import controller.imanager.HistoryManager;
import model.node.HistoryNode;
import model.entity.Task;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryHistoryManager implements HistoryManager {
    private final HistoryLinkedList<Task> historyList;
    private final Map<Long, HistoryNode<Task>> historyMap;

    public InMemoryHistoryManager() {
        historyList = new HistoryLinkedList<>();
        historyMap = new HashMap<>();
    }

    @Override
    public List<Task> getHistory() {
        return historyList.getTasks();
    }

    @Override
    public void add(Task task) {
        if (historyMap.containsKey(task.getId())) {
            remove(task.getId());
        }

        historyMap.put(task.getId(), historyList.linkLast(task));
    }

    @Override
    public void remove(Long id) {
        if (historyMap.containsKey(id)) {
            HistoryNode<Task> node = historyMap.get(id);
            historyList.removeNode(node);
        }
    }

    public static String toString(HistoryManager manager) {
        String history = " ";
        if (!manager.getHistory().isEmpty()) {
            history = manager.getHistory()
                    .stream()
                    .map(x -> x.getId().toString())
                    .collect(Collectors.joining(","));
        }
        return history;
    }

    public static List<Long> fromString(String value) {
        List<Long> historyList = new LinkedList<>();
        String[] history = value.split(",");

        for (String taskId : history) {
            historyList.add(Long.parseLong(taskId));
        }
        return historyList;
    }

    class HistoryLinkedList<T> {
        private HistoryNode<T> head;
        private HistoryNode<T> tail;

        public HistoryLinkedList() {
            head = null;
            tail = null;
        }

        private HistoryNode<T> linkLast(T t) {
            HistoryNode<T> newNode = new HistoryNode<>(t);

            if (head == null) {
                head = newNode;
            } else {
                tail.next = newNode;
                newNode.prev = tail;
            }

            tail = newNode;

            return newNode;
        }

        private List<T> getTasks() {
            List<T> task = new ArrayList<T>();
            HistoryNode<T> node = head;

            while (node != null) {
                task.add(node.data);
                node = node.next;
            }

            return task;
        }

        private void removeNode(HistoryNode<T> node) {
            HistoryNode<T> prevNode = node.prev;
            HistoryNode<T> nextNode = node.next;

            if (prevNode != null) {
                node.prev.next = nextNode;
            } else {
                head = node.next;
            }

            if (nextNode != null) {
                node.next.prev = prevNode;
            } else {
                tail = node.prev;
            }
        }
    }

}
