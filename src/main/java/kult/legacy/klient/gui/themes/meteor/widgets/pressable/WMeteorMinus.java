package kult.legacy.klient.gui.themes.meteor.widgets.pressable;

import kult.legacy.klient.gui.renderer.GuiRenderer;
import kult.legacy.klient.gui.themes.meteor.MeteorWidget;
import kult.legacy.klient.gui.widgets.pressable.WMinus;

public class WMeteorMinus extends WMinus implements MeteorWidget {
    @Override
    protected void onRender(GuiRenderer renderer, double mouseX, double mouseY, double delta) {
        double pad = pad();
        double s = theme.scale(3);

        renderBackground(renderer, this, pressed, mouseOver);
        renderer.quad(x + pad, y + height / 2 - s / 2, width - pad * 2, s, theme().minusColor.get());
    }
}
