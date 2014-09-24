package com.rtgroup.tango.trader.rts_docking;

import com.rtgroup.tango.trader.docking.controller.DockManager;
import com.rtgroup.tango.trader.docking.model.DockContainer;
import com.rtgroup.tango.trader.docking.model.DockContainer.Orientation;
import com.rtgroup.tango.trader.docking.model.DockView.ViewSide;

//~--- JDK imports ------------------------------------------------------------

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.BevelBorder;

//~--- classes ----------------------------------------------------------------

public class TestDockableLayer {
    static int                      counter               = 0;
    private final static JComponent _translucentContainer = new JComponent() {
        protected void paintComponent(Graphics g) {
            Rectangle  rect = getBounds();
            Graphics2D g2d  = (Graphics2D) g;

            g.setColor(FILL_COLOR);
            g.fillRect(0, 0, rect.width, rect.height);
            g2d.setColor(BORDER_COLOR);
            g2d.setStroke(new BasicStroke(3));
            g.drawRect(0, 0, rect.width, rect.height);
        }
        ;
    };
    private final static Color
        BORDER_COLOR = new Color(57, 105, 138, 126),
        FILL_COLOR   = new Color(161, 180, 195, 126);

    //~--- methods ------------------------------------------------------------

    public static void main(final String[] args) {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    main(args);
                }
            });

            return;
        }

        for (GraphicsDevice device : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) {
            for (GraphicsConfiguration gc : device.getConfigurations()) {
                System.out.println(gc.getBounds());
                System.out.println(Toolkit.getDefaultToolkit().getScreenInsets(gc));
            }
        }

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

        final DockManager manager   = new DockManager();

        DockContainer     container = manager.createDockContainer(Orientation.HORIONTAL);

        container.addDockViewAt(createPanel(), container, ViewSide.TOP);
        container.addDockViewAt(createPanel(), container, ViewSide.TOP);
        container.addDockViewAt(createPanel(), container, ViewSide.TOP);
        container.addDockViewAt(createPanel(), container, ViewSide.TOP);

        manager.display("hello", container);
    }

    static JPanel createPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        panel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        panel.add(new JLabel("HelloWorld " + ++counter), BorderLayout.PAGE_START);
        panel.add(new JScrollPane(new JTextArea(panel.toString(), 100, 200)), BorderLayout.CENTER);
        panel.setPreferredSize(new Dimension(200, 150));

        return panel;
    }
}
