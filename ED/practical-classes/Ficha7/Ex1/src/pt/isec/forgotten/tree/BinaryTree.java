package pt.isec.forgotten.tree;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class BinaryTree<T extends Comparable<? super T>> {
    private Node<T> root = null;
    private final Comparator<T> comparator;

    BinaryTree(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    BinaryTree() {
        comparator = Comparator.naturalOrder();
    }

    public void insert(T data) {
        Node<T> node = new Node<>(data);
        root = insert(root, node);
    }

    private Node<T> insert(Node<T> root, Node<T> newNode) throws RuntimeException {
        if (root == null) return newNode;

        int compare = comparator.compare(root.getData(), newNode.getData());
        if (compare == 0) throw new RuntimeException();
        if (compare > 0) {
            root.setLeft(insert(root.getLeft(), newNode));
        } else {
            root.setRight(insert(root.getRight(), newNode));
        }

        return root;
    }

    public void remove(T data) {
        root = remove(root, data);
    }

    private Node<T> remove(Node<T> root, T data) {
        if (root == null) return null;

        int compare = comparator.compare(data, root.getData());

        if (compare < 0) {
            root.setLeft(remove(root.getLeft(), data));
            return root;
        } else if (compare > 0) {
            root.setRight(remove(root.getRight(), data));
            return root;
        }

        if (root.getLeft() == null && root.getRight() == null) return null;

        if (root.getLeft() == null) {
            return root.getRight();
        } else if (root.getRight() == null) {
            return root.getLeft();
        }

        Node<T> min = minimumElement(root.getRight());
        root.setData(min.getData());
        root.setRight(remove(root.getRight(), min.getData()));

        return root;
    }

    private Node<T> minimumElement(Node<T> root) {
        if (root.getLeft() == null) return root;
        return minimumElement(root.getLeft());
    }

    public boolean contains(T data) {
        return containsData(root, data);
    }

    private boolean containsData(Node<T> root, T data) {
        if (root == null) return false;

        int compare = comparator.compare(data, root.getData());
        if (compare == 0) return true;

        if (compare < 0) return containsData(root.getLeft(), data);
        return containsData(root.getRight(), data);
    }

    public int depthOf(T data) { return depthOf(root, data); }

    private int depthOf(Node<T> root, T data) {
        int d;

        if (root == null) return 0;
        if (data.compareTo(root.getData()) == 0) return 1;

        d = depthOf(root.getLeft(), data);
        if (d > 0) return d + 1;

        d = depthOf(root.getRight(), data);
        if (d > 0) return d + 1;

        return 0;
    }

    public void printInOrder() {
        List<T> orderedList = new LinkedList<>();
        order(root, orderedList);
        printList(orderedList);
    }

    private void printList(List<T> list) {
        for (T item : list) {
            System.out.println(item);
        }
    }

    private void order(Node<T> root, List<T> storageList) {
        if (root == null) return;

        order(root.getLeft(), storageList);
        storageList.add(root.getData());
        order(root.getRight(), storageList);
    }

    private void preOrder(Node<T> root, List<T> storageList) {
        if (root == null) return;

        storageList.add(root.getData());
        preOrder(root.getLeft(), storageList);
        preOrder(root.getRight(), storageList);
    }

    private void posOrder(Node<T> root, List<T> storageList) {
        if (root == null) return;

        posOrder(root.getLeft(), storageList);
        posOrder(root.getRight(), storageList);
        storageList.add(root.getData());
    }

    public int getTreeDepth() {
        return getTreeDepth(root);
    }

    private int getTreeDepth(Node<T> root) {
        if (root == null) return 0;

        int left = getTreeDepth(root.getLeft());
        int right = getTreeDepth(root.getRight());

        return Math.max(left + 1, right + 1);
    }

    public int getTreeSize() {
        return getTreeSize(root);
    }

    private int getTreeSize(Node<T> root) {
        if (root == null) return 0;

        return getTreeSize(root) + getTreeSize(root) + 1;
    }

    public void printLevels() {
        List<Node<T>> list = new LinkedList<>();

        list.add(root);

        while(!list.isEmpty()) {
            Node<T> node = list.remove(0);
            System.out.print(node.getData() + " ");
            if (node.getLeft() != null) list.add(node.getLeft());
            if (node.getRight() != null) list.add(node.getRight());
        }
    }
}
