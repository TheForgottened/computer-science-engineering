package pt.isec.metapd.communication;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public record TinyMessageSent(String receiver, String text, String fileName) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return "receiver: " + receiver + "\n" +
                "text: " + text + "\n" +
                "fileName: " + fileName;
    }
}
