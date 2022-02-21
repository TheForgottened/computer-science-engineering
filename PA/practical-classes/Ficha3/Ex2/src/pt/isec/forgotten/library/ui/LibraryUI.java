package pt.isec.forgotten.library.ui;

import pt.isec.forgotten.library.model.Book;
import pt.isec.forgotten.library.model.Library;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class LibraryUI {
    private Library lib;
    private Scanner sc;

    public LibraryUI(Library lib) {
        this.lib = lib;
        this.sc = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            switch (chooseOption("Adiciona livro", "Pesquisa Livro",
                    "Remove Livro", "Listar", "Sair")) {
                case 1:
                    addBook();
                    break;
                case 2:
                    searchBook();
                    break;
                case 3:
                    deleteBook();
                    break;
                case 4:
                    System.out.println(lib.toString());
                    break;
                case 5:
                    return;
            }
        }
    }

    int askInt(String question) {
        System.out.print(question);

        while (!sc.hasNextInt())
            sc.next();

        int valor = sc.nextInt();
        sc.nextLine();

        return valor;
    }

    String askString(String question) {
        String answer;

        do {
            System.out.print(question);
            answer = sc.nextLine().trim();
        } while (answer.isEmpty());

        return answer;
    }

    int chooseOption(String... options) {
        int option;

        do {
            for (int i = 0; i < options.length; i++)
                System.out.printf("%3d - %s\n", i + 1, options[i]);
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

        this.lib.addBook(title, authors);
    }

    void searchBook() {
        int code = askInt("Indique o codigo do livro a pesquisar: ");
        Book book = this.lib.searchBook(code);

        if (book == null) {
            System.out.println("Codigo de livro nao encontrado");
        } else {
            System.out.println("Livro encontrado: " + book);
        }
    }

    void deleteBook() {
        int code = askInt("Indique o codigo do livro a eliminar: ");
        boolean deleted = this.lib.deleteBook(code);

        if (deleted) {
            System.out.println("Livro eliminado");
        } else {
            System.out.println("Codigo de livro nao encontrado");
        }
    }

}
