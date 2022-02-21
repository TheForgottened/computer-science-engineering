package exemplos.labirinto;

import exemplos.labirinto.command.*;
import exemplos.labirinto.modelo.LabirintoGame;

import java.util.List;

public class GestaoLabirintoGame {
    LabirintoGame labirintoGame;
    CommandManager commandManager;

    public GestaoLabirintoGame(double vr) {
        labirintoGame = new LabirintoGame(vr);
        commandManager = new CommandManager();
    }

    public void up() { commandManager.invokeCommand(new MoveUp(labirintoGame)); }
    public void down() { commandManager.invokeCommand(new MoveDown(labirintoGame)); }
    public void left() { commandManager.invokeCommand(new MoveLeft(labirintoGame)); }
    public void right() { commandManager.invokeCommand(new MoveRight(labirintoGame)); }

    public void inicializa() { labirintoGame.inicializa(); }
    public boolean isGameFinished() { return labirintoGame.isGameFinished(); }
    public List<String> getVisibleMap() { return labirintoGame.getVisibleMap(); }

    public void undo() { commandManager.undo(); }
    public void redo() { commandManager.redo(); }
}

