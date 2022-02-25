package pt.isec.metapd.concurrentserver.runnables;

import pt.isec.metapd.communication.TinyFile;
import pt.isec.metapd.communication.TinyIP;
import pt.isec.metapd.communication.TinyRequest;
import pt.isec.metapd.files.FileUtility;
import pt.isec.metapd.repository.ServerRepository;
import pt.isec.metapd.resources.MetaPDConstants;
import pt.isec.metapd.resources.RequestType;

import java.io.*;
import java.net.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;

import static pt.isec.metapd.Server.LOGGER;

public class UdpPacketHandler implements Runnable {
    private final AtomicBoolean mustStop;

    private final DatagramSocket datagramSocket;

    private final ConcurrentMap<ObjectOutputStream, String> connectedClients;

    private final ServerRepository serverRepository;

    private final String lbAddress;
    private final int lbPort;

    public UdpPacketHandler(
            AtomicBoolean mustStop,
            DatagramSocket datagramSocket,
            ConcurrentMap<ObjectOutputStream, String> connectedClients,
            ServerRepository serverRepository,
            String lbAddress,
            int lbPort
    ) {
        this.mustStop = mustStop;
        this.datagramSocket = datagramSocket;
        this.connectedClients = connectedClients;
        this.serverRepository = serverRepository;
        this.lbAddress = lbAddress;
        this.lbPort = lbPort;
    }

    @Override
    public void run() {
        try {
            datagramSocket.setSoTimeout(MetaPDConstants.MAX_TIMEOUT * 1000);

            while (!mustStop.get()) {
                DatagramPacket datagramPacket = new DatagramPacket(
                        new byte[MetaPDConstants.MAX_PACKET_SIZE],
                        MetaPDConstants.MAX_PACKET_SIZE
                );

                try {
                    datagramSocket.receive(datagramPacket);

                    Thread thread = new Thread(new RequestHandler(
                            datagramSocket,
                            datagramPacket,
                            connectedClients,
                            serverRepository,
                            lbAddress,
                            lbPort
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
            }
        } catch (SocketException e) {
            LOGGER.log(Level.SEVERE, "Couldn't open socket to receive server pings!\n{0}", e.toString());
            mustStop.set(true);
        } finally {
            datagramSocket.close();
        }
    }

    private static class RequestHandler implements Runnable {
        private final DatagramSocket datagramSocket;
        private final DatagramPacket datagramPacket;

        private final ConcurrentMap<ObjectOutputStream, String> connectedClients;

        private final ServerRepository serverRepository;

        private final String lbAddress;
        private final int lbPort;

        private final AtomicBoolean isDataReady = new AtomicBoolean(false);
        private TinyIP validFileServer;

        private RequestHandler(
                DatagramSocket datagramSocket,
                DatagramPacket datagramPacket,
                ConcurrentMap<ObjectOutputStream, String> connectedClients,
                ServerRepository serverRepository,
                String lbAddress,
                int lbPort
        ) {
            this.datagramSocket = datagramSocket;
            this.datagramPacket = datagramPacket;
            this.connectedClients = connectedClients;
            this.serverRepository = serverRepository;
            this.lbAddress = lbAddress;
            this.lbPort = lbPort;
        }

        @Override
        public void run() {
            checkFileIntegrity();

            try {
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                        datagramPacket.getData(),
                        0,
                        datagramPacket.getLength()
                );

                ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                TinyRequest tinyRequest = (TinyRequest) objectInputStream.readObject();

                switch (tinyRequest.requestType()) {
                    case GET_VALID_FILE_SERVER -> {
                        validFileServer = (TinyIP) tinyRequest.object();
                        isDataReady.set(true);
                    }
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

        private void dbUpdate(TinyRequest tinyRequest) {
            List<String> usernameList = (List<String>) tinyRequest.object();

            for (String username : usernameList) {
                for (Map.Entry<ObjectOutputStream, String> entry : connectedClients.entrySet()) {
                    if (!username.equals(entry.getValue())) continue;

                    try {
                        entry.getKey().writeObject(new TinyRequest(RequestType.UPDATE_VIEW, ""));
                        entry.getKey().flush();
                    } catch (IOException e) {
                        LOGGER.log(Level.SEVERE, "Unable to tell client to update view!\n{0}", e.toString());
                    }
                }
            }

            checkFileIntegrity();
        }

        private void checkFileIntegrity() {
            Thread thread = new Thread(() -> {
                try {
                    List<TinyFile> filesOnDb = serverRepository.getAllFiles();
                    List<String> filesLocally = FileUtility.getAllLocalFileNames();

                    for (String fileName : filesLocally) {
                        boolean flag = false;
                        for (TinyFile tinyFile : filesOnDb) {
                            if (tinyFile.toString().equals(fileName)) {
                                flag = true;
                                break;
                            }
                        }

                        if (flag) continue;

                        FileUtility.deleteIfExists(new File(FileUtility.LOCAL_FILE_PATH + fileName));
                    }

                    Socket tcpSocket = null;
                    ObjectOutputStream tcpObjectOutputStream = null;

                    for (TinyFile tinyFile : filesOnDb) {
                        if (filesLocally.contains(tinyFile.toString())) continue;

                        if (tcpSocket == null) {
                            try (
                                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)
                            ) {
                                objectOutputStream.writeObject(new TinyRequest(RequestType.GET_VALID_FILE_SERVER, ""));
                                objectOutputStream.flush();

                                DatagramPacket fileDatagramPacket = new DatagramPacket(
                                        byteArrayOutputStream.toByteArray(),
                                        byteArrayOutputStream.size(),
                                        InetAddress.getByName(lbAddress),
                                        lbPort
                                );
                                datagramSocket.send(fileDatagramPacket);

                                while (!isDataReady.get());

                                tcpSocket = new Socket(
                                        InetAddress.getByName(validFileServer.address()),
                                        validFileServer.port()
                                );
                                tcpObjectOutputStream = new ObjectOutputStream(tcpSocket.getOutputStream());
                            }
                        }

                        tcpObjectOutputStream.writeObject(new TinyRequest(RequestType.GET_FILE, tinyFile.messageId()));
                        FileUtility.downloadFile(
                                new File(FileUtility.LOCAL_FILE_PATH + tinyFile.toString()),
                                tcpSocket.getInputStream()
                        );
                    }

                } catch (SQLException | IOException e) {
                    LOGGER.log(Level.SEVERE, "Unable to check file integrity!\n{0}", e.toString());
                }
            });
            thread.start();
        }
    }
}
