package com.rtgroup.tango.trader.docking.util;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.SwingUtilities;

//~--- classes ----------------------------------------------------------------

public class PositioningUtils {
    public static Rectangle getCentralLocation(Component comp) {
        return getCentralLocation(null, comp, comp.getPreferredSize());
    }

    public static Rectangle getCentralLocation(Component sourceComp, Component comp, Dimension size) {
        final Rectangle position;

        if (sourceComp != null) {
            Point compLocation = new Point(0, 0);

            SwingUtilities.convertPointToScreen(compLocation, sourceComp);

            GraphicsEnvironment e             = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice[]    devices       = e.getScreenDevices();
            Rectangle           displayBounds = null;

            // now get the configurations for each device
            for (GraphicsDevice device : devices) {
                GraphicsConfiguration[] configurations = device.getConfigurations();

                for (GraphicsConfiguration config : configurations) {
                    Rectangle gcBounds = config.getBounds();

                    if (gcBounds.contains(compLocation)) {
                        displayBounds = gcBounds;
                    }
                }
            }

            if (displayBounds == null) {
                position = getLocationFromMouse(size);
            } else {
                int x = (displayBounds.width - size.width) / 2;
                int y = (displayBounds.height - size.height) / 2;

                position = new Rectangle(x, y, size.width, size.height);
            }
        } else {
            position = getLocationFromMouse(size);
        }

        return position;

    }

    public static Rectangle getLocationFromMouse(Dimension panelPrefSize_) {
        GraphicsDevice device     = MouseInfo.getPointerInfo().getDevice();
        Dimension      screensize = new Dimension(device.getDisplayMode().getWidth(),
                                        device.getDisplayMode().getHeight());
        int            x          = (screensize.width - panelPrefSize_.width) / 2;
        int            y          = (screensize.height - panelPrefSize_.height) / 2;

        return new Rectangle(x, y, panelPrefSize_.width, panelPrefSize_.height);
    }
}
