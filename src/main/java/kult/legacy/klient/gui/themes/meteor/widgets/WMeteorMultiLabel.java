package kult.legacy.klient.gui.themes.meteor.widgets;

import kult.legacy.klient.gui.renderer.GuiRenderer;
import kult.legacy.klient.gui.themes.meteor.MeteorWidget;
import kult.legacy.klient.gui.widgets.WMultiLabel;
import kult.legacy.klient.utils.render.color.Color;

public class WMeteorMultiLabel extends WMultiLabel implements MeteorWidget {
    public WMeteorMultiLabel(String text, boolean title, double maxWidth) {
        super(text, title, maxWidth);
    }

    @Override
    protected void onRender(GuiRenderer renderer, double mouseX, double mouseY, double delta) {
        double h = theme.textHeight(title);
        Color color = theme().textColor.get();

        for (int i = 0; i < lines.size(); i++) {
            renderer.text(lines.get(i), x, y + h * i, color, false);
        }
    }
}
