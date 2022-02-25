package pt.isec.metapd.server;

import pt.isec.metapd.communication.TinyServer;

import java.io.Serial;
import java.io.Serializable;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.time.Instant;

public class Server implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final String address;
    private final int tcpPort;
    private final int udpPort;

    private long lastPing;

    public Server(String address, int tcpPort, int udpPort) {
        this.address = address;
        this.tcpPort = tcpPort;
        this.udpPort = udpPort;
        lastPing = Instant.now().getEpochSecond();
    }

    public Server(InetAddress inetAddress, int tcpPort, int udpPort) {
        this.address = inetAddress.getHostAddress();
        this.tcpPort = tcpPort;
        this.udpPort = udpPort;
        lastPing = Instant.now().getEpochSecond();
    }

    public Server(TinyServer tinyServer) {
        this.address = tinyServer.address();
        this.tcpPort = tinyServer.tcpPort();
        this.udpPort = tinyServer.udpPort();
        lastPing = Instant.now().getEpochSecond();
    }

    public String getAddress() { return address; }
    public int getTcpPort() { return tcpPort; }
    public int getUdpPort() { return udpPort; }

    public long timeSincePing() { return Instant.now().getEpochSecond() - lastPing; }
    public void updateServerLastPing() { lastPing = Instant.now().getEpochSecond(); }

    @Override
    public String toString() {
        return "address: " + address + "\n" +
                "tcpPort: " + tcpPort + "\n" +
                "udpPort: " + udpPort + "\n" +
                "lastPing: " + lastPing + " (UNIX time)\n" +
                "timeSincePing: " + timeSincePing() + " (seconds)";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Server that = (Server) o;

        return this.address.equalsIgnoreCase(that.address)
                && this.tcpPort == that.getTcpPort()
                && this.udpPort == that.getUdpPort();
    }

    @Override
    public int hashCode() {
        return ByteBuffer.wrap(address.getBytes()).getInt() * tcpPort * udpPort;
    }
}
