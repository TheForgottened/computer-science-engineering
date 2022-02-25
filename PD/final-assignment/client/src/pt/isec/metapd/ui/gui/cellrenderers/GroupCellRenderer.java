package pt.isec.metapd.ui.gui.cellrenderers;

import pt.isec.metapd.communication.TinyGroup;
import pt.isec.metapd.communication.TinyUser;

import javax.swing.*;
import java.awt.*;

public class GroupCellRenderer extends DefaultListCellRenderer {
    public GroupCellRenderer() { super(); }

    @Override
    public Component getListCellRendererComponent(
            JList list,
            Object value,
            int index,
            boolean isSelected,
            boolean cellHasFocus
    ) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        TinyGroup tinyGroup = (TinyGroup) value;

        StringBuilder stringBuilder = new StringBuilder();

        for (TinyUser member : tinyGroup.members()) {
            stringBuilder.append("<br/>&nbsp;&nbsp;&nbsp;&nbsp;")
                    .append(member.username())
                    .append(" aka ")
                    .append(member.name())
                    .append(member.state() ? " (ON)" : " (OFF)");
        }

        setText("<html>&lt;" + tinyGroup.name() + "&gt;" + stringBuilder + "</html>");
        return this;
    }
}
