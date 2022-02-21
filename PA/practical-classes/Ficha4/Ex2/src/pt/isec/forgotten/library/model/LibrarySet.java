package pt.isec.forgotten.library.model;

import java.io.*;
import java.util.*;

public class LibrarySet implements Library, Serializable {
    private String name;
    private Set<Book> books;

    public LibrarySet(String name) {
        this.name = name;
        books = new HashSet<>();
    }


    public String toStringTitle() {
        StringBuilder sb = new StringBuilder(String.format("Library %s:", name));
        List<Book> lista = new ArrayList<>();
        lista.addAll(books);
        Collections.sort(lista,new Book.ComparaTitulo());
        Iterator<Book> it = lista.iterator();
        while (it.hasNext()) {
            Book book = it.next();
            sb.append("\n\t- ");
            sb.append(book.toStringFormatdo());
        }
        return sb.toString();
    }

    public String toStringCode() {
        StringBuilder sb = new StringBuilder(String.format("Library %s:", name));
        List<Book> lista = new ArrayList<>();
        lista.addAll(books);
        Collections.sort(lista,new Book.ComparaCodigo());
        Iterator<Book> it = lista.iterator();
        while (it.hasNext()) {
            Book book = it.next();
            sb.append("\n\t- ");
            sb.append(book.toStringFormatdo());
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(name);
        sb.append('\n');
        for (Book l : books) {
            sb.append(l.toString());
            sb.append('\n');
        }

        return sb.toString();
    }

    @Override
    public int addBook(String title, List<String> authors) {
        Book b = new Book(title, authors);
        if (!books.add(b)) {
            return -1;
        }

        return b.getCodigo();
    }

    @Override
    public int addBook(Book book) {
        if (!books.add(book))
            return -1;
        return book.getCodigo();
    }

    @Override
    public Book searchBook(int code) {
        //Book base = new Book(codigo);
        Iterator<Book> it = books.iterator();
        while (it.hasNext()) {
            Book book = it.next();
            //if (book.equals(base))
            //    return book;
            if (book.getCodigo() == code)
                return book;
        }
        //for (Book livro : books)
        //    if (livro.getCodigo() == codigo)
        //        return livro;

        return null;
    }

    @Override
    public boolean deleteBook(int code) {
        return books.remove(new Book(code));
    }

    public void writeTxt(File f) {
        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(f)));

            pw.println(this);
            pw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void readTxt(File file) {
        try {
            FileWriter wr = new FileWriter(file);
            wr.write(this.toString());
            wr.close();
        } catch (IOException E) {
            System.out.println("Erro IO!");
        }
    }

    private static final String FILENAME = "library.bin";

    public boolean saveGenericBin() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILENAME));
            oos.writeObject(this);
            oos.close();

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean readGenericBin() {
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(FILENAME));
            Object obj = ois.readObject();
            ois.close();

            return true;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}
