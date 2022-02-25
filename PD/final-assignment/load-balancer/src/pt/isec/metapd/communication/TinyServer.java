package pt.isec.metapd.communication;

import java.io.Serial;
import java.io.Serializable;
import java.net.InetAddress;

public record TinyServer(String address, int tcpPort, int udpPort) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public TinyServer(InetAddress address, int tcpPort, int ftpPort) {
        this(address.getHostAddress(), tcpPort, ftpPort);
    }

    @Override
    public String toString() {
        return "address: <" + address + "> tcpPort: <" + tcpPort + "> udpPort: <" + udpPort + ">";
    }
}
