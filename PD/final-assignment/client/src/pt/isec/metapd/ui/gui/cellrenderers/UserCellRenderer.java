package pt.isec.metapd.ui.gui.cellrenderers;

import pt.isec.metapd.communication.TinyUser;

import javax.swing.*;
import java.awt.*;

public class UserCellRenderer extends DefaultListCellRenderer {
    public UserCellRenderer() { super(); }

    @Override
    public Component getListCellRendererComponent(
            JList list,
            Object value,
            int index,
            boolean isSelected,
            boolean cellHasFocus
    ) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        TinyUser tinyUser = (TinyUser) value;

        setText(tinyUser.username() + " aka " + tinyUser.name() + " (" + (tinyUser.state() ? "ON" : "OFF") + ")");
        return this;
    }
}
