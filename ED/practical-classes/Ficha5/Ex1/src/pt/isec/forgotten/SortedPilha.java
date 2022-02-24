package pt.isec.forgotten;

import java.util.ArrayList;

public class SortedPilha<T extends Comparable<? super T>> {
    Pilha<T> val = new Pilha<>(new ArrayList<T>());
    Pilha<T> min = new Pilha<>(new ArrayList<T>());

    public void push(T v) {
        if (min.empty() || min.peek().compareTo(v) >= 0) {
            min.push(v);
        }
        val.push(v);
    }

    public T pop() {
        T ret = val.pop();

        if (ret.compareTo(min.peek()) == 0) {
            min.pop();
        }

        return ret;
    }

    public T getMin() {
        return min.peek();
    }
}
