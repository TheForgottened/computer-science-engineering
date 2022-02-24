package pt.isec.forgotten;

import java.util.List;

public class Fila<T> {
    private List<? super T> filinha;

    public Fila(List<? super T> filinha) {
        this.filinha = filinha;
        filinha.clear();
    }

    public boolean empty() { return filinha.size() == 0; }

    public void add(T obj) { filinha.add(obj); }

    public T remove(T obj) { return (T) filinha.remove(0); }

    public T element() { return (T) filinha.get(0); }
}
