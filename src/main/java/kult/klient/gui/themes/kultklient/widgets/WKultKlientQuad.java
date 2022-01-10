package kult.klient.gui.themes.kultklient.widgets;

import kult.klient.gui.renderer.GuiRenderer;
import kult.klient.gui.widgets.WQuad;
import kult.klient.utils.render.color.Color;

public class WKultKlientQuad extends WQuad {
    public WKultKlientQuad(Color color) {
        super(color);
    }

    @Override
    protected void onRender(GuiRenderer renderer, double mouseX, double mouseY, double delta) {
        renderer.quadRounded(x, y, width, height, color, theme.roundAmount());
    }
}
