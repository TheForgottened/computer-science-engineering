package pt.isec.angelopaiva.jogo.logica.dados.minigames;

import java.util.concurrent.ThreadLocalRandom;

public class MinigameFactory {
    private static final int NR_MINIGAMES = 2;

    private static int lastMinigameIndex = -1;

    private MinigameFactory() {}

    public static IMinigame getMinigame() {
        int random;

        do {
            random = ThreadLocalRandom.current().nextInt(NR_MINIGAMES);
        } while (random == lastMinigameIndex);

        lastMinigameIndex = random;

        return switch (random) {
            case 0 -> new MathMinigame();
            case 1 -> new TypingMinigame();
            default -> throw new IllegalStateException("Unexpected value: " + random);
        };
    }
}
