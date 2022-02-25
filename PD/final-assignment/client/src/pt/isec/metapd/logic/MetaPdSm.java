package pt.isec.metapd.logic;

import pt.isec.metapd.communication.TinyGroup;
import pt.isec.metapd.communication.TinyGroupRequest;
import pt.isec.metapd.communication.TinyMessageReceived;
import pt.isec.metapd.communication.TinyUser;
import pt.isec.metapd.logic.data.MetaPDData;
import pt.isec.metapd.logic.states.*;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class MetaPdSm implements Serializable {
    private final AtomicBoolean mustStop = new AtomicBoolean(false);

    private final MetaPDData metaPDData;
    private IState state;

    public MetaPdSm(String lbAddress, int lbPort) throws IOException {
        this.metaPDData = new MetaPDData(mustStop, lbAddress, lbPort);
        this.state = new AwaitsLogin(metaPDData);
    }

    public void setObservable(MetaPdObservable metaPdObservable) { metaPDData.setObservable(metaPdObservable); }

    public AppState getCurrentState() { return state.getCurrentState(); }

    public void setMustStop(boolean value) {
        mustStop.set(value);
    }

    public boolean getMustStop() {
        return mustStop.get();
    }

    protected MetaPDData getMetaPDData(){
        return metaPDData;
    }

    public IState getState(){
        return state;
    }

    public void setState(IState state){this.state = state;}

    public void login(String username, String password) {
        setState(getState().login(username, password));
    }

    public void signup(String username, String name, String password) {
        setState(getState().signup(username, name, password));
    }

    public void goBack(){
        setState(getState().goBack());
    }

    public boolean isAwaitingLogin() {
        return (getState() instanceof AwaitsLogin);
    }

    public boolean isAwaitingSignUp() {
        return (getState() instanceof AwaitsSignUp);
    }

    public boolean isAwaitingCommand() {
        return (getState() instanceof AwaitsCommand);
    }


    //Get Data

    public String getUsername() {
        return getMetaPDData().getUsername();
    }

    public boolean makeCommand(int commandCode) {
        return getMetaPDData().makeCommand(commandCode, null, null,null);
    }

    public boolean makeCommand(int commandCode, String info) {
        return getMetaPDData().makeCommand(commandCode, info, null,null);
    }
    public boolean makeCommand(int commandCode, String info1, String info2) {
        return getMetaPDData().makeCommand(commandCode, info1, info2,null);
    }

    public boolean makeCommand(int commandCode, String info1, String info2, File info3) {
        return getMetaPDData().makeCommand(commandCode, info1, info2,info3);
    }

    public List<TinyUser> getSystemUserList() {
        return getMetaPDData().getSystemUserList();
    }

    public List<TinyMessageReceived> getMessageGroupList() {
        return getMetaPDData().getMessageGroupList();
    }

    public List<TinyMessageReceived> getMessageContactList() {
        return getMetaPDData().getMessageContactList();
    }

    public List<TinyUser> getContactList() {
        return getMetaPDData().getContactList();
    }

    public List<TinyUser> getFriendshipRequestList() {
        return getMetaPDData().getFriendshipRequestList();
    }

    public List<TinyGroup> getGroupList() {
        return getMetaPDData().getGroupList();
    }

    public List<TinyGroup> getMyGroupsList() {
        return getMetaPDData().getMyGroupsList();
    }

    public String getName() {
        return getMetaPDData().getName();
    }

    public List<TinyGroupRequest> getGroupRequestList() {
        return getMetaPDData().getGroupRequestList();
    }
}
