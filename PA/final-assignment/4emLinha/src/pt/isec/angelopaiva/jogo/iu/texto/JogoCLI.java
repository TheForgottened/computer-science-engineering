package pt.isec.angelopaiva.jogo.iu.texto;

import pt.isec.angelopaiva.jogo.logica.StateMachine;
import pt.isec.angelopaiva.jogo.logica.dados.players.Player;
import pt.isec.angelopaiva.jogo.logica.dados.players.PlayerAI;
import pt.isec.angelopaiva.jogo.logica.dados.players.PlayerHuman;
import pt.isec.angelopaiva.jogo.utils.UtilsCLI;
import pt.isec.angelopaiva.jogo.utils.UtilsFiles;

import java.util.ArrayList;

public class JogoCLI {
    private final StateMachine sm;
    private boolean mustExit;

    public JogoCLI(StateMachine sm) {
        this.sm = sm;
    }

    public void start() {
        mustExit = false;

        while (!mustExit) {
            switch (sm.getCurrentState()) {
                case CHOOSE_OPTION -> chooseOptionUI();
                case WAIT_PLAYERS_NAME_AND_TYPE -> waitPlayersNameAndTypeUI();
                case GAME -> gameUI();
                case END_GAME -> endGameUI();
                case MINIGAME_FOR_SPECIAL_PIECE -> minigameForSpecialPieceUI();
                case WATCH_REPLAY -> watchReplayUI();
                default -> throw new IllegalStateException("Unexpected value: " + sm.getCurrentState());
            }
        }

        System.out.println("Até breve!\n");
    }

    private void chooseOptionUI() {
        switch (UtilsCLI.chooseOption("Novo Jogo", "Carregar Jogo", "Replay", "Sair")) {
            case 1 -> sm.startWaitPlayersNameAndType();

            case 2 -> {
                String[] saves = UtilsFiles.getSaves().toArray(new String[0]);

                if (saves.length == 0) {
                    System.out.println("Não tem jogos disponíveis!");
                    sm.goBack();
                    return;
                }

                if (UtilsCLI.chooseOption("Listar Jogos", "Voltar") == 0) {
                    sm.goBack();
                    return;
                }

                String fileName = UtilsCLI.chooseRetrieveOptionWithoutExit(saves);

                if (sm.loadGame(fileName)) {
                    System.out.println("Erro a carregar o jogo!");
                }
            }

            case 3 -> {
                String[] replays = UtilsFiles.getReplays().toArray(new String[0]);

                if (replays.length == 0) {
                    System.out.println("Não tem jogos para replay disponíveis!");
                    sm.goBack();
                    return;
                }

                if (UtilsCLI.chooseOption("Listar Jogos", "Voltar") == 0) {
                    sm.goBack();
                    return;
                }

                String fileName = UtilsCLI.chooseRetrieveOptionWithoutExit(replays);

                if (sm.loadGameForReplay(fileName)) {
                    System.out.println("Erro a carregar o jogo para replay!");
                }
            }

            case 0 -> mustExit = true;
            default -> { /* there's no need to do anything */ }
        }
    }

    private void waitPlayersNameAndTypeUI() {
        String playerName1;
        String playerName2;

        playerName1 = UtilsCLI.askForString("Nome do jogador 1: ");
        Player p1 = UtilsCLI.chooseOptionWithoutExit( // 0 - AI, 1 - HUMANO
                "Jogador 1 é uma IA",
                "Jogador 1 é um ser humano"
        ) == 0 ? new PlayerAI(playerName1) : new PlayerHuman(playerName1);

        do {
            playerName2 = UtilsCLI.askForString("Nome do jogador 2: ");
        } while (playerName1.equals(playerName2));

        Player p2 = UtilsCLI.chooseOptionWithoutExit( // 0 - AI, 1 - HUMANO
                "Jogador 2 é uma IA",
                "Jogador 2 é um ser humano"
        ) == 0 ? new PlayerAI(playerName2) : new PlayerHuman(playerName2);

        sm.setPlayers(p1, p2);
    }

    private void gameUI() {
        System.out.println(sm.getBoardAsString());

        ArrayList<String> options = new ArrayList<>();

        options.add("Fazer Jogada");
        options.add("Criar Save do Jogo");

        if (sm.getUndoCreditsFromCurrentPlayer() > 0) options.add("Desfazer Jogadas");
        if (sm.currentPlayerCanPlayMinigames()) options.add("Minijogo para peça especial");

        options.add("Voltar ao menu inicial");

        System.out.println("Jogador " + sm.getCurrentPlayer() + " - " + sm.getCurrentPlayerName() + ":\n");

        switch (UtilsCLI.chooseOption(options.toArray(new String[0]))) {
            case 1 -> {
                int column;
                boolean useSpecialPiece = false;

                if (!sm.currentPlayerIsHuman()) {
                    sm.placePieceOnColumnAI();
                    return;
                }

                if (sm.currentPlayerHasSpecialPiece()) {
                    useSpecialPiece = UtilsCLI.askForBoolean("Pretende usar uma peça especial?");
                }

                column = UtilsCLI.askForInt("Em que coluna pretende jogar? (1 - " + sm.getNumberBoardColumns() + ") ");
                column--;

                sm.placePieceOnColumn(column, useSpecialPiece);
            }

            case 2 -> {
                String saveName = UtilsCLI.askForString("Nome do save: ");

                sm.saveGameForSave(saveName);
            }

            case 3 -> {
                int nrUndos = UtilsCLI.askForInt("Número de jogadas a desfazer (max. " + sm.getUndoCreditsFromCurrentPlayer() + "): ");

                sm.undoMoves(nrUndos);
            }

            case 4 -> sm.startMinigameForSpecialPiece();
            case 0 -> sm.goBack();
            default -> { /* there's no need to do anything */ }
        }
    }

    private void endGameUI() {
        System.out.println(sm.getEndGameInfo());

        UtilsCLI.waitForEnter("Prima enter para voltar ao menu principal...");
        sm.goBack();
    }

    private void minigameForSpecialPieceUI() {
        System.out.println(sm.getMinigameWording() + ":");
        String answer = UtilsCLI.askForString(sm.getMinigameQuestion());

        sm.setMinigameAnswer(answer);
    }

    private void watchReplayUI() {
        System.out.println("Jogador " + sm.getCurrentPlayer() + " - " + sm.getCurrentPlayerName() + ":\n");
        System.out.println(sm.getBoardAsString());
        sm.replayMove();
        UtilsCLI.waitForEnter("Prima enter continuar...");
        System.out.println();
    }
}
