package pt.isec.metapd.logic.states;

import pt.isec.metapd.logic.AppState;
import pt.isec.metapd.logic.data.MetaPDData;

public class AwaitsLogin extends StateAdapter{
    public AwaitsLogin(MetaPDData metaPDData) {
        super(metaPDData);
    }

    @Override
    public IState login(String username, String password) {
        if(getMetaPDData().login(username, password)) {
            getMetaPDData().updateAllData();
            return new AwaitsCommand(getMetaPDData());
        }
        return this; //if signup is not valid
    }

    @Override
    public IState goBack() {
        return new AwaitsSignUp(getMetaPDData());
    }

    @Override
    public AppState getCurrentState() {
        return AppState.AWAITS_LOGIN;
    }
}
