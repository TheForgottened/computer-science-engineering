package pt.isec.forgotten.balls.utils;

import java.util.Scanner;

public class UtilsUI {

    static Scanner sc;

    static {
        sc = new Scanner(System.in);
    }

    private UtilsUI() {}

    public static int askInt(String question) {
        System.out.print(question);

        while (!sc.hasNextInt()) {
            sc.next();
        }

        int value = sc.nextInt();
        sc.nextLine();
        return value;
    }
    public static String askString(String question) {
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
                System.out.printf("%3d - %s\n", i + 1, options[i]);
            }

            System.out.printf("\n%3d - %s\n", 0, options[options.length - 1]);
            option = askInt("\n> ");
        } while (option < 0 || option >= options.length);

        return option;
    }
}

