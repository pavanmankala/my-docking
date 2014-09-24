package com.rtgroup.tango.trader.rts_docking;

import com.rtgroup.tango.trader.docking.components.TitleBar;

//~--- JDK imports ------------------------------------------------------------


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Area;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JWindow;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.EtchedBorder;

//~--- classes ----------------------------------------------------------------

public class SizableWindow {
    public static void main(String[] args) {
        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());

                    break;
                }
            }

            UIDefaults defaults = UIManager.getDefaults();

            defaults.put("ScrollBar.maximumThumbSize", new Dimension(10000, 10000));
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame();

        JFrame.setDefaultLookAndFeelDecorated(false);
        frame.setUndecorated(true);
        frame.setFocusable(false);
        frame.setFocusCycleRoot(false);
        frame.setFocusableWindowState(true);

        final JWindow window = new JWindow(frame) {
            @Override
            protected void finalize() throws Throwable {
                super.finalize();
                System.out.println("finalied");
            }
        };
        final Area[] intersectArea = new Area[9];


        window.getContentPane().add(new TitleBar("Hello world"), BorderLayout.PAGE_START);

        window.getContentPane().add(new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                new JScrollPane(new JTextArea("Hello world", 100, 100)),
                new JScrollPane(new JTextArea("Hello world", 100, 100))), BorderLayout.CENTER);
        window.getRootPane().setBorder(new EtchedBorder(EtchedBorder.RAISED));
        window.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Area intersection = new Area(new Rectangle(window.getSize()));

                intersection.subtract(new Area(new Rectangle(3, 20, window.getWidth() - 6, window.getHeight() - 6)));
                intersectArea[0] = intersection;

                Area nw = new Area(new Rectangle(0, 0, 15, 15));

                nw.intersect(intersection);
                intersectArea[1] = nw;
            }
        });
        window.setSize(1400, 300);
        frame.setVisible(true);
        window.setVisible(true);
    }

    //~--- inner classes ------------------------------------------------------

    static class ResizeComponent extends JComponent {}
}
