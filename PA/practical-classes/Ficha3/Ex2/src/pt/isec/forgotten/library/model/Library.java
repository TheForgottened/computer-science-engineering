package pt.isec.forgotten.library.model;

import java.util.ArrayList;

public interface Library {
    int addBook(String title, ArrayList<String> authors);

    Book searchBook(int code);

    boolean deleteBook(int code);

    @Override
    String toString();
}
