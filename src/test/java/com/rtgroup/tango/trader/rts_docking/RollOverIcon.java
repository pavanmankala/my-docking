package com.rtgroup.tango.trader.rts_docking;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

//~--- classes ----------------------------------------------------------------

public class RollOverIcon {
    private static final JLabel TEMP = new JLabel();

    //~--- get methods --------------------------------------------------------

    private static Icon getIcon(boolean isPressed, Icon baseIcon_) {
        BufferedImage img = new BufferedImage(baseIcon_.getIconWidth(), baseIcon_.getIconHeight(),
                                BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();

        baseIcon_.paintIcon(TEMP, g, 0, 0);
        g.dispose();

        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                Color rgba  = new Color(img.getRGB(i, j), true);
                int   alpha = rgba.getAlpha();

                if (isPressed) {
                    rgba = rgba.darker();
                } else {
                    rgba = rgba.brighter();
                }

                rgba = new Color(rgba.getRed(), rgba.getGreen(), rgba.getBlue(), alpha);

                img.setRGB(i, j, rgba.getRGB());
            }
        }

        return new ImageIcon(img);
    }

    //~--- methods ------------------------------------------------------------

    public static void main(String[] args) {
        ImageIcon icon =
            new ImageIcon(RollOverIcon.class.getResource("/com/rtgroup/tango/trader/rts_docking/cross-circle.png"));
        JFrame  frame = new JFrame();
        JButton butt  = new JButton();

        butt.setPressedIcon(getIcon(true, icon));
        butt.setRolloverIcon(getIcon(false, icon));
        butt.setIcon(icon);
        frame.add(butt);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
