package kult.klient.gui.themes.meteor.widgets;

import kult.klient.gui.themes.meteor.MeteorWidget;
import kult.klient.gui.widgets.WTopBar;
import kult.klient.utils.render.color.Color;

public class WMeteorTopBar extends WTopBar implements MeteorWidget {
    @Override
    protected Color getButtonColor(boolean pressed, boolean hovered) {
        return theme().backgroundColor.get(pressed, hovered);
    }

    @Override
    protected Color getNameColor() {
        return theme().textColor.get();
    }
}
