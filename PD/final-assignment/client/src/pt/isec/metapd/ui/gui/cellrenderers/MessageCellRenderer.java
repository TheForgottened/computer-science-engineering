package pt.isec.metapd.ui.gui.cellrenderers;

import pt.isec.metapd.communication.TinyMessageReceived;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;

public class MessageCellRenderer extends DefaultListCellRenderer {
    public MessageCellRenderer() { super(); }

    @Override
    public Component getListCellRendererComponent(
            JList list,
            Object value,
            int index,
            boolean isSelected,
            boolean cellHasFocus
    ) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        TinyMessageReceived tinyMessageReceived = (TinyMessageReceived) value;

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy - HH:mm");

        if (!tinyMessageReceived.isGroupMessage()) {
            setText(
                    "<html>" +
                    tinyMessageReceived.senderUsername() + " sent at " + sdf.format(tinyMessageReceived.sendDate()) + ":<br/>" +
                    tinyMessageReceived.text() + "<br/><br/>" +
                    "File associated: " + (tinyMessageReceived.fileName() == null
                            || tinyMessageReceived.fileName().isBlank() ? "NONE" : tinyMessageReceived.fileName()) +
                    "</html>"
            );
        } else {
            setText(
                    "<html>" +
                    "GROUP &lt;" + tinyMessageReceived.groupName() + "&gt;<br/>" +
                    tinyMessageReceived.senderUsername() + " sent at " + sdf.format(tinyMessageReceived.sendDate()) + ":<br/>" +
                    tinyMessageReceived.text() + "<br/><br/>" +
                    "File associated: " + (tinyMessageReceived.fileName() == null
                            || tinyMessageReceived.fileName().isBlank() ? "NONE" : tinyMessageReceived.fileName()) +
                    "</html>"
            );
        }
        return this;
    }
}
