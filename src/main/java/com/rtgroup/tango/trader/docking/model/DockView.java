package com.rtgroup.tango.trader.docking.model;

import com.rtgroup.tango.trader.docking.components.Separator;

//~--- JDK imports ------------------------------------------------------------


import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;

//~--- classes ----------------------------------------------------------------

public class DockView {
    protected boolean         _initialBoundsSet = false;
    protected final Component _dockComp;
    protected Separator       _separator;
    protected int             _x, _y, _width, _height;

    //~--- constant enums -----------------------------------------------------

    public enum ViewSide {
        TOP, BOTTOM, RIGHT, LEFT, CENTER,;
    }

    //~--- constructors -------------------------------------------------------

    public DockView(Component dockComp_) {
        _dockComp = dockComp_;
    }

    //~--- methods ------------------------------------------------------------

    public void doLayout() {}

    public void invalidate() {
        _dockComp.invalidate();
    }

    public void validate() {
        _dockComp.validate();
    }

    public void repaint() {
        _dockComp.repaint();
    }

    //~--- get methods --------------------------------------------------------

    public Rectangle getBounds() {
        return new Rectangle(_x, _y, _width, _height);
    }

    public Dimension getPreferredSize() {
        return _dockComp.getPreferredSize();
    }

    public Dimension getMinimumSize() {
        return _dockComp.getMinimumSize();
    }

    public Dimension getSize() {
        return new Dimension(_width, _height);
    }

    public boolean isContainer() {
        return false;
    }

    public Component getComponent() {
        return _dockComp;
    }

    public Separator getSeparator() {
        return _separator;
    }

    public boolean isInitialBoundsSet() {
        return _initialBoundsSet;
    }

    //~--- set methods --------------------------------------------------------

    public void setSeparator(Separator separator_) {
        this._separator = separator_;
    }

    public void setBounds(Rectangle bounds) {
        setBounds(bounds.x, bounds.y, bounds.width, bounds.height);
    }

    public void setBounds(int x, int y, int width, int height) {
        if (_initialBoundsSet) {}
        else {
            _initialBoundsSet = true;
        }

        _x      = x;
        _y      = y;
        _width  = width;
        _height = height;

        if (_dockComp != null) {
            _dockComp.setBounds(_x, _y, _width, _height);
        }
    }
}
