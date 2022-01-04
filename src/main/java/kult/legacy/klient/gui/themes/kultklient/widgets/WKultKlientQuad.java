package kult.legacy.klient.gui.themes.kultklient.widgets;

import kult.legacy.klient.gui.renderer.GuiRenderer;
import kult.legacy.klient.gui.widgets.WQuad;
import kult.legacy.klient.utils.render.color.Color;

public class WKultKlientQuad extends WQuad {
    public WKultKlientQuad(Color color) {
        super(color);
    }

    @Override
    protected void onRender(GuiRenderer renderer, double mouseX, double mouseY, double delta) {
        renderer.quadRounded(x, y, width, height, color, theme.roundAmount());
    }
}
