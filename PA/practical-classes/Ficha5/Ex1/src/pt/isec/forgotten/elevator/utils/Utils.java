package pt.isec.forgotten.elevator.utils;

import java.util.Scanner;

public final class Utils {
    static Scanner sc;

    static {
        sc = new Scanner(System.in);
    }

    private Utils() {
    }

    public static int askInt (String question) {
        System.out.print(question);

        while (!sc.hasNextInt()) {
            sc.next();
        }

        int value = sc.nextInt();
        sc.nextLine();
        return value;
    }

    public static String pedeString(String question) {
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
