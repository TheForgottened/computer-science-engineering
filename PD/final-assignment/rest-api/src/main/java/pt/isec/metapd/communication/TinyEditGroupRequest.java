package pt.isec.metapd.communication;

import java.io.Serial;
import java.io.Serializable;

public record TinyEditGroupRequest(int id, String name) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}
