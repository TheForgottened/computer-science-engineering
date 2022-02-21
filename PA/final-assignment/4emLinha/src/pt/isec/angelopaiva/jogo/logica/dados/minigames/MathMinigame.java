package pt.isec.angelopaiva.jogo.logica.dados.minigames;

import java.text.DecimalFormat;
import java.util.concurrent.ThreadLocalRandom;

public class MathMinigame extends MinigameAdapter {
    private static final int REQUIRED_NR_RIGHT_ANSWERS = 5;
    private static final int MAX_SECONDS = 30;

    private int nrRightAnswers = 0;
    private double lastSolution;

    public MathMinigame() {
        setMaxSeconds(MAX_SECONDS);
        startTimer();
    }

    @Override
    public String getQuestion() {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");

        int a = getRandomTwoDigitInt();
        int b = getRandomTwoDigitInt();
        Operators o = getRandomOperator();

        switch (o) {
            case ADDITION -> lastSolution = (double) a + b;
            case SUBTRACTION -> lastSolution = (double) a - b;
            case DIVISION -> lastSolution = (double) a / b;
            case MULTIPLICATION -> lastSolution = (double) a * b;
        }

        String x = decimalFormat.format(lastSolution);

        lastSolution = Double.parseDouble(x);

        return String.format("%d %c %d = ", a, o.operator, b);
    }

    @Override
    public void setAnswer(String answer) {
        if (answer == null || answer.isBlank()) return;

        double doubleAnswer;

        try {
            doubleAnswer = Double.parseDouble(answer);
        } catch (NumberFormatException e) {
            return;
        }

        if (doubleAnswer == lastSolution) nrRightAnswers++;

        finished = true;

        if (nrRightAnswers < REQUIRED_NR_RIGHT_ANSWERS) finished = hasTimedOut();

        if (finished) stopTimer();
    }

    @Override
    public String getWording() { return "Resolva a operação (arredondada a 2 casas decimais)"; }

    @Override
    public boolean hasWon() { return nrRightAnswers >= REQUIRED_NR_RIGHT_ANSWERS && super.hasWon(); }

    private int getRandomTwoDigitInt() { return ThreadLocalRandom.current().nextInt(0, 100); }
    private Operators getRandomOperator() { return Operators.values()[ThreadLocalRandom.current().nextInt(Operators.values().length)]; }

    private enum Operators {
        ADDITION('+'),
        SUBTRACTION('-'),
        DIVISION('/'),
        MULTIPLICATION('*');

        char operator;

        Operators(char operator) { this.operator = operator; }
    }
}
