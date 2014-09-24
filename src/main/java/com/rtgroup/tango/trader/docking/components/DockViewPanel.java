package com.rtgroup.tango.trader.docking.components;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

//~--- classes ----------------------------------------------------------------

public class DockViewPanel extends JPanel {
    private final Component _sourceComponent;

    //~--- constructors -------------------------------------------------------

    public DockViewPanel(Component sourceComponent_) {
        super(new BorderLayout());
        setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        _sourceComponent = sourceComponent_;
        add(_sourceComponent, BorderLayout.CENTER);
    }

    //~--- get methods --------------------------------------------------------

    public Component getSourcePanel() {
        return _sourceComponent;
    }
}
