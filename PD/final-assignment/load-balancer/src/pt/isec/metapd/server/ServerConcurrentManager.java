package pt.isec.metapd.server;

import pt.isec.metapd.communication.TinyServer;
import pt.isec.metapd.rmi.RemoteLbObservable;

import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ServerConcurrentManager {
    private static final int MIN_PING_TIME = 20 /* seconds */;
    private final Set<Server> serversSet = ConcurrentHashMap.newKeySet();

    private int lastTcpServerIndex = -1;
    private int lastFtpServerIndex = -1;

    public boolean addServer(Server server) { return serversSet.add(server); }
    public boolean addServer(TinyServer tinyServer) { return addServer(new Server(tinyServer)); }

    // Remove servers from the list that haven't pinged in the last three cycles
    public void checkForTimeout() {
        serversSet.removeIf(tinyServer -> tinyServer.timeSincePing() >= MIN_PING_TIME * 3);
    }

    public void checkForTimeoutAndNotify(RemoteLbObservable remoteLbObservable) {
        Iterator<Server> iterator = serversSet.iterator();

        while (iterator.hasNext()) {
            Server tempServer = iterator.next();

            if (tempServer.timeSincePing() < MIN_PING_TIME * 3) continue;

            remoteLbObservable.notifyServerTimedOut(tempServer);
            iterator.remove();
        }
    }

    public boolean updateServerLastPing(Server server) {
        for (Server temp : serversSet) {
            if (temp.equals(server)) {
                temp.updateServerLastPing();
                return true;
            }
        }

        return false;
    }

    public boolean updateServerLastPing(TinyServer tinyServer) { return updateServerLastPing(new Server(tinyServer)); }

    public synchronized Server getTcpServer() throws EmptyStackException {
        if (serversSet.isEmpty()) throw new EmptyStackException();

        int desired = lastTcpServerIndex + 1;
        if (desired >= serversSet.size()) desired = 0;

        int i = 0;
        for (Server server : serversSet) {
            if (i != desired) {
                i++;
                continue;
            }

            lastTcpServerIndex = desired;
            return server;
        }

        // The method will never get here
        return null;
    }

    public synchronized Server getFtpServer() throws EmptyStackException {
        if (serversSet.isEmpty()) throw new EmptyStackException();

        int desired = lastFtpServerIndex + 1;
        if (desired == serversSet.size()) desired = 0;

        int i = 0;
        for (Server server : serversSet) {
            if (i != desired) {
                i++;
                continue;
            }

            lastFtpServerIndex = desired;
            return server;
        }

        // The method will never get here
        return null;
    }

    public String connectedServersToString() {
        StringBuilder stringBuilder = new StringBuilder();

        for (Server server : serversSet) {
            stringBuilder.append("******\n");
            stringBuilder.append(server);
            stringBuilder.append("\n");
        }
        stringBuilder.append("******");

        return stringBuilder.toString();
    }

    public Iterator<Server> iterator() { return serversSet.iterator(); }
}
