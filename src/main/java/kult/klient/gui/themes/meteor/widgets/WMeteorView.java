package kult.klient.gui.themes.meteor.widgets;

import kult.klient.gui.renderer.GuiRenderer;
import kult.klient.gui.themes.meteor.MeteorWidget;
import kult.klient.gui.widgets.containers.WView;

public class WMeteorView extends WView implements MeteorWidget {
    @Override
    protected void onRender(GuiRenderer renderer, double mouseX, double mouseY, double delta) {
        if (canScroll && hasScrollBar) renderer.quad(handleX(), handleY(), handleWidth(), handleHeight(), theme().scrollbarColor.get(handlePressed, handleMouseOver));
    }
}
