package pt.isec.forgotten;

import java.util.List;
import java.util.ListIterator;

public class Pilha<T> {
    private List<? super T> pilhinha;

    public Pilha(List<? super T> pilhinha) {
        this.pilhinha = pilhinha;
        pilhinha.clear();
    }

    public boolean empty() { return pilhinha.size() == 0; }

    public T peek() { return (T) pilhinha.get(pilhinha.size() - 1); }

    public T pop() { return (T) pilhinha.remove(pilhinha.size() - 1); }

    public void push(T obj) { pilhinha.add(0, obj); }
}
