package pt.isec.metapd.communication;

import java.io.Serial;
import java.io.Serializable;

public record TinyUser(String username, String name, boolean state) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return "username: " + username + " aka: " + name + (state ? " (online)" : " (offline)");
    }
}
