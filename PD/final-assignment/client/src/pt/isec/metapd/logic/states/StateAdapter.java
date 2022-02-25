package pt.isec.metapd.logic.states;

import pt.isec.metapd.logic.data.MetaPDData;

public abstract class StateAdapter implements IState{
    private MetaPDData metaPDData;

    public StateAdapter(MetaPDData metaPDData){
        this.metaPDData = metaPDData;
    }

    public MetaPDData getMetaPDData() {
        return metaPDData;
    }

    @Override
    public IState login(String username, String password) {
        return this;
    }

    @Override
    public IState signup(String username, String name, String password) {
        return this;
    }

    @Override
    public IState goBack() {
        return this;
    }
}
