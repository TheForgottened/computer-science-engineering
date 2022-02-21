package pt.isec.ans.biblioteca.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class BibliotecaList implements Biblioteca {
    private String nome;
    private List<Livro> livros;

    public BibliotecaList(String nome) {
        this.nome = nome;
        livros = new ArrayList<>();
    }

    @Override
    public String toStringPorTitulo() {
        StringBuilder sb = new StringBuilder(String.format("Biblioteca %s:",nome));
        List<Livro> lista = new ArrayList<>(livros);
        Collections.sort(lista,new Livro.ComparaTitulo());
        Iterator<Livro> it = lista.iterator();
        while (it.hasNext()) {
            Livro livro= it.next();
            sb.append("\n\t- ");
            sb.append(livro.toString());
        }
        return sb.toString();
    }

    @Override
    public String toStringPorCodigo() {
        StringBuilder sb = new StringBuilder(String.format("Biblioteca %s:",nome));
        List<Livro> lista = new ArrayList<>(livros);
        Collections.sort(lista,new Livro.ComparaCodigo());
        Iterator<Livro> it = lista.iterator();
        while (it.hasNext()) {
            Livro livro= it.next();
            sb.append("\n\t- ");
            sb.append(livro.toString());
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(nome);
        sb.append('\n');
        for(Livro l : livros) {
            sb.append(l.toString());
            sb.append('\n');
        }
        return sb.toString();
    }

    @Override
    public int acrescentaLivro(String titulo, List<String> autores) {
        Livro novo = new Livro(titulo,autores);
        livros.add(novo);
        return novo.getCodigo();
    }

    @Override
    public int acrescentaLivro(Livro livro) {
        livros.add(livro);
        return livro.getCodigo();
    }

    @Override
    public Livro pesquisaLivro(int codigo) {
       /* Iterator<Livro> it = livros.iterator();
        while (it.hasNext()) {
            Livro livro = it.next();

        }*/

        int indice = livros.indexOf(new Livro(codigo));
        if (indice<0)
            return null;
        return livros.get(indice);
    }

    @Override
    public boolean eliminaLivro(int codigo) {
        int indice = livros.indexOf(new Livro(codigo));
        if (indice<0)
            return false;
        livros.remove(indice);
        return true;
    }
}
