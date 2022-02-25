package pt.isec.metapd.communication;

import java.io.Serial;
import java.io.Serializable;

public record TinyAnswerContactRequest(String requestSenderUsername, boolean answer) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}
