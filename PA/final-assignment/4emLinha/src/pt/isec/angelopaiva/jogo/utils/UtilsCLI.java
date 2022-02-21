package pt.isec.angelopaiva.jogo.utils;

import java.util.Scanner;

public class UtilsCLI {
    private static final Scanner sc = new Scanner(System.in);

    private UtilsCLI() {}

    public static int askForInt(String question) {
        System.out.printf(question);

        while (!sc.hasNextInt()) {
            sc.next();
        }

        int value = sc.nextInt();
        sc.nextLine();

        return value;
    }

    public static String askForString(String question) {
        String answer;

        do {
            System.out.print(question);
            answer = sc.nextLine().trim();
        } while (answer.isEmpty());

        return answer;
    }

    public static int chooseOption(String... options) {
        int option;

        do {
            for (int i = 0; i < options.length - 1; i++) {
                System.out.printf("%3d - %s%n", i + 1, options[i]);
            }

            System.out.printf("%n%3d - %s%n", 0, options[options.length - 1]);
            option = askForInt("%n> ");
        } while (option < 0 || option >= options.length);

        return option;
    }

    public static int chooseOptionWithoutExit(String... options) {
        int option;

        do {
            for (int i = 0; i < options.length; i++) {
                System.out.printf("%3d - %s%n", i + 1, options[i]);
            }

            option = askForInt("%n> ");
            option--;
        } while (option < 0 || option >= options.length);

        return option;
    }

    public static String chooseRetrieveOptionWithoutExit(String... options) {
        int option = chooseOptionWithoutExit(options);

        return options[option];
    }

    public static void waitForEnter(String str) {
        System.out.println(str);
        sc.nextLine();
    }

    public static boolean askForBoolean(String question) {
        final String yes = "Sim";
        final String no = "Não";
        int option;

        System.out.println(question);
        System.out.println();
        System.out.println("1 - " + yes);
        System.out.println("0 - " + no);

        do {
            while (!sc.hasNextInt()) {
                sc.next();
            }

            option = sc.nextInt();
            sc.nextLine();
        } while (option != 0 && option != 1);

        return option == 1;
    }
}
