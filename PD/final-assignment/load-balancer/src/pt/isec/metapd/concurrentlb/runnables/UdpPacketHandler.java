package pt.isec.metapd.concurrentlb.runnables;

import pt.isec.metapd.communication.TinyIP;
import pt.isec.metapd.communication.TinyRequest;
import pt.isec.metapd.communication.TinyServer;
import pt.isec.metapd.resources.MetaPDConstants;
import pt.isec.metapd.resources.RequestType;
import pt.isec.metapd.rmi.RemoteLbObservable;
import pt.isec.metapd.server.Server;
import pt.isec.metapd.server.ServerConcurrentManager;

import java.io.*;
import java.net.*;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;

import static pt.isec.metapd.LoadBalancer.LOGGER;

public class UdpPacketHandler implements Runnable {
    private final AtomicBoolean mustStop;
    private final ServerConcurrentManager serverConcurrentManager;
    private final RemoteLbObservable remoteLbObservable;
    private final int udpPort;

    public UdpPacketHandler(
            AtomicBoolean mustStop,
            ServerConcurrentManager serverConcurrentManager,
            RemoteLbObservable remoteLbObservable,
            int udpPort
    ) {
        this.mustStop = mustStop;
        this.serverConcurrentManager = serverConcurrentManager;
        this.remoteLbObservable = remoteLbObservable;
        this.udpPort = udpPort;
    }

    @Override
    public void run() {
        LOGGER.info("UdpPacketHandler thread started.");

        try (DatagramSocket datagramSocket = new DatagramSocket(udpPort)) {
            datagramSocket.setSoTimeout(MetaPDConstants.MAX_TIMEOUT * 1000);

            while (!mustStop.get()) {
                DatagramPacket datagramPacket = new DatagramPacket(
                        new byte[MetaPDConstants.MAX_PACKET_SIZE],
                        MetaPDConstants.MAX_PACKET_SIZE
                );

                try {
                    datagramSocket.receive(datagramPacket);

                    Thread thread = new Thread(new RequestHandler(
                            serverConcurrentManager,
                            remoteLbObservable,
                            datagramSocket,
                            datagramPacket
                    ));
                    thread.start();
                } catch (SocketTimeoutException e) {
                    /* this is needed so the server can close gracefully */
                } catch (IOException e) {
                    LOGGER.log(
                            Level.WARNING,
                            "Error receiving particular UDP packet from server.\n{0}",
                            e.toString()
                    );
                }

                serverConcurrentManager.checkForTimeoutAndNotify(remoteLbObservable);
            }
        } catch (SocketException e) {
            LOGGER.log(Level.SEVERE, "Couldn't open socket to receive server pings!\n{0}", e.toString());
            mustStop.set(true);
        }
    }

    private static class RequestHandler implements Runnable {
        private final ServerConcurrentManager serverConcurrentManager;
        private final RemoteLbObservable remoteLbObservable;

        private final DatagramSocket datagramSocket;
        private final DatagramPacket datagramPacket;

        private RequestHandler(
                ServerConcurrentManager serverConcurrentManager,
                RemoteLbObservable remoteLbObservable,
                DatagramSocket datagramSocket,
                DatagramPacket datagramPacket
        ) {
            this.serverConcurrentManager = serverConcurrentManager;
            this.remoteLbObservable = remoteLbObservable;
            this.datagramSocket = datagramSocket;
            this.datagramPacket = datagramPacket;
        }

        @Override
        public void run() {
            try {
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                        datagramPacket.getData(),
                        0,
                        datagramPacket.getLength()
                );

                ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                TinyRequest tinyRequest = (TinyRequest) objectInputStream.readObject();

                switch (tinyRequest.requestType()) {
                    case PING -> ping(tinyRequest, datagramPacket.getPort());
                    case GET_VALID_FILE_SERVER -> getValidFileServer();
                    case GET_VALID_SERVER -> getValidServer();
                    case DB_UPDATE -> dbUpdate(tinyRequest);

                    default -> LOGGER.log(
                            Level.WARNING,
                            "Invalid UDP request " + tinyRequest.requestType() +
                            " from " + datagramPacket.getAddress().getHostAddress() + "."
                    );
                }
            } catch (IOException | ClassNotFoundException e) {
                LOGGER.log(Level.WARNING, "Error reading particular UDP packet from server.\n{0}", e.toString());
            }
        }

        private void ping(TinyRequest tinyRequest, int udpPort) {
            int tcpPort = (Integer) tinyRequest.object();
            TinyServer tinyServer = new TinyServer(
                    datagramPacket.getAddress(),
                    tcpPort,
                    udpPort
            );

            if (!serverConcurrentManager.updateServerLastPing(tinyServer)) {
                serverConcurrentManager.addServer(tinyServer);
                remoteLbObservable.notifyServerAdded(tinyServer);
            }
        }

        private void getValidFileServer() {
            InetAddress address = datagramPacket.getAddress();
            int port = datagramPacket.getPort();

            try (
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)
            ) {
                Server server = null;

                while (server == null) {
                    server = serverConcurrentManager.getFtpServer();
                }

                objectOutputStream.writeObject(new TinyRequest(
                        RequestType.GET_VALID_FILE_SERVER,
                        new TinyIP(server.getAddress(), server.getTcpPort())
                ));
                objectOutputStream.flush();

                DatagramPacket answerDatagramPacket = new DatagramPacket(
                        byteArrayOutputStream.toByteArray(),
                        byteArrayOutputStream.size(),
                        address,
                        port
                );

                datagramSocket.send(answerDatagramPacket);
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Unable to answer request!\n{0}", e.toString());
            }

            remoteLbObservable.notifyServerRequestedForFileServer();
        }

        private void getValidServer() {
            InetAddress address = datagramPacket.getAddress();
            int port = datagramPacket.getPort();

            try (
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)
                    ) {
                Server server = null;

                while (server == null) {
                    server = serverConcurrentManager.getTcpServer();
                }

                objectOutputStream.writeObject(new TinyIP(server.getAddress(), server.getTcpPort()));
                objectOutputStream.flush();

                DatagramPacket answerDatagramPacket = new DatagramPacket(
                        byteArrayOutputStream.toByteArray(),
                        byteArrayOutputStream.size(),
                        address,
                        port
                );

                datagramSocket.send(answerDatagramPacket);
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Unable to answer request!\n{0}", e.toString());
            }

            remoteLbObservable.notifyClientRequestedForServer();
        }

        private void dbUpdate(TinyRequest tinyRequest) {
            Iterator<Server> it = serverConcurrentManager.iterator();

            while (it.hasNext()) {
                Server server = it.next();

                try (
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)
                ) {
                    objectOutputStream.writeObject(tinyRequest);
                    objectOutputStream.flush();

                    DatagramPacket answerDatagramPacket = new DatagramPacket(
                            byteArrayOutputStream.toByteArray(),
                            byteArrayOutputStream.size(),
                            InetAddress.getByName(server.getAddress()),
                            server.getUdpPort()
                    );

                    datagramSocket.send(answerDatagramPacket);
                } catch (IOException e) {
                    LOGGER.log(Level.SEVERE, "Unable to answer request!\n{0}", e.toString());
                }
            }

            List<String> affectedUsers = (List<String>) tinyRequest.object();
            remoteLbObservable.notifyDbUpdate(affectedUsers);
        }
    }
}
