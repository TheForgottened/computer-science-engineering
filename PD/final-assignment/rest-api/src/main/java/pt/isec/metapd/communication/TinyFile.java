package pt.isec.metapd.communication;

import java.io.Serial;
import java.io.Serializable;

public record TinyFile(int messageId, String name) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return messageId + "_" + name;
    }
}
