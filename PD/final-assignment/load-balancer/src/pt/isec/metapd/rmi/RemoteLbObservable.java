package pt.isec.metapd.rmi;

import pt.isec.metapd.communication.TinyServer;
import pt.isec.metapd.server.Server;
import pt.isec.metapd.server.ServerConcurrentManager;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import static pt.isec.metapd.LoadBalancer.LOGGER;

public class RemoteLbObservable extends UnicastRemoteObject implements RemoteLbObservableInterface {
    private final ServerConcurrentManager serverConcurrentManager;

    private final Set<RemoteLbObserverInterface> observersSet = new HashSet<>();

    public RemoteLbObservable(ServerConcurrentManager serverConcurrentManager) throws RemoteException {
        this.serverConcurrentManager = serverConcurrentManager;
    }

    public synchronized void notifyDbUpdate(List<String> affectedUsers) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("An asynchronous notification is being sent! The following users are affected: ");

        for (int i = 0; i < affectedUsers.size(); i++) {
            stringBuilder.append("\"").append(affectedUsers.get(i)).append("\"");

            if (i == affectedUsers.size() - 1) break;

            stringBuilder.append(", ");
        }

        notifyObservers(stringBuilder.toString());
    }

    public synchronized void notifyServerAdded(Server server) {
        notifyObservers(
                "The server with address <" + server.getAddress() +
                        ">, UDP port <" + server.getUdpPort() +
                        "> and TCP port <" + server.getTcpPort() +
                        "> just registered!"
        );
    }

    public synchronized void notifyServerAdded(TinyServer tinyServer) {
        notifyObservers(
                "The server with address <" + tinyServer.address() +
                        ">, UDP port <" + tinyServer.udpPort() +
                        "> and TCP port <" + tinyServer.tcpPort() +
                        "> just registered!"
        );
    }

    public synchronized void notifyServerTimedOut(Server server) {
        notifyObservers(
                "The server with address <" + server.getAddress() +
                        ">, UDP port <" + server.getUdpPort() +
                        "> and TCP port <" + server.getTcpPort() +
                        "> just timed out!"
        );
    }

    public synchronized void notifyClientRequestedForServer() {
        notifyObservers("A client just requested a server!");
    }

    public synchronized void notifyServerRequestedForFileServer() {
        notifyObservers("A server just requested a file server!");
    }

    private synchronized void notifyObservers(String text) {
        Iterator<RemoteLbObserverInterface> it = observersSet.iterator();

        while (it.hasNext()) {
            try {
                it.next().notify(text);
            } catch (RemoteException e) {
                LOGGER.log(Level.WARNING, "Connection lost to observer.");
                it.remove();
            }
        }
    }

    @Override
    public synchronized String connectedServersToString() throws RemoteException {
        return serverConcurrentManager.connectedServersToString();
    }

    @Override
    public synchronized void addObserver(RemoteLbObserverInterface observer) throws RemoteException {
        if (observersSet.add(observer)) {
            LOGGER.log(Level.INFO, "New observer registered.");
        } else {
            LOGGER.log(Level.WARNING, "An observer that already existed tried to register again.");
        }
    }

    @Override
    public synchronized void removeObserver(RemoteLbObserverInterface observer) throws RemoteException {
        if (observersSet.remove(observer)) {
            LOGGER.log(Level.INFO, "An observer just unregistered.");
        } else {
            LOGGER.log(Level.WARNING, "An unregistered observer just tried to unregister.");
        }
    }
}
