package pt.isec.forgotten.library.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class LibraryList implements Library {
    private String name;
    private List<Book> books;

    public LibraryList(String name) {
        this.name = name;
        books = new ArrayList<>();
    }

    @Override
    public String toStringPorTitulo() {
        StringBuilder sb = new StringBuilder(String.format("Library %s:",nome));
        List<Book> lista = new ArrayList<>(books);
        Collections.sort(lista, new Book.ComparaTitulo());
        Iterator<Book> it = lista.iterator();
        while (it.hasNext()) {
            Book book = it.next();
            sb.append("\n\t- ");
            sb.append(book.toString());
        }
        return sb.toString();
    }

    @Override
    public String toStringPorCodigo() {
        StringBuilder sb = new StringBuilder(String.format("Library %s:",nome));
        List<Book> lista = new ArrayList<>(books);
        Collections.sort(lista,new Book.ComparaCodigo());
        Iterator<Book> it = lista.iterator();
        while (it.hasNext()) {
            Book book = it.next();
            sb.append("\n\t- ");
            sb.append(book.toString());
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(nome);
        sb.append('\n');
        for(Book l : books) {
            sb.append(l.toString());
            sb.append('\n');
        }
        return sb.toString();
    }

    @Override
    public int acrescentaLivro(String titulo, List<String> autores) {
        Book novo = new Book(titulo,autores);
        books.add(novo);
        return novo.getCodigo();
    }

    @Override
    public int acrescentaLivro(Book book) {
        books.add(book);
        return book.getCodigo();
    }

    @Override
    public Book pesquisaLivro(int codigo) {
       /* Iterator<Book> it = books.iterator();
        while (it.hasNext()) {
            Book livro = it.next();

        }*/

        int indice = books.indexOf(new Book(codigo));
        if (indice<0)
            return null;
        return books.get(indice);
    }

    @Override
    public boolean eliminaLivro(int codigo) {
        int indice = books.indexOf(new Book(codigo));
        if (indice<0)
            return false;
        books.remove(indice);
        return true;
    }
}
