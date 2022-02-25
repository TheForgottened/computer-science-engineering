package pt.isec.metapd.communication;

import java.io.Serial;
import java.io.Serializable;

public record TinyCredentials(String username, String md5Password) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return username + ":" + md5Password;
    }
}
