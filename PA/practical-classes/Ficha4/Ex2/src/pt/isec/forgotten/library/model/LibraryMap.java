package pt.isec.forgotten.library.model;

import java.util.*;

public class LibraryMap implements Library {
    private String nome;
    private Map<Integer, Book> livros;

    public LibraryMap(String nome) {
        this.nome = nome;
        livros = new HashMap<Integer, Book>();
    }

    @Override
    public String toStringPorTitulo() {
        StringBuilder sb = new StringBuilder(String.format("Library %s:",nome));
        List<Book> lista = new ArrayList<>(livros.values());
        Collections.sort(lista,new Book.ComparaTitulo());
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
        List<Book> lista = new ArrayList<>(livros.values());
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
        for(Book l : livros.values()) {
            sb.append(l.toString());
            sb.append('\n');
        }
        return sb.toString();
    }

    //    K   V
    //    1   livro 1
    //    2   livro 3
    @Override
    public int acrescentaLivro(String titulo, List<String> autores) {
        Book novo = new Book(titulo,autores);
        livros.put(novo.getCodigo(),novo);
        return novo.getCodigo();
    }

    @Override
    public int acrescentaLivro(Book book) {
        if (livros.containsValue(book))
            return -1;
        livros.put(book.getCodigo(), book);
        return book.getCodigo();
    }

    @Override
    public Book pesquisaLivro(int codigo) {
        return livros.get(codigo);
    }

    @Override
    public boolean eliminaLivro(int codigo) {
        return livros.remove(codigo) != null;
    }


}
