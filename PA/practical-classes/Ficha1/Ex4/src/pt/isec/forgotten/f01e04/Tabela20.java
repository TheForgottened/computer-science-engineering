package pt.isec.forgotten.f01e04;

class Tabela20 {
    public static final int TAM_TAB = 20;

    private int [] tab; // = new int[TAM_TAB];
    private int nrtentativas = 0;
    private int nrvalores;

    Tabela20() {
        tab = new int[TAM_TAB];
    }

    public boolean existe (int valor) {
        for (int i = 0; i < nrvalores; i++) {
            if (tab[i] == valor)
                return true;
        }

        return false;
    }

    public void preenche() {
        while (nrvalores < TAM_TAB) {
            nrtentativas++;

            int valor = (int) (Math.random() * 101);

            if (!existe(valor))
                tab[nrvalores++] = valor;
        }
    }

    public int nrDuplicados() {
        return nrtentativas - nrvalores;
    }

    public void mostra() {
        System.out.printf("[");

        for (int i = 0; i < nrvalores; i++) {
            if (i != 0)
                System.out.print(" ");
            System.out.print(tab[i]);
        }

        System.out.print("]");
    }
}