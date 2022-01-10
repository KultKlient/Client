package kult.klient.gui.themes.meteor.widgets;

import kult.klient.gui.renderer.GuiRenderer;
import kult.klient.gui.widgets.WQuad;
import kult.klient.utils.render.color.Color;

public class WMeteorQuad extends WQuad {
    public WMeteorQuad(Color color) {
        super(color);
    }

    @Override
    protected void onRender(GuiRenderer renderer, double mouseX, double mouseY, double delta) {
        renderer.quadRounded(x, y, width, height, color, theme.roundAmount());
    }
}
