package pt.isec.forgotten.f02ex01;

public class Hangman {
    public static void main(String[] args) {
        HangmanModel game = new HangmanModel();
        HangmanUI gameUI = new HangmanUI(game);
        gameUI.jogar();
    }
}
