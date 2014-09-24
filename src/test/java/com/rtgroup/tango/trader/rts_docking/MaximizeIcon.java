package com.rtgroup.tango.trader.rts_docking;

import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Area;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.Icon;

//~--- classes ----------------------------------------------------------------

public class MaximizeIcon implements Icon {
    private static final int ICON_HEIGHT = 16;

    //~--- fields -------------------------------------------------------------

    private final Area area;

    //~--- constructors -------------------------------------------------------

    public MaximizeIcon() {
        area = new Area(new Rectangle(0, 0, 10, 10));
        area.exclusiveOr(new Area(new Rectangle(3, 3, 4, 4)));
    }

    //~--- methods ------------------------------------------------------------

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2d = (Graphics2D) g;

        if (c instanceof AbstractButton) {
            ButtonModel m = ((AbstractButton) c).getModel();

            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);

            Color         color1 = Color.GRAY;
            Color         color2 = Color.WHITE;
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

            if (m.isPressed()) {
                g2d.drawLine(x, y, x + ICON_HEIGHT, y);
                g2d.drawLine(x, y, x, y + ICON_HEIGHT);
                g2d.translate(x + 3, y + 3);
            } else {
                g2d.translate(x + 2, y + 2);
            }

            g2d.setColor(Color.white);
            g2d.fill(area);
            g2d.setColor(Color.gray);
            g2d.draw(area);
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
