package com.rtgroup.tango.trader.docking.components;

import com.rtgroup.tango.trader.docking.model.DockContainer;

//~--- JDK imports ------------------------------------------------------------

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;

//~--- classes ----------------------------------------------------------------

public class DockLayout implements LayoutManager {
    private final DockContainer _dockContainer;

    //~--- constructors -------------------------------------------------------

    public DockLayout(DockContainer dc_) {
        _dockContainer = dc_;
    }

    //~--- methods ------------------------------------------------------------

    @Override
    public void addLayoutComponent(String name, Component comp) {}

    @Override
    public void removeLayoutComponent(Component comp) {}

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        return _dockContainer.getPreferredSize();
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return _dockContainer.getMinimumSize();
    }

    @Override
    public void layoutContainer(Container parent) {
        _dockContainer.doLayout(parent);
    }
}
