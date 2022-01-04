package kult.legacy.klient.gui.themes.kultklient.widgets;

import kult.legacy.klient.gui.renderer.GuiRenderer;
import kult.legacy.klient.gui.themes.kultklient.KultKlientWidget;
import kult.legacy.klient.gui.widgets.containers.WView;

public class WKultKlientView extends WView implements KultKlientWidget {
    @Override
    protected void onRender(GuiRenderer renderer, double mouseX, double mouseY, double delta) {
        if (canScroll && hasScrollBar) renderer.quad(handleX(), handleY(), handleWidth(), handleHeight(), theme().scrollbarColor.get(handlePressed, handleMouseOver));
    }
}
