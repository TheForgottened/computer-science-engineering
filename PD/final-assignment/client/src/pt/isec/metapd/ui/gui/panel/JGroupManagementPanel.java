package pt.isec.metapd.ui.gui.panel;

import pt.isec.metapd.communication.TinyGroup;
import pt.isec.metapd.communication.TinyUser;
import pt.isec.metapd.logic.MetaPdObservable;
import pt.isec.metapd.resources.DraculaTheme;
import pt.isec.metapd.ui.gui.cellrenderers.UserCellRenderer;

import javax.swing.*;
import java.awt.*;

public class JGroupManagementPanel extends JPanel {
    private final TinyGroup groupToManage;
    private final JFrame parentFrame;
    private final MetaPdObservable metaPdObservable;

    public JGroupManagementPanel(TinyGroup groupToManage, JFrame parentFrame, MetaPdObservable metaPdObservable) {
        this.groupToManage = groupToManage;
        this.parentFrame = parentFrame;
        this.metaPdObservable = metaPdObservable;

        stylizeWindow();
    }

    private void stylizeWindow() {
        setBackground(DraculaTheme.BACKGROUND);
        setLayout(new GridBagLayout());

        JPanel verticalPanel = new JPanel();
        verticalPanel.setOpaque(false);
        verticalPanel.setLayout(new BoxLayout(verticalPanel, BoxLayout.Y_AXIS));

        JLabel nameLabel = new JLabel("New Name");
        nameLabel.setForeground(DraculaTheme.FOREGROUND);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField nameTextField = new JTextField();
        nameTextField.setPreferredSize(new Dimension(200, 20));
        nameTextField.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton updateNameButton = new JButton("Update Name");
        updateNameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        updateNameButton.addActionListener(actionListener -> SwingUtilities.invokeLater(() -> {
            if (nameTextField.getText().isBlank()) return;

            if (metaPdObservable.makeCommand(21,
                    "" + groupToManage.id(),
                    nameTextField.getText().trim()
            )) {
                JOptionPane.showMessageDialog(parentFrame,
                        "Name changed successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                JOptionPane.showMessageDialog(parentFrame,
                        "Unable to change name!",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        }));

        DefaultListModel<TinyUser> membersModel = new DefaultListModel<>();

        for (TinyUser member : groupToManage.members()) membersModel.addElement(member);

        JList<TinyUser> membersList = new JList<>(membersModel);
        membersList.setCellRenderer(new UserCellRenderer());
        membersList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane membersScroller = new JScrollPane(membersList);
        membersScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        membersScroller.setOpaque(false);
        membersScroller.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton kickButton = new JButton("Kick Member");
        kickButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        kickButton.addActionListener(actionListener -> SwingUtilities.invokeLater(() -> {
            TinyUser tinyUser = membersList.getSelectedValue();
            if (tinyUser == null) return;

            if (metaPdObservable.makeCommand(23,
                    "" + tinyUser.username(),
                    "" + groupToManage.id()
            )) {
                JOptionPane.showMessageDialog(parentFrame,
                        "Member kicked successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

                membersModel.removeElement(tinyUser);
                groupToManage.members().remove(tinyUser);
            } else {
                JOptionPane.showMessageDialog(parentFrame,
                        "Unable to kick member!",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        }));

        JButton deleteGroupButton = new JButton("Delete Group");
        deleteGroupButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        deleteGroupButton.addActionListener(actionListener -> SwingUtilities.invokeLater(() -> {
            if (metaPdObservable.makeCommand(22, "" + groupToManage.id())) {
                JOptionPane.showMessageDialog(parentFrame,
                        "Group deleted successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

                parentFrame.setVisible(false);
                parentFrame.dispose();
            } else {
                JOptionPane.showMessageDialog(parentFrame,
                        "Unable to delete group!",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        }));

        verticalPanel.add(nameLabel);
        verticalPanel.add(nameTextField);
        verticalPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        verticalPanel.add(updateNameButton);
        verticalPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        verticalPanel.add(membersScroller);
        verticalPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        verticalPanel.add(kickButton);
        verticalPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        verticalPanel.add(deleteGroupButton);

        add(verticalPanel);
    }
}
