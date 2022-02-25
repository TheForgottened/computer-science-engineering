package pt.isec.metapd.ui.gui;

import pt.isec.metapd.communication.TinyGroup;
import pt.isec.metapd.communication.TinyMessageReceived;
import pt.isec.metapd.communication.TinyUser;
import pt.isec.metapd.logic.AppState;
import pt.isec.metapd.logic.MetaPdObservable;
import pt.isec.metapd.resources.DraculaTheme;
import pt.isec.metapd.ui.gui.cellrenderers.GroupCellRenderer;
import pt.isec.metapd.ui.gui.cellrenderers.MessageCellRenderer;
import pt.isec.metapd.ui.gui.cellrenderers.UserCellRenderer;
import pt.isec.metapd.ui.gui.panel.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class PanelManager {
    private final MetaPdObservable metaPdObservable;
    private final JFrame mainJFrame;

    private final JPanel jPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();

    public PanelManager(MetaPdObservable metaPdObservable, JFrame mainJFrame) {
        this.metaPdObservable = metaPdObservable;
        this.mainJFrame = mainJFrame;

        createView();
    }

    public JPanel getPanel() { return jPanel; }

    private void createView() {
        jPanel.setLayout(cardLayout);

        jPanel.add(new AwaitsCommandPanel(metaPdObservable), "AwaitsCommand");
        jPanel.add(new AwaitsLoginPanel(metaPdObservable), "AwaitsLogin");
        jPanel.add(new AwaitsSignUpPanel(metaPdObservable), "AwaitsSignUp");

        cardLayout.show(jPanel, "AwaitsLogin");
    }

    private class AwaitsCommandPanel extends JPanel {
        private final MetaPdObservable metaPdObservable;

        private final JLabel headerLabel = new JLabel();

        private final DefaultListModel<TinyUser> allUsersModel = new DefaultListModel<>();
        private final JList<TinyUser> allUsersList = new JList<>(allUsersModel);

        private final DefaultListModel<TinyMessageReceived> myPrivateMessagesModel = new DefaultListModel<>();
        private final JList<TinyMessageReceived> myPrivateMessagesList = new JList<>(myPrivateMessagesModel);

        private final DefaultListModel<TinyUser> myContactsModel = new DefaultListModel<>();
        private final JList<TinyUser> myContactsList = new JList<>(myContactsModel);


        private final DefaultListModel<TinyGroup> allGroupsModel = new DefaultListModel<>();
        private final JList<TinyGroup> allGroupsList = new JList<>(allGroupsModel);

        private final DefaultListModel<TinyMessageReceived> myGroupMessagesModel = new DefaultListModel<>();
        private final JList<TinyMessageReceived> myGroupMessagesList = new JList<>(myGroupMessagesModel);

        private final DefaultListModel<TinyGroup> myGroupsModel = new DefaultListModel<>();
        private final JList<TinyGroup> myGroupsList = new JList<>(myGroupsModel);

        private final JButton requestsButton = new JButton("Requests (0)");

        public AwaitsCommandPanel(MetaPdObservable metaPdObservable) {
            this.metaPdObservable = metaPdObservable;
            metaPdObservable.addObserver(this);

            stylizeWindow();
        }

        @Override
        public void update(Graphics g) {
            if (metaPdObservable.getCurrentState() != AppState.AWAITS_COMMAND) return;

            cardLayout.show(jPanel, "AwaitsCommand");

            SwingUtilities.invokeLater(() -> {
                headerLabel.setText(String.format("Hello %s - %s!", metaPdObservable.getUsername(), metaPdObservable.getName()));

                int totalRequests = metaPdObservable.getGroupRequestList().size()
                        + metaPdObservable.getFriendshipRequestList().size();
                requestsButton.setText(
                        "Requests (" + totalRequests + ")");

                allUsersModel.removeAllElements();
                for (TinyUser element : metaPdObservable.getSystemUserList()) {
                    allUsersModel.addElement(element);
                }

                myPrivateMessagesModel.removeAllElements();
                for (TinyMessageReceived element : metaPdObservable.getMessageContactList()) {
                    myPrivateMessagesModel.addElement(element);
                }

                myContactsModel.removeAllElements();
                for (TinyUser element : metaPdObservable.getContactList()) {
                    myContactsModel.addElement(element);
                }

                allGroupsModel.removeAllElements();
                for (TinyGroup element : metaPdObservable.getGroupList()) {
                    allGroupsModel.addElement(element);
                }

                myGroupMessagesModel.removeAllElements();
                for (TinyMessageReceived element : metaPdObservable.getMessageGroupList()) {
                    myGroupMessagesModel.addElement(element);
                }

                myGroupsModel.removeAllElements();
                for (TinyGroup element : metaPdObservable.getMyGroupsList()) {
                    myGroupsModel.addElement(element);
                }
            });
        }

        private void stylizeWindow() {
            final Dimension MINIMUM_SIDE_SCROLLER_DIMENSION = new Dimension(0, 250);
            final Dimension MINIMUM_MIDDLE_SCROLLER_DIMENSION = new Dimension(0, 300);

            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            setLayout(new GridBagLayout());
            setBackground(DraculaTheme.BACKGROUND);

            //
            // Row 0
            //

            JPanel headerPanel = new JPanel();
            headerPanel.setOpaque(false);
            headerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

            // headerLabel declared as a member of the class
            headerLabel.setText("Hello User - Name!");
            headerLabel.setForeground(DraculaTheme.FOREGROUND);

            JButton editInfoButton = new JButton("Edit Info");
            editInfoButton.addActionListener(actionListener -> SwingUtilities.invokeLater(() -> {
                JFrame messageFrame = new JFrame();

                messageFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                messageFrame.setTitle("Edit Info");
                messageFrame.setMinimumSize(new Dimension(500, 500));
                messageFrame.setResizable(false);
                messageFrame.add(new JEditInfoPanel(messageFrame, metaPdObservable));
                messageFrame.setLocationRelativeTo(null);
                messageFrame.pack();
                messageFrame.setVisible(true);
            }));

            // requestsButton declared as a member of the class
            requestsButton.addActionListener(actionListener -> SwingUtilities.invokeLater(() -> {
                JFrame messageFrame = new JFrame();

                messageFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                messageFrame.setTitle("Edit Info");
                messageFrame.setMinimumSize(new Dimension(500, 500));
                messageFrame.setResizable(false);
                messageFrame.add(new JRequestsPanel(messageFrame, metaPdObservable));
                messageFrame.setLocationRelativeTo(null);
                messageFrame.pack();
                messageFrame.setVisible(true);
            }));

            JButton logoutButton = new JButton("Logout");
            logoutButton.addActionListener(actionListener -> SwingUtilities.invokeLater(metaPdObservable::goBack));

            headerPanel.add(headerLabel);
            headerPanel.add(editInfoButton);
            headerPanel.add(logoutButton);
            headerPanel.add(requestsButton);

            //
            // Row 1
            //

            // Column 0
            JPanel row1col0Panel = new JPanel();
            row1col0Panel.setOpaque(false);
            row1col0Panel.setAlignmentX(Component.CENTER_ALIGNMENT);
            row1col0Panel.setLayout(new BoxLayout(row1col0Panel, BoxLayout.Y_AXIS));

            JLabel allUsersLabel = new JLabel("All Users");
            allUsersLabel.setForeground(DraculaTheme.FOREGROUND);
            allUsersLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            JTextField searchUserTextField = new JTextField();
            searchUserTextField.setAlignmentX(Component.CENTER_ALIGNMENT);
            searchUserTextField.setMaximumSize(new Dimension(300, 10));
            searchUserTextField.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) { filter(); }
                @Override
                public void removeUpdate(DocumentEvent e) { filter(); }
                @Override
                public void changedUpdate(DocumentEvent e) { filter(); }

                private void filter() {
                    allUsersModel.removeAllElements();

                    for (TinyUser tinyUser : metaPdObservable.getSystemUserList()) {
                        String username = tinyUser.username();
                        String name = tinyUser.name();

                        if (name == null) {
                            if (username.contains(searchUserTextField.getText())) {
                                allUsersModel.addElement(tinyUser);
                            }
                        } else {
                            if (username.contains(searchUserTextField.getText()) || name.contains(searchUserTextField.getText())) {
                                allUsersModel.addElement(tinyUser);
                            }
                        }
                    }
                }
            });

            allUsersList.setCellRenderer(new UserCellRenderer());
            allUsersList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            JScrollPane allUserListScroller = new JScrollPane(allUsersList);
            allUserListScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            allUserListScroller.setOpaque(true);
            allUserListScroller.setSize(MINIMUM_SIDE_SCROLLER_DIMENSION);
            allUserListScroller.setAlignmentX(Component.CENTER_ALIGNMENT);

            JButton sendContactRequestButton = new JButton("Send Request");
            sendContactRequestButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            sendContactRequestButton.addActionListener(actionListener -> {
                TinyUser tinyUser = allUsersList.getSelectedValue();

                if (tinyUser == null) return;

                SwingUtilities.invokeLater(() -> {
                    if (metaPdObservable.makeCommand(10, tinyUser.username())) {
                        JOptionPane.showMessageDialog(mainJFrame,
                                "Request sent successfully!",
                                "Success",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                    } else {
                        JOptionPane.showMessageDialog(mainJFrame,
                                "Unable to send request!",
                                "Warning",
                                JOptionPane.WARNING_MESSAGE
                        );
                    }
                });
            });

            row1col0Panel.add(allUsersLabel);
            row1col0Panel.add(searchUserTextField);
            row1col0Panel.add(Box.createRigidArea(new Dimension(0, 5)));
            row1col0Panel.add(allUserListScroller);
            row1col0Panel.add(Box.createRigidArea(new Dimension(0, 10)));
            row1col0Panel.add(sendContactRequestButton);

            // Column 1
            JPanel row1col1Panel = new JPanel();
            row1col1Panel.setOpaque(false);
            row1col1Panel.setAlignmentX(Component.CENTER_ALIGNMENT);
            row1col1Panel.setLayout(new BoxLayout(row1col1Panel, BoxLayout.Y_AXIS));

            JLabel myPrivateMessagesLabel = new JLabel("Private Messages");
            myPrivateMessagesLabel.setForeground(DraculaTheme.FOREGROUND);
            myPrivateMessagesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            myPrivateMessagesList.setCellRenderer(new MessageCellRenderer());
            myPrivateMessagesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            JScrollPane myPrivateMessagesScroller = new JScrollPane(myPrivateMessagesList);
            allUserListScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            myPrivateMessagesScroller.setOpaque(false);
            myPrivateMessagesScroller.setSize(MINIMUM_MIDDLE_SCROLLER_DIMENSION);
            myPrivateMessagesScroller.setAlignmentX(Component.CENTER_ALIGNMENT);

            JPanel myPrivateMessagesButtonPanel = new JPanel();
            myPrivateMessagesButtonPanel.setOpaque(false);
            myPrivateMessagesButtonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
            myPrivateMessagesButtonPanel.setLayout(new BoxLayout(myPrivateMessagesButtonPanel, BoxLayout.X_AXIS));

            JButton deletePrivateMessageButton = new JButton("Delete Message");
            deletePrivateMessageButton.addActionListener(actionListener -> {
                TinyMessageReceived tinyMessageReceived = myPrivateMessagesList.getSelectedValue();
                if (tinyMessageReceived == null) return;

                SwingUtilities.invokeLater(() -> {
                    if (metaPdObservable.makeCommand(8, "" + tinyMessageReceived.id())) {
                        JOptionPane.showMessageDialog(mainJFrame,
                                "Message deleted successfully!",
                                "Success",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                    } else {
                        JOptionPane.showMessageDialog(mainJFrame,
                                "Unable to delete message!",
                                "Warning",
                                JOptionPane.WARNING_MESSAGE
                        );
                    }
                });
            });

            JButton downloadPrivateFileButton = new JButton("Download File");
            downloadPrivateFileButton.addActionListener(actionListener -> {
                TinyMessageReceived tinyMessageReceived = myPrivateMessagesList.getSelectedValue();
                if (tinyMessageReceived == null) return;

                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(mainJFrame,
                            "Download started!",
                            "Download",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                    metaPdObservable.makeCommand(26,
                            "" + tinyMessageReceived.id(),
                            tinyMessageReceived.fileName()
                    );

                    JOptionPane.showMessageDialog(mainJFrame,
                            "Download finished!",
                            "Download",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                });
            });

            myPrivateMessagesButtonPanel.add(deletePrivateMessageButton);
            myPrivateMessagesButtonPanel.add(Box.createRigidArea(new Dimension(5, 0)));
            myPrivateMessagesButtonPanel.add(downloadPrivateFileButton);

            row1col1Panel.add(myPrivateMessagesLabel);
            row1col1Panel.add(myPrivateMessagesScroller);
            row1col1Panel.add(Box.createRigidArea(new Dimension(0, 10)));
            row1col1Panel.add(myPrivateMessagesButtonPanel);

            // Column 2
            JPanel row1col2Panel = new JPanel();
            row1col2Panel.setOpaque(false);
            row1col2Panel.setAlignmentX(Component.CENTER_ALIGNMENT);
            row1col2Panel.setLayout(new BoxLayout(row1col2Panel, BoxLayout.Y_AXIS));

            JLabel myContactsLabel = new JLabel("Contacts");
            myContactsLabel.setForeground(DraculaTheme.FOREGROUND);
            myContactsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            JTextField searchContactsTextField = new JTextField();
            searchContactsTextField.setAlignmentX(Component.CENTER_ALIGNMENT);
            searchContactsTextField.setMaximumSize(new Dimension(300, 10));
            searchContactsTextField.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) { filter(); }
                @Override
                public void removeUpdate(DocumentEvent e) { filter(); }
                @Override
                public void changedUpdate(DocumentEvent e) { filter(); }

                private void filter() {
                    myContactsModel.removeAllElements();

                    for (TinyUser tinyUser : metaPdObservable.getContactList()) {
                        String username = tinyUser.username();
                        String name = tinyUser.name();

                        if (name == null) {
                            if (username.contains(searchContactsTextField.getText())) {
                                myContactsModel.addElement(tinyUser);
                            }
                        } else {
                            if (username.contains(searchContactsTextField.getText()) || name.contains(searchContactsTextField.getText())) {
                                myContactsModel.addElement(tinyUser);
                            }
                        }
                    }
                }
            });

            myContactsList.setCellRenderer(new UserCellRenderer());
            myContactsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            JScrollPane myContactsListScroller = new JScrollPane(myContactsList);
            myContactsListScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            myContactsListScroller.setOpaque(false);
            myContactsListScroller.setSize(MINIMUM_SIDE_SCROLLER_DIMENSION);
            myContactsListScroller.setAlignmentX(Component.CENTER_ALIGNMENT);

            JPanel myContactsButtonPanel = new JPanel();
            myContactsButtonPanel.setOpaque(false);
            myContactsButtonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
            myContactsButtonPanel.setLayout(new BoxLayout(myContactsButtonPanel, BoxLayout.X_AXIS));

            JButton sendMyContactsMessageButton = new JButton("Send Message");
            sendMyContactsMessageButton.addActionListener(actionListener -> {
                TinyUser tinyUser = myContactsList.getSelectedValue();
                if (tinyUser == null) return;

                SwingUtilities.invokeLater(() -> {
                    JFrame messageFrame = new JFrame();

                    messageFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                    messageFrame.setTitle("Send Message");
                    messageFrame.setMinimumSize(new Dimension(500, 500));
                    messageFrame.setResizable(false);
                    messageFrame.add(new JPrivateMessagePanel(tinyUser, messageFrame, metaPdObservable));
                    messageFrame.setLocationRelativeTo(null);
                    messageFrame.pack();
                    messageFrame.setVisible(true);
                });
            });

            JButton removeMyContactsButton = new JButton("Remove");
            removeMyContactsButton.addActionListener(actionListener -> {
                TinyUser tinyUser = myContactsList.getSelectedValue();
                if (tinyUser == null) return;

                SwingUtilities.invokeLater(() -> {
                    if (metaPdObservable.makeCommand(4, tinyUser.username())) {
                        JOptionPane.showMessageDialog(mainJFrame,
                                "Contact removed successfully!",
                                "Success",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                    } else {
                        JOptionPane.showMessageDialog(mainJFrame,
                                "Unable to remove contact!",
                                "Warning",
                                JOptionPane.WARNING_MESSAGE
                        );
                    }
                });
            });

            myContactsButtonPanel.add(sendMyContactsMessageButton);
            myContactsButtonPanel.add(Box.createRigidArea(new Dimension(5, 0)));
            myContactsButtonPanel.add(removeMyContactsButton);

            row1col2Panel.add(myContactsLabel);
            row1col2Panel.add(searchContactsTextField);
            row1col2Panel.add(Box.createRigidArea(new Dimension(0, 5)));
            row1col2Panel.add(myContactsListScroller);
            row1col2Panel.add(Box.createRigidArea(new Dimension(0, 10)));
            row1col2Panel.add(myContactsButtonPanel);

            //
            // Row 2
            //

            // Column 0
            JPanel row2col0Panel = new JPanel();
            row2col0Panel.setOpaque(false);
            row2col0Panel.setAlignmentX(Component.CENTER_ALIGNMENT);
            row2col0Panel.setLayout(new BoxLayout(row2col0Panel, BoxLayout.Y_AXIS));

            JLabel allGroupsLabel = new JLabel("All Groups");
            allGroupsLabel.setForeground(DraculaTheme.FOREGROUND);
            allGroupsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            JTextField searchGroupTextField = new JTextField();
            searchGroupTextField.setAlignmentX(Component.CENTER_ALIGNMENT);
            searchGroupTextField.setMaximumSize(new Dimension(300, 10));
            searchGroupTextField.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) { filter(); }
                @Override
                public void removeUpdate(DocumentEvent e) { filter(); }
                @Override
                public void changedUpdate(DocumentEvent e) { filter(); }

                private void filter() {
                    allGroupsModel.removeAllElements();

                    for (TinyGroup tinyGroup : metaPdObservable.getGroupList()) {
                        if (tinyGroup.name().contains(searchGroupTextField.getText())) {
                            allGroupsModel.addElement(tinyGroup);
                        }
                    }
                }
            });

            allGroupsList.setCellRenderer(new GroupCellRenderer());
            allGroupsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            JScrollPane allGroupsScroller = new JScrollPane(allGroupsList);
            allGroupsScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            allGroupsScroller.setOpaque(false);
            allGroupsScroller.setSize(MINIMUM_SIDE_SCROLLER_DIMENSION);
            allGroupsScroller.setAlignmentX(Component.CENTER_ALIGNMENT);

            JPanel allGroupsButtonPanel = new JPanel();
            allGroupsButtonPanel.setOpaque(false);
            allGroupsButtonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
            allGroupsButtonPanel.setLayout(new BoxLayout(allGroupsButtonPanel, BoxLayout.X_AXIS));

            JButton sendGroupRequestButton = new JButton("Send Request");
            sendGroupRequestButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            sendGroupRequestButton.addActionListener(actionListener -> {
                TinyGroup tinyGroup = allGroupsList.getSelectedValue();
                if (tinyGroup == null) return;

                SwingUtilities.invokeLater(() -> {
                    if (metaPdObservable.makeCommand(15, "" + tinyGroup.id())) {
                        JOptionPane.showMessageDialog(mainJFrame,
                                "Request sent successfully!",
                                "Success",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                    } else {
                        JOptionPane.showMessageDialog(mainJFrame,
                                "Unable to send request!",
                                "Warning",
                                JOptionPane.WARNING_MESSAGE
                        );
                    }
                });
            });

            JButton createGroupButton = new JButton("Create Group");
            createGroupButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            createGroupButton.addActionListener(actionListener -> SwingUtilities.invokeLater(() -> {
                String groupName = (String) JOptionPane.showInputDialog(
                        mainJFrame,
                        "Group Name",
                        "Create Group",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        null,
                        ""
                );

                if (groupName == null || groupName.isBlank()) {
                    JOptionPane.showMessageDialog(mainJFrame,
                            "Unable to create group!",
                            "Warning",
                            JOptionPane.WARNING_MESSAGE
                    );

                    return;
                }

                if (metaPdObservable.makeCommand(19, groupName)) {
                    JOptionPane.showMessageDialog(mainJFrame,
                            "Group created successfully!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                } else {
                    JOptionPane.showMessageDialog(mainJFrame,
                            "Unable to create group!",
                            "Warning",
                            JOptionPane.WARNING_MESSAGE
                    );
                }
            }));

            allGroupsButtonPanel.add(sendGroupRequestButton);
            allGroupsButtonPanel.add(Box.createRigidArea(new Dimension(5, 0)));
            allGroupsButtonPanel.add(createGroupButton);

            row2col0Panel.add(allGroupsLabel);
            row2col0Panel.add(searchGroupTextField);
            row2col0Panel.add(Box.createRigidArea(new Dimension(0, 5)));
            row2col0Panel.add(allGroupsScroller);
            row2col0Panel.add(Box.createRigidArea(new Dimension(0, 10)));
            row2col0Panel.add(allGroupsButtonPanel);

            // Column 1
            JPanel row2col1Panel = new JPanel();
            row2col1Panel.setOpaque(false);
            row2col1Panel.setAlignmentX(Component.CENTER_ALIGNMENT);
            row2col1Panel.setLayout(new BoxLayout(row2col1Panel, BoxLayout.Y_AXIS));

            JLabel myGroupMessagesLabel = new JLabel("My Group Messages");
            myGroupMessagesLabel.setForeground(DraculaTheme.FOREGROUND);
            myGroupMessagesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            myGroupMessagesList.setCellRenderer(new MessageCellRenderer());
            myGroupMessagesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            JScrollPane myGroupMessageScroller = new JScrollPane(myGroupMessagesList);
            myGroupMessageScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            myGroupMessageScroller.setOpaque(false);
            myGroupMessageScroller.setSize(MINIMUM_MIDDLE_SCROLLER_DIMENSION);
            myGroupMessageScroller.setAlignmentX(Component.CENTER_ALIGNMENT);

            JPanel myGroupMessagesButtonPanel = new JPanel();
            myGroupMessagesButtonPanel.setOpaque(false);
            myGroupMessagesButtonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
            myGroupMessagesButtonPanel.setLayout(new BoxLayout(myGroupMessagesButtonPanel, BoxLayout.X_AXIS));

            JButton deleteGroupMessageButton = new JButton("Delete Message");
            deleteGroupMessageButton.addActionListener(actionListener -> {
                TinyMessageReceived tinyMessageReceived = myGroupMessagesList.getSelectedValue();
                if (tinyMessageReceived == null) return;

                SwingUtilities.invokeLater(() -> {
                    if (metaPdObservable.makeCommand(8, "" + tinyMessageReceived.id())) {
                        JOptionPane.showMessageDialog(mainJFrame,
                                "Message deleted successfully!",
                                "Success",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                    } else {
                        JOptionPane.showMessageDialog(mainJFrame,
                                "Unable to delete message!",
                                "Warning",
                                JOptionPane.WARNING_MESSAGE
                        );
                    }
                });
            });

            JButton downloadGroupFileButton = new JButton("Download File");
            downloadGroupFileButton.addActionListener(actionListener -> {
                TinyMessageReceived tinyMessageReceived = myPrivateMessagesList.getSelectedValue();
                if (tinyMessageReceived == null) return;

                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(mainJFrame,
                            "Download started!",
                            "Download",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                    metaPdObservable.makeCommand(26,
                            "" + tinyMessageReceived.id(),
                            tinyMessageReceived.fileName()
                    );

                    JOptionPane.showMessageDialog(mainJFrame,
                            "Download finished!",
                            "Download",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                });
            });

            myGroupMessagesButtonPanel.add(deleteGroupMessageButton);
            myGroupMessagesButtonPanel.add(Box.createRigidArea(new Dimension(5, 0)));
            myGroupMessagesButtonPanel.add(downloadGroupFileButton);

            row2col1Panel.add(myGroupMessagesLabel);
            row2col1Panel.add(myGroupMessageScroller);
            row2col1Panel.add(Box.createRigidArea(new Dimension(0, 10)));
            row2col1Panel.add(myGroupMessagesButtonPanel);

            // Column 2
            JPanel row2col2Panel = new JPanel();
            row2col2Panel.setOpaque(false);
            row2col2Panel.setAlignmentX(Component.CENTER_ALIGNMENT);
            row2col2Panel.setLayout(new BoxLayout(row2col2Panel, BoxLayout.Y_AXIS));

            JLabel myGroupsLabel = new JLabel("My Groups");
            myGroupsLabel.setForeground(DraculaTheme.FOREGROUND);
            myGroupsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            JTextField searchMyGroupsTextField = new JTextField();
            searchMyGroupsTextField.setAlignmentX(Component.CENTER_ALIGNMENT);
            searchMyGroupsTextField.setMaximumSize(new Dimension(300, 10));
            searchMyGroupsTextField.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) { filter(); }
                @Override
                public void removeUpdate(DocumentEvent e) { filter(); }
                @Override
                public void changedUpdate(DocumentEvent e) { filter(); }

                private void filter() {
                    myGroupsModel.removeAllElements();

                    for (TinyGroup tinyGroup : metaPdObservable.getMyGroupsList()) {
                        if (tinyGroup.name().contains(searchMyGroupsTextField.getText())) {
                            myGroupsModel.addElement(tinyGroup);
                        }
                    }
                }
            });

            myGroupsList.setCellRenderer(new GroupCellRenderer());
            myGroupsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            JScrollPane myGroupsListScroller = new JScrollPane(myGroupsList);
            myGroupsListScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            myGroupsListScroller.setOpaque(false);
            myGroupsListScroller.setSize(MINIMUM_SIDE_SCROLLER_DIMENSION);
            myGroupsListScroller.setAlignmentX(Component.CENTER_ALIGNMENT);

            JPanel myGroupsButtonPanel = new JPanel();
            myGroupsButtonPanel.setOpaque(false);
            myGroupsButtonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
            myGroupsButtonPanel.setLayout(new BoxLayout(myGroupsButtonPanel, BoxLayout.X_AXIS));

            JButton sendMyGroupsMessageButton = new JButton("Send Message");
            sendMyGroupsMessageButton.addActionListener(actionListener -> {
                TinyGroup tinyGroup = myGroupsList.getSelectedValue();
                if (tinyGroup == null) return;

                SwingUtilities.invokeLater(() -> {
                    JFrame messageFrame = new JFrame();

                    messageFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                    messageFrame.setTitle("Send Group Message");
                    messageFrame.setMinimumSize(new Dimension(500, 500));
                    messageFrame.setResizable(false);
                    messageFrame.add(new JGroupMessagePanel(tinyGroup, messageFrame, metaPdObservable));
                    messageFrame.setLocationRelativeTo(null);
                    messageFrame.pack();
                    messageFrame.setVisible(true);
                });
            });

            JButton manageMyGroupsMessageButton = new JButton("Manage");
            manageMyGroupsMessageButton.addActionListener(actionListener -> {
                TinyGroup tinyGroup = myGroupsList.getSelectedValue();
                if (tinyGroup == null) return;

                SwingUtilities.invokeLater(() -> {
                    JFrame messageFrame = new JFrame();

                    messageFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                    messageFrame.setTitle("Send Group Message");
                    messageFrame.setMinimumSize(new Dimension(500, 500));
                    messageFrame.setResizable(false);
                    messageFrame.add(new JGroupManagementPanel(tinyGroup, messageFrame, metaPdObservable));
                    messageFrame.setLocationRelativeTo(null);
                    messageFrame.pack();
                    messageFrame.setVisible(true);
                });
            });

            JButton leaveMyGroupsButton = new JButton("Leave");
            leaveMyGroupsButton.addActionListener(actionListener -> {
                TinyGroup tinyGroup = myGroupsList.getSelectedValue();
                if (tinyGroup == null) return;

                SwingUtilities.invokeLater(() -> {
                    if (metaPdObservable.makeCommand(20, "" + tinyGroup.id())) {
                        JOptionPane.showMessageDialog(mainJFrame,
                                "Group left successfully!",
                                "Success",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                    } else {
                        JOptionPane.showMessageDialog(mainJFrame,
                                "Unable to leave group!",
                                "Warning",
                                JOptionPane.WARNING_MESSAGE
                        );
                    }
                });
            });

            myGroupsButtonPanel.add(sendMyGroupsMessageButton);
            myGroupsButtonPanel.add(Box.createRigidArea(new Dimension(5, 0)));
            myGroupsButtonPanel.add(manageMyGroupsMessageButton);
            myGroupsButtonPanel.add(Box.createRigidArea(new Dimension(5, 0)));
            myGroupsButtonPanel.add(leaveMyGroupsButton);

            row2col2Panel.add(myGroupsLabel);
            row2col2Panel.add(searchMyGroupsTextField);
            row2col2Panel.add(Box.createRigidArea(new Dimension(0, 5)));
            row2col2Panel.add(myGroupsListScroller);
            row2col2Panel.add(Box.createRigidArea(new Dimension(0, 10)));
            row2col2Panel.add(myGroupsButtonPanel);


            //
            // Add to the main panel
            //

            gridBagConstraints.weightx = 1;
            gridBagConstraints.weighty = 1;
            gridBagConstraints.insets = new Insets(10, 10, 10, 10);
            gridBagConstraints.fill = GridBagConstraints.BOTH;

            // Define Columns Size
            gridBagConstraints.gridy = 1;
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridwidth = 1;
            add(Box.createRigidArea(new Dimension(300, 0)));

            gridBagConstraints.gridy = 1;
            gridBagConstraints.gridx = 1;
            add(Box.createRigidArea(new Dimension(680, 0)));

            gridBagConstraints.gridy = 1;
            gridBagConstraints.gridx = 2;
            add(Box.createRigidArea(new Dimension(300, 0)));

            // Add Other Panels
            gridBagConstraints.weighty = 0.05f;
            gridBagConstraints.gridy = 0;
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridwidth = 3;
            add(headerPanel, gridBagConstraints);

            gridBagConstraints.gridwidth = 1;
            gridBagConstraints.weighty = 1;

            gridBagConstraints.weightx = 0.2f;
            gridBagConstraints.gridy = 1;
            gridBagConstraints.gridx = 0;
            add(row1col0Panel, gridBagConstraints);

            gridBagConstraints.weightx = 0.6f;
            gridBagConstraints.gridy = 1;
            gridBagConstraints.gridx = 1;
            add(row1col1Panel, gridBagConstraints);

            gridBagConstraints.weightx = 0.2f;
            gridBagConstraints.gridy = 1;
            gridBagConstraints.gridx = 2;
            add(row1col2Panel, gridBagConstraints);

            gridBagConstraints.weightx = 0.2f;
            gridBagConstraints.gridy = 2;
            gridBagConstraints.gridx = 0;
            add(row2col0Panel, gridBagConstraints);

            gridBagConstraints.weightx = 0.6f;
            gridBagConstraints.gridy = 2;
            gridBagConstraints.gridx = 1;
            add(row2col1Panel, gridBagConstraints);

            gridBagConstraints.weightx = 0.2f;
            gridBagConstraints.gridy = 2;
            gridBagConstraints.gridx = 2;
            add(row2col2Panel, gridBagConstraints);
        }
    }

    private class AwaitsLoginPanel extends JPanel {
        private final MetaPdObservable metaPdObservable;

        private final JTextField usernameField = new JTextField();
        private final JPasswordField passwordField = new JPasswordField();

        public AwaitsLoginPanel(MetaPdObservable metaPdObservable) {
            this.metaPdObservable = metaPdObservable;
            metaPdObservable.addObserver(this);

            stylizeWindow();
        }

        @Override
        public void update(Graphics g) {
            if (metaPdObservable.getCurrentState() != AppState.AWAITS_LOGIN) return;

            cardLayout.show(jPanel, "AwaitsLogin");
            usernameField.setText("");
            passwordField.setText("");
        }

        private void stylizeWindow() {
            setBackground(DraculaTheme.BACKGROUND);
            setLayout(new GridBagLayout());

            JPanel verticalPanel = new JPanel();
            verticalPanel.setOpaque(false);
            verticalPanel.setLayout(new BoxLayout(verticalPanel, BoxLayout.Y_AXIS));

            JLabel usernameLabel = new JLabel("Your Username:");
            usernameLabel.setForeground(DraculaTheme.FOREGROUND);
            usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            // usernameField declared as a member of the class
            usernameField.setPreferredSize(new Dimension(200, 20));
            usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel passwordLabel = new JLabel("Your Password:");
            passwordLabel.setForeground(DraculaTheme.FOREGROUND);
            passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            // passwordField declared as a member of the class
            passwordField.setPreferredSize(new Dimension(200, 20));
            passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);

            JButton loginButton = new JButton("Login");
            loginButton.setPreferredSize(new Dimension(100, 20));
            loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            loginButton.setToolTipText("Login...");
            loginButton.addActionListener(
                    actionEvent -> metaPdObservable.login(
                            usernameField.getText(),
                            String.valueOf(passwordField.getPassword())
                    )
            );

            JButton signUpButton = new JButton("Sign Up");
            signUpButton.setPreferredSize(new Dimension(100, 20));
            signUpButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            loginButton.setToolTipText("Sign up...");
            signUpButton.addActionListener(
                    actionEvent -> metaPdObservable.goBack()
            );

            verticalPanel.add(usernameLabel);
            verticalPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            verticalPanel.add(usernameField);
            verticalPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            verticalPanel.add(passwordLabel);
            verticalPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            verticalPanel.add(passwordField);
            verticalPanel.add(Box.createRigidArea(new Dimension(0, 20)));
            verticalPanel.add(loginButton);
            verticalPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            verticalPanel.add(signUpButton);

            add(verticalPanel);
        }
    }

    private class AwaitsSignUpPanel extends JPanel {
        private final MetaPdObservable metaPdObservable;

        private final JTextField usernameField = new JTextField();
        private final JTextField nameField = new JTextField();
        private final JPasswordField passwordField = new JPasswordField();

        public AwaitsSignUpPanel(MetaPdObservable metaPdObservable) {
            this.metaPdObservable = metaPdObservable;
            metaPdObservable.addObserver(this);

            stylizeWindow();
        }

        @Override
        public void update(Graphics g) {
            if (metaPdObservable.getCurrentState() != AppState.AWAITS_SIGNUP) return;

            cardLayout.show(jPanel, "AwaitsSignUp");

            usernameField.setText("");
            nameField.setText("");
            passwordField.setText("");
        }

        private void stylizeWindow() {
            setBackground(DraculaTheme.BACKGROUND);
            setLayout(new GridBagLayout());

            JPanel verticalPanel = new JPanel();
            verticalPanel.setOpaque(false);
            verticalPanel.setLayout(new BoxLayout(verticalPanel, BoxLayout.Y_AXIS));

            JLabel usernameLabel = new JLabel("Your Username:");
            usernameLabel.setForeground(DraculaTheme.FOREGROUND);
            usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            // usernameField declared as a member of the class
            usernameField.setPreferredSize(new Dimension(200, 20));
            usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel nameLabel = new JLabel("Your Name:");
            nameLabel.setForeground(DraculaTheme.FOREGROUND);
            nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            // nameField declared as a member of the class
            nameField.setPreferredSize(new Dimension(200, 20));
            nameField.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel passwordLabel = new JLabel("Your Password:");
            passwordLabel.setForeground(DraculaTheme.FOREGROUND);
            passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            // nameField declared as a member of the class
            passwordField.setPreferredSize(new Dimension(200, 20));
            passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);

            JButton loginButton = new JButton("Login");
            loginButton.setPreferredSize(new Dimension(100, 20));
            loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            loginButton.setToolTipText("Login...");
            loginButton.addActionListener(
                    actionEvent -> metaPdObservable.goBack()
            );

            JButton signUpButton = new JButton("Sign Up");
            signUpButton.setPreferredSize(new Dimension(100, 20));
            signUpButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            loginButton.setToolTipText("Sign up...");
            signUpButton.addActionListener(
                    actionEvent -> metaPdObservable.signup(
                            usernameField.getText(),
                            nameField.getText(),
                            String.valueOf(passwordField.getPassword())
                    )
            );

            verticalPanel.add(usernameLabel);
            verticalPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            verticalPanel.add(usernameField);
            verticalPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            verticalPanel.add(nameLabel);
            verticalPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            verticalPanel.add(nameField);
            verticalPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            verticalPanel.add(passwordLabel);
            verticalPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            verticalPanel.add(passwordField);
            verticalPanel.add(Box.createRigidArea(new Dimension(0, 20)));
            verticalPanel.add(loginButton);
            verticalPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            verticalPanel.add(signUpButton);

            add(verticalPanel);
        }
    }
}
