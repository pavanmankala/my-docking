package com.rtgroup.tango.trader.docking.util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;

//~--- classes ----------------------------------------------------------------

public class DockUtils {
    private static final JComponent TEMP = new JLabel();

    //~--- get methods --------------------------------------------------------

    public static Icon getIcon(boolean isPressed, Icon baseIcon_) {
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

    public static Color[] getGradientBrightColors(Color baseColor, float step, float max) {
        List<Color> list    = new ArrayList<Color>();
        float       color[] = Color.RGBtoHSB(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), null);

        list.add(baseColor);

        for (; (color[2] += step) < (max); ) {
            list.add(Color.getHSBColor(color[0], color[1], color[2]));
        }

        return list.toArray(new Color[] {});
    }
}
