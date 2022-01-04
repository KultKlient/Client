package kult.legacy.klient.gui.themes.kultklient.widgets;

import kult.legacy.klient.gui.themes.kultklient.KultKlientWidget;
import kult.legacy.klient.gui.widgets.WTopBar;
import kult.legacy.klient.utils.render.color.Color;

public class WKultKlientTopBar extends WTopBar implements KultKlientWidget {
    @Override
    protected Color getButtonColor(boolean pressed, boolean hovered) {
        return theme().backgroundColor.get(pressed, hovered);
    }

    @Override
    protected Color getNameColor() {
        return theme().textColor.get();
    }
}
