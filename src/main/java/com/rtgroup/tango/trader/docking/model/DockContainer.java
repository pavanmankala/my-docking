package com.rtgroup.tango.trader.docking.model;

import com.rtgroup.tango.trader.docking.components.Separator;
import com.rtgroup.tango.trader.docking.components.Separator.Alignment;

//~--- JDK imports ------------------------------------------------------------

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

//~--- classes ----------------------------------------------------------------

public class DockContainer extends DockView {
    private final List<DockView>  _dockViews  = new ArrayList<DockView>();
    private final List<Separator> _separators = new ArrayList<Separator>();
    private final Orientation     _orientation;

    //~--- constant enums -----------------------------------------------------

    public enum Orientation { HORIONTAL, VERTICAL,; }

    //~--- constructors -------------------------------------------------------

    public DockContainer(Orientation orientation_) {
        super(null);
        _orientation = orientation_;
    }

    //~--- methods ------------------------------------------------------------

    public void doLayout(Container parent_) {
        Insets    insets     = parent_.getInsets();
        Dimension parentSize = parent_.getSize();
        int       x          = insets.left;
        int       y          = insets.top;

        parentSize.width  -= (insets.left + insets.right);
        parentSize.height -= (insets.top + insets.bottom);
        performLayout(parent_, this, x, y, parentSize.width, parentSize.height);
    }

    static void performLayout(Container parent_, DockContainer container, int x, int y, int width_, int height_) {
        List<DockView>  dockViews   = container._dockViews;
        List<Separator> separators  = container._separators;
        float           h_ratio[]   = new float[dockViews.size()];
        float           w_ratio[]   = new float[dockViews.size()];
        int             widths[]    = new int[dockViews.size()];
        int             heights[]   = new int[dockViews.size()];
        int             totalWidth  = 0, remWidth;
        int             totalHeight = 0, remHeight;


        switch (container._orientation) {
            case HORIONTAL :
                for (int i = 0; i < dockViews.size(); i++) {
                    DockView view = dockViews.get(i);

                    if (view.isInitialBoundsSet()) {
                        widths[i]   = view._width;
                        heights[i]  = view._height;
                        totalHeight += heights[i];
                    } else {
                        Dimension size = view.getPreferredSize();

                        widths[i]  = size.width;
                        heights[i] = size.height;

                        if (i == 0) {
                            heights[i] -= Separator.DIVIDER_SIZE * 2;
                        } else if (i == (dockViews.size() - 1)) {}
                        else {
                            heights[i] -= Separator.DIVIDER_SIZE * 2;
                        }

                        totalHeight += heights[i];
                    }
                }

                height_    -= (dockViews.size() - 1) * Separator.DIVIDER_SIZE * 2;

                totalWidth = widths[0];

                break;

            case VERTICAL :
                for (int i = 0; i < dockViews.size(); i++) {
                    DockView view = dockViews.get(i);

                    if (view.isInitialBoundsSet()) {
                        widths[i]  = view._width;
                        heights[i] = view._height;
                        totalWidth += widths[i];
                    } else {
                        Dimension size = view.getPreferredSize();

                        widths[i]  = size.width;
                        heights[i] = size.height;

                        if (i == 0) {
                            widths[i] -= Separator.DIVIDER_SIZE * 2;
                        } else if (i == (dockViews.size() - 1)) {}
                        else {
                            widths[i] -= Separator.DIVIDER_SIZE * 2;
                        }

                        totalWidth += widths[i];
                    }
                }

                width_      -= (dockViews.size() - 1) * Separator.DIVIDER_SIZE * 2;

                totalHeight = heights[0];

                break;
        }

        boolean hasSeps = h_ratio.length > 1;

        remWidth  = width_;
        remHeight = height_;

        for (int i = 0; i < h_ratio.length; i++) {
            h_ratio[i] = (heights[i] * 1.0f) / totalHeight;
            w_ratio[i] = (widths[i] * 1.0f) / totalWidth;
        }

        for (int i = 0; i < h_ratio.length; i++) {
            DockView  view = dockViews.get(i);
            Separator sep  = separators.get(i);

            if (hasSeps) {
                switch (container._orientation) {
                    case HORIONTAL : {
                        if (i == (h_ratio.length - 1)) {
                            heights[i] = remHeight;
                        } else {
                            heights[i] = (int) (h_ratio[i] * height_);
                            remHeight  -= heights[i];
                        }

                        if (i == 0) {
                            view.setBounds(x, y, width_, heights[i]);
                            y += view._height;
                            sep.setBounds(x, y, width_, Separator.DIVIDER_SIZE * 2);
                            y += Separator.DIVIDER_SIZE * 2;
                            parent_.add(sep);
                        } else if (i == (h_ratio.length - 1)) {
                            view.setBounds(x, y, width_, heights[i]);
                            y += view._height;
                        } else {
                            view.setBounds(x, y, width_, heights[i]);
                            y += view._height;
                            sep.setBounds(x, y, width_, Separator.DIVIDER_SIZE * 2);
                            y += (Separator.DIVIDER_SIZE * 2);
                            parent_.add(sep);
                        }

                        break;
                    }

                    case VERTICAL : {
                        if (i == (h_ratio.length - 1)) {
                            widths[i] = remWidth;
                        } else {
                            widths[i] = (int) (w_ratio[i] * width_);
                            remWidth  -= widths[i];
                        }

                        if (i == 0) {
                            view.setBounds(x, y, widths[i], height_);
                            x += view._width;
                            sep.setBounds(x, y, Separator.DIVIDER_SIZE * 2, height_);
                            x += Separator.DIVIDER_SIZE * 2;
                            parent_.add(sep);
                        } else if (i == (h_ratio.length - 1)) {
                            view.setBounds(x, y, widths[i], height_);
                            x += view._width;
                        } else {
                            view.setBounds(x, y, widths[i], height_);
                            x += view._width;
                            sep.setBounds(x, y, Separator.DIVIDER_SIZE * 2, height_);
                            x += (Separator.DIVIDER_SIZE * 2);
                            parent_.add(sep);
                        }

                        break;
                    }
                }
            }
        }

        for (DockView dv : dockViews) {
            if (dv.isContainer()) {
                performLayout(parent_, (DockContainer) dv, dv._x, dv._y, dv._width, dv._height);
            }
        }
    }

    private void calculateSize(Dimension d_, SizeCalculation calculation_) {
        boolean isFirst = true;

        switch (_orientation) {
            case HORIONTAL :
                for (DockView dv : _dockViews) {
                    if (dv.isContainer()) {
                        calculateSize(d_, calculation_);
                    } else {
                        Dimension dc = calculation_.getSize(dv.getComponent());

                        d_.height += dc.height;

                        if (isFirst) {
                            d_.width += dc.width;
                            isFirst  = false;
                        }
                    }
                }

                d_.height += (_dockViews.size() - 1) * 2 * Separator.DIVIDER_SIZE;

                break;

            case VERTICAL :
                for (DockView dv : _dockViews) {
                    if (dv.isContainer()) {
                        calculateSize(d_, calculation_);
                    } else {
                        Dimension dc = calculation_.getSize(dv.getComponent());

                        d_.width += dc.width;

                        if (isFirst) {
                            d_.height += dc.height;
                            isFirst   = false;
                        }
                    }
                }

                d_.width += (_dockViews.size() - 1) * 2 * Separator.DIVIDER_SIZE;

                break;
        }
    }

    //~--- get methods --------------------------------------------------------

    public Collection<DockView> getDockViews() {
        return Collections.unmodifiableCollection(_dockViews);
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension d = new Dimension();

        calculateSize(d, new SizeCalculation() {
            @Override
            public Dimension getSize(Component comp) {
                return comp.getPreferredSize();
            }
        });

        return d;
    }

    @Override
    public Dimension getMinimumSize() {
        Dimension d = new Dimension();

        calculateSize(d, new SizeCalculation() {
            @Override
            public Dimension getSize(Component comp) {
                return comp.getMinimumSize();
            }
        });

        return d;
    }

    @Override
    public Dimension getSize() {
        Dimension d = new Dimension();

        calculateSize(d, new SizeCalculation() {
            @Override
            public Dimension getSize(Component comp) {
                return comp.getSize();
            }
        });

        return d;
    }

    public Orientation getOrientation() {
        return _orientation;
    }

    @Override
    public boolean isContainer() {
        return true;
    }

    //~--- methods ------------------------------------------------------------

    public void removeDockView(DockView viewTobeRemoved_) {
        DockContainer dockViewContainer = getDockContainer(viewTobeRemoved_);

        if (dockViewContainer != null) {
            List<DockView> dockViews = dockViewContainer._dockViews;
            int            index     = dockViews.indexOf(viewTobeRemoved_);
            int            size      = dockViews.size();
            Component      dockComp  = viewTobeRemoved_.getComponent();

            if (index == -1) {
                throw new RuntimeException("Tried to remove dockview from container in which it doesnot exists");
            }

            if (size == 2) {
                DockContainer parentContainer = getDockContainer(dockViewContainer);

                if (parentContainer != null) {
                    parentContainer._dockViews.remove(dockViewContainer);

                    DockView newDockView = new DockView(dockComp);

                    newDockView.setBounds(dockViewContainer.getBounds());
                    parentContainer._dockViews.add(newDockView);
                    parentContainer.doLayout();
                }
            } else if (size < 2) {
                throw new RuntimeException("Someting happened");
            } else {
                dockViews.remove(index);
                dockViewContainer.doLayout();
            }
        }
    }

    public DockView addDockViewAt(Component compTobeAdded_, DockView toView_, ViewSide toViewSide_) {
        DockContainer addToContainer;

        if (toView_.isContainer()) {
            addToContainer = (DockContainer) toView_;

            switch (addToContainer._orientation) {
                case HORIONTAL : {
                    switch (toViewSide_) {
                        case CENTER :
                        case LEFT :
                        case RIGHT :
                            throw new RuntimeException("invalid position in container");

                        default :
                            break;
                    }

                    break;
                }

                case VERTICAL : {
                    switch (toViewSide_) {
                        case CENTER :
                        case TOP :
                        case BOTTOM :
                            throw new RuntimeException("invalid position in container");

                        default :
                            break;
                    }

                    break;
                }
            }
        } else {
            addToContainer = getDockContainer(toView_);
        }

        Orientation     containerOrientation = addToContainer._orientation;
        List<DockView>  parentDockViews      = addToContainer._dockViews;
        List<Separator> separators           = addToContainer._separators;
        int             index                = parentDockViews.indexOf(toView_);

        switch (containerOrientation) {
            case HORIONTAL : {
                switch (toViewSide_) {
                    case TOP : {
                        index = (index < 0)
                                ? 0
                                : index;

                        index = (index > 0)
                                ? index - 1
                                : index;
                        parentDockViews.add(index, new DockView(compTobeAdded_));
                        separators.add(new Separator(Alignment.HORIZONTAL));

                        break;
                    }

                    case BOTTOM : {
                        index = (index < 0)
                                ? parentDockViews.size()
                                : index;

                        index = (index < parentDockViews.size())
                                ? index + 1
                                : index;
                        parentDockViews.add(index, new DockView(compTobeAdded_));
                        separators.add(new Separator(Alignment.HORIZONTAL));

                        break;
                    }

                    case LEFT : {
                        DockContainer newContainer = new DockContainer(Orientation.VERTICAL);

                        newContainer._dockViews.add(parentDockViews.remove(index));
                        newContainer._dockViews.add(new DockView(compTobeAdded_));
                        newContainer._separators.add(new Separator(Alignment.VERTICAL));
                        parentDockViews.add(index, newContainer);

                        break;
                    }

                    case RIGHT : {
                        DockContainer newContainer = new DockContainer(Orientation.VERTICAL);

                        newContainer._dockViews.add(new DockView(compTobeAdded_));
                        newContainer._dockViews.add(parentDockViews.remove(index));
                        newContainer._separators.add(new Separator(Alignment.VERTICAL));
                        parentDockViews.add(index, newContainer);

                        break;
                    }

                    case CENTER :
                }

                break;
            }

            case VERTICAL : {
                switch (toViewSide_) {
                    case LEFT : {
                        index = (index < 0)
                                ? 0
                                : index;
                        index = (index > 0)
                                ? index - 1
                                : index;
                        parentDockViews.add(index, new DockView(compTobeAdded_));
                        separators.add(new Separator(Alignment.VERTICAL));

                        break;
                    }

                    case RIGHT : {
                        index = (index < 0)
                                ? parentDockViews.size()
                                : index;
                        index = (index < parentDockViews.size())
                                ? index + 1
                                : index;
                        parentDockViews.add(index, new DockView(compTobeAdded_));
                        separators.add(new Separator(Alignment.VERTICAL));

                        break;
                    }

                    case BOTTOM : {
                        DockContainer newContainer = new DockContainer(Orientation.HORIONTAL);

                        newContainer._dockViews.add(parentDockViews.remove(index));
                        newContainer._dockViews.add(new DockView(compTobeAdded_));
                        newContainer._separators.add(new Separator(Alignment.HORIZONTAL));
                        parentDockViews.add(index, newContainer);

                        break;
                    }

                    case TOP : {
                        DockContainer newContainer = new DockContainer(Orientation.HORIONTAL);

                        newContainer._dockViews.add(new DockView(compTobeAdded_));
                        newContainer._dockViews.add(parentDockViews.remove(index));
                        newContainer._separators.add(new Separator(Alignment.HORIZONTAL));
                        parentDockViews.add(index, newContainer);

                        break;
                    }

                    case CENTER :
                }

                break;
            }
        }

        for (int i = 0; i < parentDockViews.size() - 1; i++) {}

        return parentDockViews.get(index);
    }

    //~--- get methods --------------------------------------------------------

    public DockContainer getDockContainer(DockView view_) {
        for (DockView view : _dockViews) {
            if (view_ == view) {
                return this;
            } else if (view.isContainer()) {
                DockContainer container;

                if ((container = ((DockContainer) view).getDockContainer(view_)) != null) {
                    return container;
                }
            }
        }

        return null;
    }

    //~--- inner interfaces ---------------------------------------------------

    interface SizeCalculation {
        Dimension getSize(Component comp);
    }
}
