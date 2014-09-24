package com.rtgroup.tango.trader.rts_docking;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.Icon;

//~--- classes ----------------------------------------------------------------

public class CloseIcon implements Icon {
    private static final int ICON_HEIGHT = 16;

    //~--- methods ------------------------------------------------------------

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2d = (Graphics2D) g;

        if (c instanceof AbstractButton) {
            ButtonModel m      = ((AbstractButton) c).getModel();
            int         d      = 5;
            Stroke      stroke = new BasicStroke(2.5f);

            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);

            Color         color1 = Color.RED.darker();
            Color         color2 = Color.ORANGE.darker();
            GradientPaint gp;

            if (m.isArmed() || m.isRollover()) {
                gp = new GradientPaint(x + 10, y, color2, x + 10, y + 20, color1);
            } else {
                gp = new GradientPaint(x + 10, y, color1, x + 10, y + 20, color2);
            }

            g2d.setPaint(gp);
            g2d.fillRect(x + 0, y + 0, ICON_HEIGHT, ICON_HEIGHT);
            g2d.setColor(Color.GRAY);
            g2d.drawRect(x + 0, y + 0, ICON_HEIGHT - 1, ICON_HEIGHT - 1);
            g2d.setStroke(stroke);

            if (m.isPressed()) {
                g2d.setColor(Color.BLACK);

                g2d.drawLine(x, y, x + ICON_HEIGHT, y);
                g2d.drawLine(x, y, x, y + ICON_HEIGHT);
                x += 1;
                y += 1;
            }

            x += 5;
            y += 5;
            g2d.setColor(Color.white);
            g2d.drawLine(x, y, x + d, y + d);
            g2d.drawLine(x, y + d, x + d, y);
        }
    }

    //~--- get methods --------------------------------------------------------

    @Override
    public int getIconWidth() {
        return ICON_HEIGHT;
    }

    @Override
    public int getIconHeight() {
        return ICON_HEIGHT;
    }
}
