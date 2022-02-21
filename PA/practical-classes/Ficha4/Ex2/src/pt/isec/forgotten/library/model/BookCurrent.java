package pt.isec.forgotten.library.model;

import java.util.List;

public class BookCurrent extends Book {
    String ISBN;
    float preco;

    public BookCurrent(String titulo, List<String> autores, String ISBN, float preco) {
        super(titulo, autores);
        this.ISBN = ISBN;
        this.preco = preco;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public float getPreco() {
        return preco;
    }

    public void setPreco(float preco) {
        this.preco = preco;
    }

    @Override
    public String toString() {
        return super.toString()+ " => BookCurrent{" +
                "ISBN='" + ISBN + '\'' +
                ", preco=" + preco +
                "}";
    }
}
