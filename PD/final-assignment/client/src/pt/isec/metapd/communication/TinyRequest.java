package pt.isec.metapd.communication;


import pt.isec.metapd.resources.RequestType;

import java.io.Serial;
import java.io.Serializable;

public record TinyRequest(RequestType requestType, Object object) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}