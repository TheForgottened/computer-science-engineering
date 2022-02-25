package pt.isec.metapd.concurrentserver;

import pt.isec.metapd.concurrentserver.runnables.CommandsInterpreter;
import pt.isec.metapd.concurrentserver.runnables.LoadBalancerPinger;
import pt.isec.metapd.concurrentserver.runnables.TcpHandler;
import pt.isec.metapd.concurrentserver.runnables.UdpPacketHandler;
import pt.isec.metapd.repository.ServerRepository;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;

import static pt.isec.metapd.Server.LOGGER;

public class ConcurrentServer {
    private final AtomicBoolean mustStop;

    private final ServerRepository serverRepository;

    private final ConcurrentMap<ObjectOutputStream, String> connectedClients = new ConcurrentHashMap<>();

    private final String lbAddress;
    private final int lbPort;

    public ConcurrentServer(AtomicBoolean mustStop, String lbAddress, int lbPort, String dbAddress) {
        this.mustStop = mustStop;
        this.lbAddress = lbAddress;
        this.lbPort = lbPort;

        this.serverRepository = new ServerRepository(dbAddress);
    }

    public void start() {
        Thread commandsThread = new Thread(new CommandsInterpreter(mustStop));
        commandsThread.start();

        ServerSocket serverTcpSocket = null;
        DatagramSocket serverUdpSocket = null;

        try {
            serverTcpSocket = new ServerSocket(0);
            serverUdpSocket = new DatagramSocket();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Unable to open TCP sockets!\n{0}", e.toString());
            return;
        }

        Thread loadBalancerPingerThread = new Thread(new LoadBalancerPinger(
                mustStop,
                serverUdpSocket,
                serverTcpSocket.getLocalPort(),
                lbAddress,
                lbPort
        ));
        loadBalancerPingerThread.start();

        Thread tcpHandlerThread = new Thread(new TcpHandler(
                mustStop,
                connectedClients,
                serverTcpSocket,
                serverUdpSocket,
                lbAddress,
                lbPort,
                serverRepository
        ));
        tcpHandlerThread.start();

        Thread udpPacketHandlerThread = new Thread(new UdpPacketHandler(
                mustStop,
                serverUdpSocket,
                connectedClients,
                serverRepository,
                lbAddress,
                lbPort
        ));
        udpPacketHandlerThread.start();
    }
}
