package kult.klient.gui.themes.meteor.widgets;

import kult.klient.gui.renderer.GuiRenderer;
import kult.klient.gui.themes.meteor.MeteorWidget;
import kult.klient.gui.widgets.WTooltip;

public class WMeteorTooltip extends WTooltip implements MeteorWidget {
    public WMeteorTooltip(String text) {
        super(text);
    }

    @Override
    protected void onRender(GuiRenderer renderer, double mouseX, double mouseY, double delta) {
        renderer.quad(this, theme().backgroundColor.get());
    }
}
