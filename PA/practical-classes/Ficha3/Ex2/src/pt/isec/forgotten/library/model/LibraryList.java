package pt.isec.forgotten.library.model;

import java.util.ArrayList;

public class LibraryList implements Library {
    private String name;
    private ArrayList<Book> books = new ArrayList<>();

    public LibraryList(String name) {
        this.name = name;
    }

    @Override
    public int addBook(String title, ArrayList<String> authors) {
        Book newBook = new Book(title, authors);
        books.add(newBook);
        return newBook.getCode();
    }

    /*
    public Book searchBook(int code) {
        for (Book b: this.books) {
            if (b.getCode() == code) {
                return b;
            }
        }

        return null;
    }
    */

    @Override
    public Book searchBook(int code) {
        int index = this.books.indexOf(new Book(code));

        if (index < 0) {
            return null;
        }

        return this.books.get(index);
    }

    /*
    public boolean deleteBook(int code) {
        for (Book b: this.books) {
            if (b.getCode() == code) {
                return books.remove(b);
            }
        }

        return false;
    }
    */

    @Override
    public boolean deleteBook(int code) {
        int index = this.books.indexOf(new Book(code));

        if (index < 0) {
            return false;
        }

        this.books.remove(index);
        return true;
    }

    @Override
    public String toString() {
        return "Biblioteca " + name + ":\n" + books;
    }
}
