package pt.isec.metapd.ui.gui.panel;

import pt.isec.metapd.communication.TinyGroup;
import pt.isec.metapd.communication.TinyGroupRequest;
import pt.isec.metapd.communication.TinyUser;
import pt.isec.metapd.logic.MetaPdObservable;
import pt.isec.metapd.resources.DraculaTheme;
import pt.isec.metapd.ui.gui.cellrenderers.GroupCellRenderer;
import pt.isec.metapd.ui.gui.cellrenderers.GroupRequestCellRenderer;
import pt.isec.metapd.ui.gui.cellrenderers.UserCellRenderer;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class JRequestsPanel extends JPanel {
    private final JFrame parentFrame;
    private final MetaPdObservable metaPdObservable;

    public JRequestsPanel(JFrame parentFrame, MetaPdObservable metaPdObservable) {
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

        JLabel contactRequestsLabel = new JLabel("Contact Requests");
        contactRequestsLabel.setForeground(DraculaTheme.FOREGROUND);
        contactRequestsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        DefaultListModel<TinyUser> contactRequestsModel = new DefaultListModel<>();

        for (TinyUser tinyUser : metaPdObservable.getFriendshipRequestList()) contactRequestsModel.addElement(tinyUser);

        JList<TinyUser> contactRequestsList = new JList<>(contactRequestsModel);
        contactRequestsList.setCellRenderer(new UserCellRenderer());
        contactRequestsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane contactRequestsScroller = new JScrollPane(contactRequestsList);
        contactRequestsScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        contactRequestsScroller.setOpaque(false);
        contactRequestsScroller.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel contactRequestsButtonPanel = new JPanel();
        contactRequestsButtonPanel.setOpaque(false);
        contactRequestsButtonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contactRequestsButtonPanel.setLayout(new BoxLayout(contactRequestsButtonPanel, BoxLayout.X_AXIS));

        JButton acceptContactRequestButton = new JButton("Accept Contact Request");
        acceptContactRequestButton.addActionListener(actionListener -> {
            TinyUser tinyUser = contactRequestsList.getSelectedValue();
            if (tinyUser == null) return;

            SwingUtilities.invokeLater(() -> {
                if (metaPdObservable.makeCommand(12, tinyUser.username())) {
                    JOptionPane.showMessageDialog(parentFrame,
                            "Contact request accepted successfully!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                    contactRequestsModel.removeElement(tinyUser);
                } else {
                    JOptionPane.showMessageDialog(parentFrame,
                            "Unable to accept contact request!",
                            "Warning",
                            JOptionPane.WARNING_MESSAGE
                    );
                }
            });
        });

        JButton rejectContactRequestButton = new JButton("Reject Contact Request");
        rejectContactRequestButton.addActionListener(actionListener -> {
            TinyUser tinyUser = contactRequestsList.getSelectedValue();
            if (tinyUser == null) return;

            SwingUtilities.invokeLater(() -> {
                if (metaPdObservable.makeCommand(13, tinyUser.username())) {
                    JOptionPane.showMessageDialog(parentFrame,
                            "Contact request rejected successfully!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                    contactRequestsModel.removeElement(tinyUser);
                } else {
                    JOptionPane.showMessageDialog(parentFrame,
                            "Unable to reject contact request!",
                            "Warning",
                            JOptionPane.WARNING_MESSAGE
                    );
                }
            });
        });

        contactRequestsButtonPanel.add(acceptContactRequestButton);
        contactRequestsButtonPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        contactRequestsButtonPanel.add(rejectContactRequestButton);

        JLabel groupRequestsLabel = new JLabel("Group Requests");
        groupRequestsLabel.setForeground(DraculaTheme.FOREGROUND);
        groupRequestsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        DefaultListModel<TinyGroupRequest> groupRequestsModel = new DefaultListModel<>();

        for (TinyGroupRequest tinyGroupRequest : metaPdObservable.getGroupRequestList()) groupRequestsModel.addElement(tinyGroupRequest);

        JList<TinyGroupRequest> groupRequestsList = new JList<>(groupRequestsModel);
        groupRequestsList.setCellRenderer(new GroupRequestCellRenderer());
        groupRequestsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane groupRequestsScroller = new JScrollPane(groupRequestsList);
        groupRequestsScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        groupRequestsScroller.setOpaque(false);
        groupRequestsScroller.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel groupRequestsButtonPanel = new JPanel();
        groupRequestsButtonPanel.setOpaque(false);
        groupRequestsButtonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        groupRequestsButtonPanel.setLayout(new BoxLayout(groupRequestsButtonPanel, BoxLayout.X_AXIS));

        JButton acceptGroupRequestButton = new JButton("Accept Group Request");
        acceptGroupRequestButton.addActionListener(actionListener -> {
            TinyGroupRequest tinyGroupRequest = groupRequestsList.getSelectedValue();
            if (tinyGroupRequest == null) return;

            SwingUtilities.invokeLater(() -> {
                if (metaPdObservable.makeCommand(
                        17,
                        tinyGroupRequest.requester().username(),
                        "" + tinyGroupRequest.groupId()
                )) {
                    JOptionPane.showMessageDialog(parentFrame,
                            "Group request accepted successfully!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                    groupRequestsModel.removeElement(tinyGroupRequest);
                } else {
                    JOptionPane.showMessageDialog(parentFrame,
                            "Unable to accept group request!",
                            "Warning",
                            JOptionPane.WARNING_MESSAGE
                    );
                }
            });
        });

        JButton rejectGroupRequestButton = new JButton("Reject Group Request");
        rejectGroupRequestButton.addActionListener(actionListener -> {
            TinyGroupRequest tinyGroupRequest = groupRequestsList.getSelectedValue();
            if (tinyGroupRequest == null) return;

            SwingUtilities.invokeLater(() -> {
                if (metaPdObservable.makeCommand(
                        18,
                        tinyGroupRequest.requester().username(),
                        "" + tinyGroupRequest.groupId()
                )) {
                    JOptionPane.showMessageDialog(parentFrame,
                            "Group request rejected successfully!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                    groupRequestsModel.removeElement(tinyGroupRequest);
                } else {
                    JOptionPane.showMessageDialog(parentFrame,
                            "Unable to reject group request!",
                            "Warning",
                            JOptionPane.WARNING_MESSAGE
                    );
                }
            });
        });

        groupRequestsButtonPanel.add(acceptGroupRequestButton);
        groupRequestsButtonPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        groupRequestsButtonPanel.add(rejectGroupRequestButton);

        verticalPanel.add(contactRequestsLabel);
        verticalPanel.add(contactRequestsScroller);
        verticalPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        verticalPanel.add(contactRequestsButtonPanel);
        verticalPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        verticalPanel.add(groupRequestsLabel);
        verticalPanel.add(groupRequestsScroller);
        verticalPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        verticalPanel.add(groupRequestsButtonPanel);
        verticalPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        add(verticalPanel);
    }
}
