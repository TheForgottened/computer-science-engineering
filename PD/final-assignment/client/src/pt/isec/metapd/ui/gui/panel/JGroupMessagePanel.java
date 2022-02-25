package pt.isec.metapd.ui.gui.panel;

import pt.isec.metapd.communication.TinyGroup;
import pt.isec.metapd.logic.MetaPdObservable;
import pt.isec.metapd.resources.DraculaTheme;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class JGroupMessagePanel extends JPanel {
    private final TinyGroup messageReceiver;
    private final JFrame parentFrame;
    private final MetaPdObservable metaPdObservable;

    private File selectedFile = null;

    public JGroupMessagePanel(TinyGroup messageReceiver, JFrame parentFrame, MetaPdObservable metaPdObservable) {
        this.messageReceiver = messageReceiver;
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

        JLabel messageTitleLabel = new JLabel("Message to group " + messageReceiver.name());
        messageTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        messageTitleLabel.setForeground(DraculaTheme.FOREGROUND);

        JTextArea messageTextArea = new JTextArea();
        messageTextArea.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

        JButton getFileButton = new JButton("Associate File");
        getFileButton.addActionListener(actionListener -> {
            JFileChooser jFileChooser = new JFileChooser();

            if (jFileChooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) return;

            selectedFile = jFileChooser.getSelectedFile();
        });

        JButton sendMessageButton = new JButton("Send Message");
        sendMessageButton.addActionListener(actionListener -> {
            if (messageTextArea.getText().isBlank()) return;

            if (selectedFile == null) {
                SwingUtilities.invokeLater(() -> {
                    if (metaPdObservable.makeCommand(
                            24,
                            "" + messageReceiver.id(),
                            messageTextArea.getText()
                    )) {
                        JOptionPane.showMessageDialog(parentFrame,
                                "Message sent successfully!",
                                "Success",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                    } else {
                        JOptionPane.showMessageDialog(parentFrame,
                                "Unable to send message!",
                                "Warning",
                                JOptionPane.WARNING_MESSAGE
                        );
                    }
                });
            } else {
                SwingUtilities.invokeLater(() -> {
                    if (metaPdObservable.makeCommand(
                            24,
                            "" + messageReceiver.id(),
                            messageTextArea.getText(),
                            selectedFile
                    )) {
                        JOptionPane.showMessageDialog(parentFrame,
                                "Message sent successfully!",
                                "Success",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                    } else {
                        JOptionPane.showMessageDialog(parentFrame,
                                "Unable to send message!",
                                "Warning",
                                JOptionPane.WARNING_MESSAGE
                        );
                    }
                });
            }
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(actionListener -> {
            parentFrame.setVisible(false);
            parentFrame.dispose();
        });

        buttonPanel.add(getFileButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(sendMessageButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(cancelButton);

        verticalPanel.add(messageTitleLabel);
        verticalPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        verticalPanel.add(messageTextArea);
        verticalPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        verticalPanel.add(buttonPanel);

        add(verticalPanel);
    }
}
