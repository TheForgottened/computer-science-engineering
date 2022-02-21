package pt.isec.angelopaiva.jogo.logica.estados;

import pt.isec.angelopaiva.jogo.logica.AppStates;
import pt.isec.angelopaiva.jogo.logica.JogoManager;
import pt.isec.angelopaiva.jogo.utils.UtilsFiles;

public class EndGame extends StateAdapter {
    protected EndGame(JogoManager jogoManager, boolean mustSave) {
        super(jogoManager);

        if (!mustSave) return;

        UtilsFiles.saveGameToReplayFile(jogoManager);
        UtilsFiles.updateReplayList();
    }

    @Override
    public IState goBack() { return new ChooseOption(jogoManager); }

    @Override
    public AppStates getState() { return AppStates.END_GAME; }
}
