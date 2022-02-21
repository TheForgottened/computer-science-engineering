package pt.isec.ans.biblioteca.modelo;

import java.util.*;

public class BibliotecaSet implements Biblioteca {
    private String nome;
    private Set<Livro> livros;

    public BibliotecaSet(String nome) {
        this.nome = nome;
        livros = new HashSet<>();
    }


    public String toStringPorTitulo() {
        StringBuilder sb = new StringBuilder(String.format("Biblioteca %s:",nome));
        List<Livro> lista = new ArrayList<>();
        lista.addAll(livros);
        Collections.sort(lista,new Livro.ComparaTitulo());
        Iterator<Livro> it = lista.iterator();
        while (it.hasNext()) {
            Livro livro= it.next();
            sb.append("\n\t- ");
            sb.append(livro.toStringFormatdo());
        }
        return sb.toString();
    }

    public String toStringPorCodigo() {
        StringBuilder sb = new StringBuilder(String.format("Biblioteca %s:",nome));
        List<Livro> lista = new ArrayList<>();
        lista.addAll(livros);
        Collections.sort(lista,new Livro.ComparaCodigo());
        Iterator<Livro> it = lista.iterator();
        while (it.hasNext()) {
            Livro livro= it.next();
            sb.append("\n\t- ");
            sb.append(livro.toStringFormatdo());
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
        if (!livros.add(novo))
            return -1;
        return novo.getCodigo();
    }

    @Override
    public int acrescentaLivro(Livro livro) {
        if (!livros.add(livro))
            return -1;
        return livro.getCodigo();
    }

    @Override
    public Livro pesquisaLivro(int codigo) {
        //Livro base = new Livro(codigo);
        Iterator<Livro> it = livros.iterator();
        while (it.hasNext()) {
            Livro livro = it.next();
            //if (livro.equals(base))
            //    return livro;
            if (livro.getCodigo() == codigo)
                return livro;
        }
        //for (Livro livro : livros)
        //    if (livro.getCodigo() == codigo)
        //        return livro;

        return null;
    }

    @Override
    public boolean eliminaLivro(int codigo) {
        return livros.remove(new Livro(codigo));
    }


}
