package com.rtgroup.tango.trader.docking.components;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;

import javax.swing.BorderFactory;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

//~--- classes ----------------------------------------------------------------

public class DockWindowLayerPane extends JLayeredPane {
    private static final Integer DOCK_PANEL_LAYER  = new Integer(1);
    private static final Integer TRANSLUCENT_LAYER = new Integer(2);

    //~--- fields -------------------------------------------------------------

    private final JPanel _docksPanel;

    //~--- constructors -------------------------------------------------------

    public DockWindowLayerPane(JPanel panel_) {
        _docksPanel = panel_;
        setLayout(new LayeredPaneLayout());
        add(_docksPanel, DOCK_PANEL_LAYER);
    }

    //~--- methods ------------------------------------------------------------

    public void addTranslucentLayer(Component comp_) {
        add(comp_, TRANSLUCENT_LAYER);
    }

    //~--- inner classes ------------------------------------------------------

    class LayeredPaneLayout implements LayoutManager {
        @Override
        public void addLayoutComponent(String name, Component comp) {}

        @Override
        public void removeLayoutComponent(Component comp) {}

        @Override
        public Dimension preferredLayoutSize(Container parent) {
            return _docksPanel.getPreferredSize();
        }

        @Override
        public Dimension minimumLayoutSize(Container parent) {
            return _docksPanel.getMinimumSize();
        }

        @Override
        public void layoutContainer(Container parent) {
            Insets    ins = parent.getInsets();
            Dimension dim = parent.getSize();

            _docksPanel.setBounds(0, 0, dim.width - ins.left - ins.left, dim.height - ins.top - ins.bottom);
        }
    }
}
