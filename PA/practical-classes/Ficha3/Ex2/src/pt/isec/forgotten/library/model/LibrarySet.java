package pt.isec.forgotten.library.model;

import java.util.*;

public class LibrarySet implements Library {
    private String name;
    private Set<Book> books;

    public LibrarySet(String name) {
        books = new HashSet<>();
        this.name = name;
    }

    @Override
    public int addBook(String title, ArrayList<String> authors) {
        Book new1 = new Book(title, authors);

        if (!this.books.add(new1)) {
            return -1;
        }

        return new1.getCode();
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

    class compareTitle implements Comparator<Book> {

        @Override
        public int compare(Book o1, Book o2) { return o1.getTitle().compareTo(o2.getTitle()); }
    }

    @Override
    public Book searchBook(int code) {
        Iterator<Book> it = this.books.iterator();

        while (it.hasNext()) {
            Book book = it.next();

            if (book.getCode() == code) {
                return book;
            }
        }

        return null;
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
       return this.books.remove(new Book(code));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(String.format("Biblioteca %s: ", name));
        List<Book> list = new ArrayList<>();
        list.addAll(this.books);
        Collections.sort(list);
        Iterator<Book> it = books.iterator();

        while (it.hasNext()) {
            Book book = it.next();
            sb.append("\n\t- ");
            sb.append(book.toString());
        }

        return sb.toString();
    }
}
