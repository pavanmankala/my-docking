package com.rtgroup.tango.trader.docking.components;

import com.rtgroup.tango.trader.docking.util.DockUtils;

//~--- JDK imports ------------------------------------------------------------

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.Timer;

//~--- classes ----------------------------------------------------------------

public class AnimatedLabel extends JLabel implements ActionListener {
    private int         index,
                        increment = 1;
    private Color       _baseColor;
    private final Timer animator;
    private Color[]     colors;

    //~--- constructors -------------------------------------------------------

    public AnimatedLabel(String text, Icon icon, int horizontalAlignment) {
        super(text, icon, horizontalAlignment);
        animator = new Timer(10, this);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                _baseColor = getForeground();
                index      = 0;
                increment  = 1;

                if (!"".equals(getText())) {
                    animator.start();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setForeground(_baseColor);
                animator.stop();
            }
        });
    }

    //~--- methods ------------------------------------------------------------

    @Override
    public void actionPerformed(ActionEvent e) {
        if (colors == null) {
            colors = DockUtils.getGradientBrightColors(_baseColor, 0.02f, 0.6f);
        }

        if (index >= colors.length) {
            increment = -1;
            index--;
        }

        if (index <= 0) {
            increment = 1;
            index++;
        }

        setForeground(colors[index]);
        index += increment;
    }
}
