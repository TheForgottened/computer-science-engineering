package pt.isec.metapd.concurrentlb;

import pt.isec.metapd.concurrentlb.runnables.CommandsInterpreter;
import pt.isec.metapd.concurrentlb.runnables.UdpPacketHandler;
import pt.isec.metapd.resources.MetaPDConstants;
import pt.isec.metapd.rmi.RemoteLbObservable;
import pt.isec.metapd.server.ServerConcurrentManager;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;

import static pt.isec.metapd.LoadBalancer.LOGGER;

public class ConcurrentLb {
    private final ServerConcurrentManager serverConcurrentManager = new ServerConcurrentManager();

    private RemoteLbObservable remoteLbObservable = null;

    private final AtomicBoolean mustStop;
    private final int udpPort;

    public ConcurrentLb (AtomicBoolean mustStop, int udpPort) {
        this.mustStop = mustStop;
        this.udpPort = udpPort;
    }

    public void start() {
        registerRemoteService();

        if (mustStop.get() || remoteLbObservable == null) return;

        Thread commandsThread = new Thread(new CommandsInterpreter(mustStop));
        commandsThread.start();

        Thread lbUdpPacketHandlerThread = new Thread(new UdpPacketHandler(
                mustStop,
                serverConcurrentManager,
                remoteLbObservable,
                udpPort
        ));
        lbUdpPacketHandlerThread.start();
    }

    private void registerRemoteService() {
        try {
            LocateRegistry.createRegistry(Registry.REGISTRY_PORT);

            remoteLbObservable = new RemoteLbObservable(serverConcurrentManager);
            LOGGER.log(Level.INFO, "RMI service <{0}> created and running.", MetaPDConstants.RMI_SERVICE_NAME);

            Naming.bind("rmi://localhost/" + MetaPDConstants.RMI_SERVICE_NAME, remoteLbObservable);
            LOGGER.log(Level.INFO, "RMI service <{0}> registered on the registry.", MetaPDConstants.RMI_SERVICE_NAME);
        } catch(RemoteException e) {
            LOGGER.log(Level.SEVERE, "RMI remote exception: {0}", e.toString());
            mustStop.set(true);
            return;
        } catch(MalformedURLException | AlreadyBoundException e) {
            LOGGER.log(Level.SEVERE, "RMI exception: {0}", e.toString());
            mustStop.set(true);
            return;
        }

        new Thread(() -> {
            while (!mustStop.get()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {
                    Thread.currentThread().interrupt();
                }
            }

            try {
                UnicastRemoteObject.unexportObject(remoteLbObservable, true);
            } catch (NoSuchObjectException e) {
                LOGGER.log(Level.SEVERE, "Unable to terminate RMI service: {0}", e.toString());
            }
        }).start();
    }
}
