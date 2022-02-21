package pt.isec.forgotten.library.model;

import java.io.File;
import java.util.List;

public interface Library {
    @Override
    String toString();
    String toStringTitle();
    String toStringCode();

    int addBook(String title, List<String> authors);
    int addBook(Book book);

    Book searchBook(int code);

    boolean deleteBook(int code);

    void writeTxt(File f);
    String readTxt(File f);

    boolean saveGenericBin();
    boolean readGenericBin();
}
