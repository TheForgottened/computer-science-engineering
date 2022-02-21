package pt.isec.forgotten.library;

import pt.isec.forgotten.library.model.Library;
import pt.isec.forgotten.library.model.LibraryList;
import pt.isec.forgotten.library.model.LibrarySet;
import pt.isec.forgotten.library.ui.LibraryUI;

public class Main {
    public static void main(String[] args) {
        // Library lib = new LibraryList("DEIS-ISEC");
        Library lib = new LibrarySet("DEIS-ISEC");
        LibraryUI libUI = new LibraryUI(lib);
        libUI.start();
    }
}
