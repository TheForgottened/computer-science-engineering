package pt.isec.metapd.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteLbObservableInterface extends Remote {
    String connectedServersToString() throws RemoteException;

    void addObserver(RemoteLbObserverInterface observer) throws RemoteException;
    void removeObserver(RemoteLbObserverInterface observer) throws RemoteException;
}
