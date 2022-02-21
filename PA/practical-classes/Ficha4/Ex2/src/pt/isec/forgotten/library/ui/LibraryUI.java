package pt.isec.forgotten.library.ui;

import pt.isec.forgotten.library.model.*;
import pt.isec.forgotten.library.model.Book;
import pt.isec.forgotten.library.model.BookOld;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class LibraryUI {
    Library lib;
    Scanner sc;

    public LibraryUI(Library lib) {
        this.lib = lib;
        sc = new Scanner(System.in);
    }

    int askInt(String question) {
        System.out.print(question);

        while (!sc.hasNextInt()) {
            sc.next();
        }

        int value = sc.nextInt();
        sc.nextLine();
        return value;
    }

    String askString(String question) {
        String answer;

        do {
            System.out.print(question);
            answer = sc.nextLine().trim();
        } while (answer.isEmpty());

        return answer;
    }

    int chooseOption(String... options) { // String [] opcoes
        int option;

        do {
            for (int i = 0; i < options.length; i++)
                System.out.printf("%3d - %s\n",i+1,options[i]);
            option = askInt("\n> ");
        } while (option < 1 || option > options.length);

        return option;
    }
    void addBook() {
        String title = askString("Indique o titulo do livro:\n> ");

        String author;
        ArrayList<String> authors = new ArrayList<>();

        do {
            author = askString("Indique um autor [\'fim\' para terminar]:\n> ");
            if (author != null && !author.equalsIgnoreCase("fim"))
                authors.add(author);
        } while (!author.equalsIgnoreCase("fim"));

        if (authors.isEmpty()) {
            authors.add("Autor desconhecido");
        }

        lib.addBook(title, authors);
    }

    void addBookOld() {
        String title = askString("Indique o titulo do livro:\n> ");

        String author;
        ArrayList<String> authors = new ArrayList<>();

        do {
            author = askString("Indique um autor [\'fim\' para terminar]:\n> ");
            if (author != null && !author.equalsIgnoreCase("fim"))
                authors.add(author);
        } while (!author.equalsIgnoreCase("fim"));

        if (authors.isEmpty()) {
            authors.add("Autor desconhecido");
        }

        int copies = askInt("Indique o numero de copias: ");
        lib.addBook(new BookOld(title, authors, copies));
    }

    void addBookCurrent() {
        String title = askString("Indique o titulo do livro:\n> ");

        String author;
        ArrayList<String> authors = new ArrayList<>();

        do {
            author = askString("Indique um autor [\'fim\' para terminar]:\n> ");
            if (author != null && !author.equalsIgnoreCase("fim"))
                authors.add(author);
        } while (!author.equalsIgnoreCase("fim"));

        if (authors.isEmpty()) {
            authors.add("Autor desconhecido");
        }

        String isbn = askString("Indique o ISBN: ");
        System.out.println("Indique o preco do livro: ");
        while (!sc.hasNextFloat()) {
            sc.next();
        }

        float price = sc.nextFloat();
        lib.addBook(new BookCurrent(title, authors, isbn, price));
    }

    void searchBook() {
        int code = askInt("Indique o codigo do book a pesquisar: ");
        Book book = lib.searchBook(code);

        if (book == null) {
            System.out.println("Codigo de book nao encontrado");
        } else {
            System.out.println("Book encontrado: " + book);
        }
    }

    void deleteBook() {
        int code = askInt("Indique o codigo do livro a eliminar: ");
        boolean deleted = lib.deleteBook(code);
        if (!deleted) {
            System.out.println("Codigo de livro nao encontrado");
        } else {
            System.out.println("Book eliminado");
        }
    }

    public void start() {
        while (true) {
            switch (chooseOption("Adiciona livro","Adiciona Book Antigo","Adiciona Book Atual",
                    "Pesquisa Book","Remove Book","Listar","Listar por titulo","Listar por codigo",
                    "Acrescentar livro repetido","Sair", "Gravar dados (.txt)", "Ler dados (.txt)",
                    "Gravar dados (.bin)", "Ler dados (.bin)")) {
                case 1:
                    addBook();
                    break;
                case 2:
                    addBookOld();
                    break;
                case 3:
                    addBookCurrent();
                    break;
                case 4:
                    searchBook();
                    break;
                case 5:
                    deleteBook();
                    break;
                case 6:
                    System.out.println(lib.toString());
                    break;
                case 7:
                    System.out.println(lib.toStringTitle());
                    break;
                case 8:
                    System.out.println(lib.toStringCode());
                    break;
                case 9:
                    Book book = lib.searchBook(1);

                    if (book != null) {
                        if (lib.addBook(book) < 0) {
                            System.out.println("Erro a adicionar book");
                        } else {
                            System.out.println("Book adicionado com sucesso");
                        }
                    }
                    break;
                case 10:
                    return;
                case 11:
                    lib.writeTxt(new File("library.txt"));
                    break;
                case 12:
                    System.out.println(lib.readTxt(new File("library.txt")));
                    break;
                case 13:
                    lib.saveGenericBin();
                    break;
                case 14:
                    lib.readGenericBin();
            }
        }
    }
}
