package pt.isec.ans.biblioteca;

import pt.isec.ans.biblioteca.iu.BiblioIU;
import pt.isec.ans.biblioteca.modelo.Biblioteca;
import pt.isec.ans.biblioteca.modelo.BibliotecaList;
import pt.isec.ans.biblioteca.modelo.BibliotecaMap;
import pt.isec.ans.biblioteca.modelo.BibliotecaSet;

public class Main {
    public static void main(String[] args) {
        //Biblioteca biblioteca = new BibliotecaList("DEIS-ISEC-List");
        Biblioteca biblioteca = new BibliotecaSet("DEIS-ISEC-Set");
        //Biblioteca biblioteca = new BibliotecaMap("DEIS-ISEC-Map");
        BiblioIU biblioIU = new BiblioIU(biblioteca);
        biblioIU.comeca();
    }
}
