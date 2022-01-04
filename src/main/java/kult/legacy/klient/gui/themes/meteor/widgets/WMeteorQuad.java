package kult.legacy.klient.gui.themes.meteor.widgets;

import kult.legacy.klient.gui.renderer.GuiRenderer;
import kult.legacy.klient.gui.widgets.WQuad;
import kult.legacy.klient.utils.render.color.Color;

public class WMeteorQuad extends WQuad {
    public WMeteorQuad(Color color) {
        super(color);
    }

    @Override
    protected void onRender(GuiRenderer renderer, double mouseX, double mouseY, double delta) {
        renderer.quadRounded(x, y, width, height, color, theme.roundAmount());
    }
}
