package pt.isec.angelopaiva.jogo.logica.dados.minigames;

public interface IMinigame {
    void setMaxSeconds(int maxSeconds);

    void startTimer();
    void stopTimer();

    boolean hasWon();

    void setAnswer(String answer);
    String getQuestion();
    String getWording();

    boolean isFinished();
}
