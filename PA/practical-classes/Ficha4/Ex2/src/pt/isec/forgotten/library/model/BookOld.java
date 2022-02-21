package pt.isec.forgotten.library.model;

import java.util.List;

public class BookOld extends Book {
    int nrCopias;

    public BookOld(String titulo, List<String> autores, int copias) {
        super(titulo, autores);
        nrCopias = copias;
    }

    public int getNrCopias() {
        return nrCopias;
    }

    public void setNrCopias(int nrCopias) {
        this.nrCopias = nrCopias;
    }

    @Override
    public String toString() {
        return super.toString()+" => BookOld{" +
                "nrCopias=" + nrCopias +
                "}";
    }
}
