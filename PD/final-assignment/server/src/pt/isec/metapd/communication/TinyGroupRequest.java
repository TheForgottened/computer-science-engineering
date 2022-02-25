package pt.isec.metapd.communication;

import java.io.Serial;
import java.io.Serializable;

public record TinyGroupRequest(TinyUser requester, int groupId) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}
