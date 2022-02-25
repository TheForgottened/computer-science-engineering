package pt.isec.metapd.logic.states;

import pt.isec.metapd.logic.AppState;

import java.io.Serializable;

public interface IState extends Serializable {
    IState login(String username, String password);
    IState signup(String username, String name, String password);
    IState goBack();

    AppState getCurrentState();
}
