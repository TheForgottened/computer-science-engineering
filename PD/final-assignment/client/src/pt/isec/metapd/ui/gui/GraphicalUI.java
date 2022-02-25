package pt.isec.metapd.ui.gui;

import pt.isec.metapd.logic.MetaPdObservable;
import pt.isec.metapd.logic.MetaPdSm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GraphicalUI {
    private final MetaPdSm metaPdSM;

    public GraphicalUI(MetaPdSm metaPdSm) {
        this.metaPdSM = metaPdSm;
    }

    public void run() {
        MetaPdObservable metaPdObservable = new MetaPdObservable(metaPdSM);
        metaPdSM.setObservable(metaPdObservable);

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame();
            PanelManager panelManager = new PanelManager(metaPdObservable, frame);

            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    metaPdSM.setMustStop(true);
                }
            });
            frame.setTitle("MetaPD");
            frame.setMinimumSize(new Dimension(1280, 720));
            frame.setResizable(false);
            frame.add(panelManager.getPanel());
            frame.setLocationRelativeTo(null);
            frame.pack();
            frame.setVisible(true);
        });
    }
}
