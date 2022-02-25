package pt.isec.metapd.logic.states;

import pt.isec.metapd.logic.AppState;
import pt.isec.metapd.logic.data.MetaPDData;

public class AwaitsCommand extends StateAdapter{
    public AwaitsCommand(MetaPDData metaPDData) {
        super(metaPDData);
    }

    @Override
    public IState goBack() {
        if(getMetaPDData().logout()){
            return new AwaitsLogin(getMetaPDData());
        }
        return this;
    }

    @Override
    public AppState getCurrentState() {
        return AppState.AWAITS_COMMAND;
    }
}
