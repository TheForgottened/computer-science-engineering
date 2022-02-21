package pt.isec.forgotten.library.model;

import java.util.ArrayList;
import java.util.List;

public class Book implements Comparable<Book> {
    private static int counter = 0;

    private static int generateCode() {
        return ++counter;
    }

    private String title;
    private int code;
    private ArrayList<String> authors;

    Book(String title, List<String> authors) {
        this.title = title;
        this.authors = new ArrayList<>(authors);
        this.code = generateCode();
    }

    Book(int code) {
        this.title = null;
        this.authors = null;
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<String> getAuthors() {
        return authors;
    }

    public int getCode() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;

        return code == book.code;
    }

    @Override
    public int hashCode() {
        return code;
    }

    @Override
    public int compareTo(Book o) {
        return this.code - o.code;
    }

    @Override
    public String toString() {
        return this.title + " (" + this.code + ") " + this.authors;
    }
}
