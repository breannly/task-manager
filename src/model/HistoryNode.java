package model;

public class HistoryNode<T> {
    private T data;
    private HistoryNode<T> next;
    private HistoryNode<T> prev;

    public HistoryNode(T data) {
        this.data = data;
        this.next = null;
        this.prev = null;
    }

    private void removeNode(HistoryNode<T> node) {
        HistoryNode<T> prevNode = node.prev;
        HistoryNode<T> nextNode = node.next;

        if (prevNode != null) {
            prevNode.next = node.next;
        }

        if (nextNode != null) {
            nextNode.prev = node.prev;
        }
    }

    private void removeRoot(HistoryNode<T> root) {
        HistoryNode<T> nextNode = root.next;
        nextNode.prev = null;
    }

    public void remove(HistoryNode<T> node) {
        if (node.prev == null) {
            removeRoot(node);
            return;
        }

        removeNode(node);
    }

}
