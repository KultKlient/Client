package kult.klient.gui.themes.kultklient.widgets.pressable;

import kult.klient.gui.renderer.GuiRenderer;
import kult.klient.gui.themes.kultklient.KultKlientWidget;
import kult.klient.gui.widgets.pressable.WTriangle;

public class WKultKlientTriangle extends WTriangle implements KultKlientWidget {
    @Override
    protected void onRender(GuiRenderer renderer, double mouseX, double mouseY, double delta) {
        renderer.rotatedQuad(x, y, width, height, rotation, GuiRenderer.TRIANGLE, theme().backgroundColor.get(pressed, mouseOver));
    }
}
