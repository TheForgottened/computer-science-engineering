package pt.isec.metapd.communication;

import java.io.Serial;
import java.io.Serializable;
import java.net.InetAddress;

public record TinyIP(String address, int port) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public TinyIP(InetAddress iNetAddress, int port) {
        this(iNetAddress.toString(), port);
    }

    @Override
    public String toString() {
        return address + ":" + port;
    }
}
