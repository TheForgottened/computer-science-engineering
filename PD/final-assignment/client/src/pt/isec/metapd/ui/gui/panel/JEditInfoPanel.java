package pt.isec.metapd.ui.gui.panel;

import pt.isec.metapd.logic.MetaPdObservable;
import pt.isec.metapd.resources.DraculaTheme;

import javax.swing.*;
import java.awt.*;

public class JEditInfoPanel extends JPanel {
    private final JFrame parentFrame;
    private final MetaPdObservable metaPdObservable;

    public JEditInfoPanel(JFrame parentFrame, MetaPdObservable metaPdObservable) {
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
            if (metaPdObservable.makeCommand(0, nameTextField.getText().trim())) {
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

        JLabel passwordLabel = new JLabel("New Password");
        passwordLabel.setForeground(DraculaTheme.FOREGROUND);
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(200, 20));
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton updatePasswordButton = new JButton("Update Password");
        updatePasswordButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        updatePasswordButton.addActionListener(actionListener -> SwingUtilities.invokeLater(() -> {
            if (metaPdObservable.makeCommand(1, String.valueOf(passwordField.getPassword()))) {
                JOptionPane.showMessageDialog(parentFrame,
                        "Password changed successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

                parentFrame.setVisible(false);
                parentFrame.dispose();

                metaPdObservable.goBack();
            } else {
                JOptionPane.showMessageDialog(parentFrame,
                        "Unable to change password!",
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
        verticalPanel.add(passwordLabel);
        verticalPanel.add(passwordField);
        verticalPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        verticalPanel.add(updatePasswordButton);

        add(verticalPanel);
    }
}
