package pt.isec.forgotten.tree;

public class Node<T> {
    private T data;

    private Node<T> left;
    private Node<T> right;

    public Node(T data) { this.data = data; }

    public T getData() { return data; }
    public Node<T> getLeft() { return left; }
    public Node<T> getRight() { return right; }

    public void setData(T data) { this.data = data; }
    public void setLeft(Node<T> node) { left = node; }
    public void setRight(Node<T> node) { right = node; }
}
