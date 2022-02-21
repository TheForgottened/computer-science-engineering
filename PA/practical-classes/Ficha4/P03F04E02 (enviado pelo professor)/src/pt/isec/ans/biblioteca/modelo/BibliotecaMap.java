package pt.isec.ans.biblioteca.modelo;

import java.util.*;

public class BibliotecaMap implements Biblioteca {
    private String nome;
    private Map<Integer,Livro> livros;

    public BibliotecaMap(String nome) {
        this.nome = nome;
        livros = new HashMap<Integer,Livro>();
    }

    @Override
    public String toStringPorTitulo() {
        StringBuilder sb = new StringBuilder(String.format("Biblioteca %s:",nome));
        List<Livro> lista = new ArrayList<>(livros.values());
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
        List<Livro> lista = new ArrayList<>(livros.values());
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
        for(Livro l : livros.values()) {
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
        Livro novo = new Livro(titulo,autores);
        livros.put(novo.getCodigo(),novo);
        return novo.getCodigo();
    }

    @Override
    public int acrescentaLivro(Livro livro) {
        if (livros.containsValue(livro))
            return -1;
        livros.put(livro.getCodigo(),livro);
        return livro.getCodigo();
    }

    @Override
    public Livro pesquisaLivro(int codigo) {
        return livros.get(codigo);
    }

    @Override
    public boolean eliminaLivro(int codigo) {
        return livros.remove(codigo) != null;
    }


}
