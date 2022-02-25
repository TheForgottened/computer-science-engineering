package pt.isec.metapd.logic.states;

import pt.isec.metapd.logic.AppState;
import pt.isec.metapd.logic.data.MetaPDData;

public class AwaitsSignUp extends StateAdapter{
    public AwaitsSignUp(MetaPDData metaPDData) {
        super(metaPDData);
    }

    @Override
    public IState signup(String username, String name, String password) {
        if(getMetaPDData().signup(username, name, password)){
            return new AwaitsLogin(getMetaPDData());
        }
        return this; //if signup is not valid
    }

    @Override
    public IState goBack() {
        return new AwaitsLogin(getMetaPDData());
    }

    @Override
    public AppState getCurrentState() {
        return AppState.AWAITS_SIGNUP;
    }
}
