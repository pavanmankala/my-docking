package com.rtgroup.tango.trader.docking.components;

import javax.swing.Action;

//~--- interfaces -------------------------------------------------------------

public interface DockableComponent {
    String getTitle();

    Action[] getActionList();
}
