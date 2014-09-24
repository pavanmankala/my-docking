package com.rtgroup.tango.trader.docking.controller;

import com.rtgroup.tango.trader.docking.components.DockLayout;
import com.rtgroup.tango.trader.docking.components.DockViewPanel;
import com.rtgroup.tango.trader.docking.components.DockWindowLayerPane;
import com.rtgroup.tango.trader.docking.components.TitleBar;
import com.rtgroup.tango.trader.docking.model.DockContainer;
import com.rtgroup.tango.trader.docking.model.DockContainer.Orientation;
import com.rtgroup.tango.trader.docking.model.DockView;

//~--- JDK imports ------------------------------------------------------------

import java.awt.AWTEvent;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.KeyboardFocusManager;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.AWTEventListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;

//~--- classes ----------------------------------------------------------------

public class DockManager implements AWTEventListener {
    private final static Color
        BORDER_COLOR                            = new Color(57, 105, 138, 126),
        FILL_COLOR                              = new Color(161, 180, 195, 126);
    private static final long EVENT_FILTER_MASK = MouseEvent.SHIFT_DOWN_MASK | MouseEvent.ALT_DOWN_MASK
                                                  | MouseEvent.CTRL_DOWN_MASK;
    private static final long   MONITOR_EVENT_MASK = AWTEvent.KEY_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK;
    private static final Stroke TRANSLUCENT_STROKE = new BasicStroke(3);

    //~--- static initializers ------------------------------------------------

    static {
        JFrame.setDefaultLookAndFeelDecorated(false);
    }

    //~--- fields -------------------------------------------------------------

    private final JComponent TRANSLUCENT_SOURCE = new JComponent() {
        protected void paintComponent(java.awt.Graphics g) {
            Graphics2D g2d = (Graphics2D) ((g == null)
                                           ? null
                                           : g.create());

            try {
                paintTranslucency(this, g2d);
            } finally {
                g2d.dispose();
            }
        }
    };
    private final JComponent TRANSLUCENT_DESTINATION = new JComponent() {
        protected void paintComponent(java.awt.Graphics g) {
            Graphics2D g2d = (Graphics2D) ((g == null)
                                           ? null
                                           : g.create());

            try {
                paintTranslucency(this, g2d);
            } finally {
                g2d.dispose();
            }
        }
    };
    private final Map<String, JWindow> _windows = new HashMap<String, JWindow>();
    private final JFrame               _rootWindow;

    //~--- constructors -------------------------------------------------------

    public DockManager() {
        Toolkit.getDefaultToolkit().addAWTEventListener(this, MONITOR_EVENT_MASK);
        TRANSLUCENT_DESTINATION.setVisible(false);

        _rootWindow = new JFrame();
        _rootWindow.setUndecorated(true);
        _rootWindow.setVisible(true);
    }

    //~--- methods ------------------------------------------------------------

    @Override
    public void eventDispatched(AWTEvent event) {
        if (event instanceof InputEvent) {
            InputEvent e = (InputEvent) event;

            if ((((InputEvent) event).getModifiersEx() & EVENT_FILTER_MASK) == EVENT_FILTER_MASK) {
                Component           source = (Component) e.getSource();
                Window              w      = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusedWindow();
                DockWindowLayerPane layer  = getDockWindowLayer(source);
                DockViewPanel       dockablePanel;
                Component           deepComp;

                if (event instanceof MouseEvent) {
                    MouseEvent me = (MouseEvent) e;

                    deepComp = SwingUtilities.getDeepestComponentAt((Component) source, me.getX(), me.getY());
                } else if (event instanceof KeyEvent) {
                    Point location = MouseInfo.getPointerInfo().getLocation();

                    SwingUtilities.convertPointFromScreen(location, w);
                    deepComp = SwingUtilities.getDeepestComponentAt(w, location.x, location.y);
                } else {
                    return;
                }

                dockablePanel = getDockViewPanel(deepComp);

                if (dockablePanel != null) {
                    TRANSLUCENT_SOURCE.setVisible(true);
                    TRANSLUCENT_SOURCE.setBounds(dockablePanel.getBounds());
                    TRANSLUCENT_SOURCE.repaint();
                    layer.repaint();
                } else {
                    TRANSLUCENT_SOURCE.setVisible(false);
                }
            } else {
                TRANSLUCENT_SOURCE.setVisible(false);
            }
        }
    }

    private void paintTranslucency(JComponent comp, Graphics2D g2d) {
        Rectangle rect = comp.getBounds();

        g2d.setColor(FILL_COLOR);
        g2d.fillRect(0, 0, rect.width, rect.height);
        g2d.setColor(BORDER_COLOR);
        g2d.setStroke(TRANSLUCENT_STROKE);
        g2d.drawRect(0, 0, rect.width, rect.height);
    }

    public void display(String title_, DockContainer viewContainer_) {
        JWindow             window      = new JWindow(_rootWindow);
        JPanel              panel       = new JPanel(new DockLayout(viewContainer_));
        DockWindowLayerPane layeredPane = new DockWindowLayerPane(panel);
        JComponent          contentPane = (JComponent) window.getContentPane();
        List<Component>     comps       = getComponents(new ArrayList<Component>(), viewContainer_);

        contentPane.add(new TitleBar(title_), BorderLayout.PAGE_START);
        contentPane.add(layeredPane, BorderLayout.CENTER);

        for (Component comp : comps) {
            panel.add(new DockViewPanel(comp));
        }

        contentPane.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        window.pack();
        window.setVisible(true);
        _windows.put(title_, window);
    }

    public DockContainer createDockContainer(Orientation orientation_) {
        return new DockContainer(orientation_);
    }

    //~--- get methods --------------------------------------------------------

    public List<Component> getComponents(List<Component> list_, DockContainer dc_) {
        for (DockView v : dc_.getDockViews()) {
            if (v.isContainer()) {
                getComponents(list_, (DockContainer) v);
            } else {
                list_.add(v.getComponent());
            }
        }

        return list_;
    }

    private Window getRootWindow(Component source) {
        if (source instanceof Window) {
            return (Window) source;
        } else if (source instanceof Component) {
            return SwingUtilities.getWindowAncestor(source);
        } else {
            return null;
        }
    }

    private DockWindowLayerPane getDockWindowLayer(Component source) {
        Window w = getRootWindow(source);

        if (w instanceof JWindow) {
            for (Component comp : ((JWindow) w).getContentPane().getComponents()) {
                if (comp instanceof DockWindowLayerPane) {
                    return (DockWindowLayerPane) comp;
                }
            }
        } else if (w instanceof JFrame) {
            for (Component comp : ((JFrame) w).getContentPane().getComponents()) {
                if (comp instanceof DockWindowLayerPane) {
                    return (DockWindowLayerPane) comp;
                }
            }
        }

        return null;
    }

    private DockViewPanel getDockViewPanel(Component c) {
        if (c == null) {
            return null;
        }

        if (c.getParent() instanceof DockViewPanel) {
            return (DockViewPanel) c.getParent();
        } else {
            return getDockViewPanel(c.getParent());
        }
    }
}
