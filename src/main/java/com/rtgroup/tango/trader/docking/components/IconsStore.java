package com.rtgroup.tango.trader.docking.components;

import com.rtgroup.tango.trader.docking.util.DockUtils;

//~--- JDK imports ------------------------------------------------------------

import javax.swing.Icon;
import javax.swing.ImageIcon;

//~--- interfaces -------------------------------------------------------------

public interface IconsStore {
    Icon CLOSE_ICON       = new ImageIcon(IconsStore.class.getResource("/images/close.png")),
         DOCUMENT_ICON    = new ImageIcon(IconsStore.class.getResource("/images/document.png")),
         EXTERNALIZE_ICON = new ImageIcon(IconsStore.class.getResource("/images/externalize.png")),
         INTERNALIZE_ICON = new ImageIcon(IconsStore.class.getResource("/images/internalize.png")),
         MAXIMIZE_ICON    = new ImageIcon(IconsStore.class.getResource("/images/maximize.png")),
         MINIMIZE_ICON    = new ImageIcon(IconsStore.class.getResource("/images/minimize.png")),
         RESTORE_ICON     = new ImageIcon(IconsStore.class.getResource("/images/restore.png"));

    //~--- enums --------------------------------------------------------------

    public enum IconVariation {
        CLOSE(CLOSE_ICON), DOCUMENT(DOCUMENT_ICON), EXTERNALIZE(EXTERNALIZE_ICON), INTERNALIZE(INTERNALIZE_ICON),
        MAXIMIZE(MAXIMIZE_ICON), MINIMIZE(MINIMIZE_ICON), RESTORE(RESTORE_ICON),;

        private final Icon _baseIcon, _rollOverIcon, _pressedIcon;

        //~--- constructors ---------------------------------------------------

        private IconVariation(Icon baseIcon_) {
            _baseIcon     = baseIcon_;
            _rollOverIcon = DockUtils.getIcon(false, _baseIcon);
            _pressedIcon  = DockUtils.getIcon(true, _baseIcon);
        }

        //~--- get methods ----------------------------------------------------

        public Icon getPressedIcon() {
            return _pressedIcon;
        }

        public Icon getBaseIcon() {
            return _baseIcon;
        }

        public Icon getRollOverIcon() {
            return _rollOverIcon;
        }
    }
}
