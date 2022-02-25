package pt.isec.metapd.logic;

import pt.isec.metapd.communication.TinyGroup;
import pt.isec.metapd.communication.TinyGroupRequest;
import pt.isec.metapd.communication.TinyMessageReceived;
import pt.isec.metapd.communication.TinyUser;

import java.awt.*;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class MetaPdObservable {
    private final MetaPdSm metaPdSm;
    private final List<Container> observers = new LinkedList<>();

    public MetaPdObservable(MetaPdSm metaPdSm) {
        this.metaPdSm = metaPdSm;
    }

    public void addObserver(Container container) { observers.add(container); }
    public void removeObserver(Container container) { observers.remove(container); }

    public void updateObservers() {
        for (Container container : observers) {
            container.update(null);
        }
    }

    public AppState getCurrentState() { return metaPdSm.getCurrentState(); }

    public void setMustStop(boolean value) {
        metaPdSm.setMustStop(value);
    }

    public boolean getMustStop() {
        return metaPdSm.getMustStop();
    }

    public void login(String username, String password) {
        metaPdSm.login(username, password);
        updateObservers();
    }

    public void signup(String username, String name, String password) {
        metaPdSm.signup(username, name, password);
        updateObservers();
    }

    public void goBack() {
        metaPdSm.goBack();
        updateObservers();
    }

    public boolean makeCommand(int commandCode) {
        boolean value = metaPdSm.makeCommand(commandCode, null, null,null);
        updateObservers();
        return value;
    }

    public boolean makeCommand(int commandCode, String info) {
        boolean value = metaPdSm.makeCommand(commandCode, info, null,null);
        updateObservers();
        return value;
    }
    public boolean makeCommand(int commandCode, String info1, String info2) {
        boolean value = metaPdSm.makeCommand(commandCode, info1, info2,null);
        updateObservers();
        return value;
    }

    public boolean makeCommand(int commandCode, String info1, String info2, File info3) {
        boolean value = metaPdSm.makeCommand(commandCode, info1, info2,info3);
        updateObservers();
        return value;
    }

    //Get Data

    public String getUsername() {
        return metaPdSm.getUsername();
    }

    public List<TinyUser> getSystemUserList() {
        return metaPdSm.getSystemUserList();
    }

    public List<TinyMessageReceived> getMessageGroupList() {
        return metaPdSm.getMessageGroupList();
    }

    public List<TinyMessageReceived> getMessageContactList() {
        return metaPdSm.getMessageContactList();
    }

    public List<TinyUser> getContactList() {
        return metaPdSm.getContactList();
    }

    public List<TinyUser> getFriendshipRequestList() {
        return metaPdSm.getFriendshipRequestList();
    }

    public List<TinyGroup> getGroupList() {
        return metaPdSm.getGroupList();
    }

    public String getName() {
        return metaPdSm.getName();
    }

    public List<TinyGroupRequest> getGroupRequestList() {
        return metaPdSm.getGroupRequestList();
    }

    public List<TinyGroup> getMyGroupsList() {
        return metaPdSm.getMyGroupsList();
    }
}
