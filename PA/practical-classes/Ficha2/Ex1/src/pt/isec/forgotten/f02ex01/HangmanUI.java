package pt.isec.forgotten.f02ex01;

import java.util.Scanner;

public class HangmanUI {
    HangmanModel game;

    public HangmanUI(HangmanModel game) {
        this.game = game;
    }

    public void jogar() {
        Scanner sc = new Scanner(System.in);
        String option;

        while (!game.finished()) {

            System.out.println(game.getSituation()); // mostrar as letras descobertas
            System.out.println();
            System.out.println("Tentativas realizadas: " + game.getTries());
            System.out.println("Caracteres jogados: " + game.getUsed());
            System.out.println();

            System.out.print("Caracter ou palavra completa com " +
                    game.getSituation().length() + " caracteres para finalizar: ");
            option = sc.next().toUpperCase();
            option = option.trim();
            game.setOption(option);
            System.out.println();
        }

        if (game.guessed()) {
            System.out.println("Parabens, acertou na palavra \"" + game.getWord() + "\" em " + game.getTries() + " tentativas!");
        } else {
            System.out.println("Apos " + game.getTries() +
                    " tentativas nao acertou na palavra que era \"" + game.getWord() + "\".");
        }
    }
}
