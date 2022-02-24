package pt.isec.forgotten.reais.dezreais;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

public class IteratorDezReais implements Iterator<Double> {
    private int index = 0;
    private final DezReais dezReais;
    private int nrModifications;

    IteratorDezReais(DezReais dezReais) {
        this.dezReais = dezReais;
        this.nrModifications = dezReais.getNrModifications();
    }

    public int nextPositiveIndex() {
        for (int i = index + 1; i < dezReais.size(); i++) {
            if (dezReais.at(i) > 0) return i;
        }

        return -1;
    }

    @Override
    public boolean hasNext() {
        return index < dezReais.size() && index != -1;
    }

    /*
    @Override
    public Double next() {
        if (nrModifications != dezReais.getNrModifications()) throw new ConcurrentModificationException();
        if (!hasNext()) throw new NoSuchElementException();

        return dezReais.at(index++);
    }
    */

    @Override
    public Double next() {
        if (nrModifications != dezReais.getNrModifications()) throw new ConcurrentModificationException();
        if (!hasNext()) throw new NoSuchElementException();

        int oldIndex = index;
        index = nextPositiveIndex();
        return dezReais.at(oldIndex);
    }

    @Override
    public void remove() throws IllegalStateException {
        dezReais.remove(index);
        nrModifications++;
    }

    @Override
    public void forEachRemaining(Consumer<? super Double> action) {
        Iterator.super.forEachRemaining(action);
    }
}
