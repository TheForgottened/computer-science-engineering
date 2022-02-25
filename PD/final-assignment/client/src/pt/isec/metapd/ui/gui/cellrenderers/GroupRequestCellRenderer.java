package pt.isec.metapd.ui.gui.cellrenderers;

import pt.isec.metapd.communication.TinyGroup;
import pt.isec.metapd.communication.TinyGroupRequest;
import pt.isec.metapd.communication.TinyUser;

import javax.swing.*;
import java.awt.*;

public class GroupRequestCellRenderer extends DefaultListCellRenderer {
    public GroupRequestCellRenderer() { super(); }

    @Override
    public Component getListCellRendererComponent(
            JList list,
            Object value,
            int index,
            boolean isSelected,
            boolean cellHasFocus
    ) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        TinyGroupRequest tinyGroupRequest = (TinyGroupRequest) value;

        setText(
                tinyGroupRequest.requester().username() + " aka " + tinyGroupRequest.requester().name() +
                " wants to join " + tinyGroupRequest.groupId()
        );
        return this;
    }
}
