package kult.klient.gui.themes.kultklient.widgets;

import kult.klient.gui.themes.kultklient.KultKlientWidget;
import kult.klient.gui.widgets.WTopBar;
import kult.klient.utils.render.color.Color;

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
