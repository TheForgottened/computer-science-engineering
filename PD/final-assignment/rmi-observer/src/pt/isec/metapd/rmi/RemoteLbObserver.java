package pt.isec.metapd.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;

import static pt.isec.metapd.RmiObserver.LOGGER;

public class RemoteLbObserver  extends UnicastRemoteObject implements RemoteLbObserverInterface {
    public RemoteLbObserver() throws RemoteException { }

    @Override
    public void notify(String description) throws RemoteException {
        LOGGER.log(Level.INFO, description);
    }
}
