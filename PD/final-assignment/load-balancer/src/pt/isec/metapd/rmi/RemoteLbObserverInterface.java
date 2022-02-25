package pt.isec.metapd.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteLbObserverInterface extends Remote {
    void notify(String description) throws RemoteException;
}
