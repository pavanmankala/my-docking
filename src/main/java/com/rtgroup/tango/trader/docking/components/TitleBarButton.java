package com.rtgroup.tango.trader.docking.components;

import com.rtgroup.tango.trader.docking.components.IconsStore.IconVariation;

//~--- JDK imports ------------------------------------------------------------


import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ButtonModel;
import javax.swing.DefaultButtonModel;
import javax.swing.Icon;
import javax.swing.JComponent;

//~--- classes ----------------------------------------------------------------

public class TitleBarButton extends JComponent {
    private static final int  DIM              = 18;
    private static final long serialVersionUID = -7271921232967508999L;

    //~--- fields -------------------------------------------------------------

    private final ButtonModel _model = new DefaultButtonModel();
    private Icon              _icon;
    private IconVariation     _var;

    //~--- constructors -------------------------------------------------------

    public TitleBarButton() {
        super();
        setFocusable(false);

        Listener listener = new Listener();

        addMouseListener(listener);
        addMouseMotionListener(listener);
    }

    //~--- get methods --------------------------------------------------------

    public static TitleBarButton getButton(IconVariation var_) {
        TitleBarButton butt = new TitleBarButton();

        butt.setIconVariation(var_);

        return butt;
    }

    //~--- set methods --------------------------------------------------------

    public void setIconVariation(IconVariation var_) {
        _var = var_;
        setIcon(_var.getBaseIcon());
    }

    private void setIcon(Icon baseIcon) {
        _icon = baseIcon;
        repaint();
    }

    //~--- methods ------------------------------------------------------------

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) ((g == null)
                                       ? null
                                       : g.create());

        try {
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);

            // g2d.setColor(getParent().getBackground());
            // g2d.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

            // g2d.setColor(Color.gray.brighter());
            // g2d.fillRect(1, 1, getWidth() - 1, getHeight() - 1);

            int x = 1,
                y = 1;

            if (_model.isPressed()) {
                x++;
                y++;
            }

            _icon.paintIcon(this, g2d, x, y);
        } finally {
            g2d.dispose();
        }
    }

    //~--- get methods --------------------------------------------------------

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(DIM, DIM);
    }

    @Override
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    @Override
    public Dimension getMaximumSize() {
        return getPreferredSize();
    }

    //~--- inner classes ------------------------------------------------------

    class Listener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                setIcon(_var.getPressedIcon());
                _model.setPressed(true);
                repaint();
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if ((e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) != MouseEvent.BUTTON1_DOWN_MASK) {
                setIcon(_var.getRollOverIcon());
                _model.setRollover(true);
                repaint();
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if ((e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) != MouseEvent.BUTTON1_DOWN_MASK) {
                setIcon(_var.getBaseIcon());
                _model.setRollover(false);
                repaint();
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            setIcon(_var.getBaseIcon());
            _model.setPressed(false);
            repaint();

            if ((e.getButton() == MouseEvent.BUTTON1) && new Rectangle(getSize()).contains(e.getPoint())) {}
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            mouseEntered(e);
        }

        @Override
        public void mouseClicked(MouseEvent e) {}

        @Override
        public void mouseDragged(MouseEvent e) {}
    }
}
