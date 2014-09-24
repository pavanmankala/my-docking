package com.rtgroup.tango.trader.docking.components;

import com.rtgroup.tango.trader.docking.model.DockView;

//~--- JDK imports ------------------------------------------------------------

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.EtchedBorder;

//~--- classes ----------------------------------------------------------------

public class Separator extends JComponent {
    public static final int DIVIDER_SIZE = 2;
    static Color            COLOR1       = new Color(227, 237, 246),
                            COLOR2       = new Color(57, 105, 138);
    private static Color    BACKGROUND   = new Color(214, 217, 223);

    //~--- fields -------------------------------------------------------------

    private final Alignment _alignment;
    private DockView        _before, _after;

    //~--- constant enums -----------------------------------------------------

    public static enum Alignment { HORIZONTAL, VERTICAL }

    //~--- constructors -------------------------------------------------------

    public Separator(Alignment alignment_) {
        _alignment = alignment_;

        setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));

        Listener listener = new Listener();

        addMouseListener(listener);
        addMouseMotionListener(listener);
    }

    //~--- set methods --------------------------------------------------------

    public void setDockViews(DockView before_, DockView after_) {
        _before = before_;
        _after  = after_;
    }

    //~--- get methods --------------------------------------------------------

    @Override
    public Dimension getPreferredSize() {
        switch (_alignment) {
            case HORIZONTAL : {
                Dimension comp1Dim = _before.getPreferredSize();

                return new Dimension(comp1Dim.width, DIVIDER_SIZE * 2);
            }

            case VERTICAL : {
                Dimension comp1Dim = _before.getPreferredSize();

                return new Dimension(DIVIDER_SIZE * 2, comp1Dim.width);
            }
        }

        return null;
    }

    @Override
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    @Override
    public Dimension getMaximumSize() {
        return getPreferredSize();
    }

    //~--- methods ------------------------------------------------------------

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) ((g == null)
                                       ? null
                                       : g.create());

        try {
            GradientPaint gp = null;

            switch (_alignment) {
                case HORIZONTAL :
                    gp = new GradientPaint(new Point(0, 0), BACKGROUND, new Point(0, getHeight()),
                                           BACKGROUND.brighter());

                    break;

                case VERTICAL :
                    gp = new GradientPaint(new Point(0, 0), BACKGROUND, new Point(getWidth(), 0),
                                           BACKGROUND.brighter());

                    break;
            }

            g2d.setPaint(gp);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        } finally {
            g2d.dispose();
        }
    }

    public static void main(String[] args) {
        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());

                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final JComponent area1 = new JScrollPane(new JTextArea("Hello World 123", 300, 200));
        final JComponent area2 = new JScrollPane(new JTextArea("Hello World 12345", 300, 200));
        final Separator  sep   = new Separator(Alignment.HORIZONTAL);

        JPanel           panel = new JPanel(new LayoutManager() {
            @Override
            public void removeLayoutComponent(Component comp) {}

            @Override
            public Dimension preferredLayoutSize(Container parent) {
                return new Dimension(500, 300);
            }

            @Override
            public Dimension minimumLayoutSize(Container parent) {
                return new Dimension(250, 150);
            }

            @Override
            public void layoutContainer(Container parent) {
                Dimension dim      = parent.getSize();
                int       eachSize = (dim.height - (2 * DIVIDER_SIZE)) / 2;

                area1.setBounds(0, 0, dim.width, eachSize);
                sep.setBounds(0, eachSize, dim.width, (2 * DIVIDER_SIZE));
                area2.setBounds(0, eachSize + (2 * DIVIDER_SIZE), dim.width, eachSize);
            }

            @Override
            public void addLayoutComponent(String name, Component comp) {}
        });

        panel.add(area1);
        panel.add(sep);
        panel.add(area2);
        frame.setContentPane(panel);
        frame.pack();
        frame.setVisible(true);
    }

    //~--- inner classes ------------------------------------------------------

    class Listener extends MouseAdapter {
        int offset;

        //~--- methods --------------------------------------------------------

        @Override
        public void mousePressed(MouseEvent e) {
            offset = e.getY();
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            switch (_alignment) {
                case HORIZONTAL :
                    Separator.this.setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));

                    break;

                case VERTICAL :
                    Separator.this.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));

                    break;
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {}

        @Override
        public void mouseReleased(MouseEvent e) {
            if (!getBounds().contains(SwingUtilities.convertPoint(Separator.this, e.getPoint(), getParent()))) {
                Separator.this.setCursor(Cursor.getDefaultCursor());
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            switch (_alignment) {
                case HORIZONTAL : {
                    Point dividerLocation = getLocation();
                    Point mouseLocation   = SwingUtilities.convertPoint(Separator.this, e.getPoint(),
                                                Separator.this.getParent());

                    mouseLocation.y -= offset;

                    Dimension beforeDim    = _before.getMinimumSize(),
                              afterDim     = _after.getMinimumSize();
                    int       difference   = mouseLocation.y - dividerLocation.y;
                    Rectangle beforeBounds = _before.getBounds();
                    Rectangle afterBounds  = _after.getBounds();

                    beforeBounds.height += difference;
                    afterBounds.y       += difference;
                    afterBounds.height  -= difference;

                    if ((beforeBounds.height > beforeDim.height) && (afterBounds.height > afterDim.height)) {
                        _before.setBounds(beforeBounds);
                        _after.setBounds(afterBounds);
                        Separator.this.setBounds(beforeBounds.x, beforeBounds.height, beforeBounds.width,
                                                 2 * DIVIDER_SIZE);

                        _after.invalidate();
                        _before.invalidate();

                        _after.validate();
                        _before.validate();

                        _after.repaint();
                        _before.repaint();
                    }
                }

                case VERTICAL :
            }
        }
    }
}
