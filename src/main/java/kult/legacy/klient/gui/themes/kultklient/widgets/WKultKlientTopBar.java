package kultklient.legacy.client.gui.themes.kultklient.widgets;

import kultklient.legacy.client.gui.themes.kultklient.KultKlientWidget;
import kultklient.legacy.client.gui.widgets.WTopBar;
import kultklient.legacy.client.utils.render.color.Color;

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
