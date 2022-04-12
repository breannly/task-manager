package model.node;

public class HistoryNode<T> {
    public T data;
    public HistoryNode<T> next;
    public HistoryNode<T> prev;

    public HistoryNode(T data) {
        this.data = data;
        this.prev = null;
        this.next = null;
    }
}
