package pt.isec.forgotten.springboot;

public class Nozes {
    private String nameNozes;
    private int nrNozes;

    public Nozes() {
    }

    public Nozes(String nameNozes, int nrNozes) {
        this.nameNozes = nameNozes;
        this.nrNozes = nrNozes;
    }

    public int getNrNozes() {
        return nrNozes;
    }

    public String getNameNozes() {
        return nameNozes;
    }
}
