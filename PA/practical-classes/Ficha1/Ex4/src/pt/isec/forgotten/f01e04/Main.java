package pt.isec.forgotten.f01e04;

public class Main {
    public static void main(String[] args) {
        Tabela20 t = new Tabela20();
        t.preenche();
        System.out.println("Numero de elementos duplicados: " + t.nrDuplicados());
    }
}
