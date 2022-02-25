package pt.isec.metapd.communication;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public record TinyMessageReceived(
        int id,
        String senderUsername,
        String text,
        String fileName,
        Timestamp sendDate,
        String groupName
) implements Serializable, Comparable<TinyMessageReceived> {
    @Serial
    private static final long serialVersionUID = 1L;

    public boolean isGroupMessage() { return !groupName.isBlank(); }

    @Override
    public int compareTo(TinyMessageReceived that) {
        if (this.sendDate.after(that.sendDate())) {
            return -1;
        } else if (this.sendDate.before(that.sendDate())) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy - HH:mm");

        return "id: " + id + "\n" +
                "senderUsername: " + senderUsername + "\n" +
                "text: " + text + "\n" +
                "sendDate: " + simpleDateFormat.format(sendDate) + "\n" +
                "fileName: " + fileName;
    }
}
