package pt.isec.forgotten.reais.dezreais;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

public class DezReais implements Iterable<Double> {
    private final Double[] a = new Double[10];
    private int size = 0;
    private int nrModifications = 0;

    public void add(double ... elements) {
        if (size == 10) return;

        for (double element : elements) a[size++] = element;

        nrModifications++;
    }

    public int size() { return size; }
    public double at(int index) { return a[index]; }
    public int getNrModifications() { return nrModifications; }

    public void remove(int index) {
        if (index >= size) throw new IllegalStateException();

        for (int i = index; i < size; i++) {
            if ((i + 1) >= size) a[i] = null;

            a[i] = a[i + 1];
        }

        size--;
        nrModifications++;
    }

    @Override
    public Iterator<Double> iterator() {
        return new IteratorDezReais(this);
    }

    @Override
    public void forEach(Consumer action) {
        Iterable.super.forEach(action);
    }

    @Override
    public Spliterator<Double> spliterator() {
        return Iterable.super.spliterator();
    }
}
