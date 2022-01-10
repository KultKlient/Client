package kult.klient.gui.themes.meteor.widgets;

import kult.klient.gui.renderer.GuiRenderer;
import kult.klient.gui.themes.meteor.MeteorGuiTheme;
import kult.klient.gui.themes.meteor.MeteorWidget;
import kult.klient.gui.widgets.WVerticalSeparator;
import kult.klient.utils.render.color.Color;

public class WMeteorVerticalSeparator extends WVerticalSeparator implements MeteorWidget {
    @Override
    protected void onRender(GuiRenderer renderer, double mouseX, double mouseY, double delta) {
        MeteorGuiTheme theme = theme();
        Color colorEdges = theme.separatorEdges.get();
        Color colorCenter = theme.separatorCenter.get();

        double s = theme.scale(1);
        double offsetX = Math.round(width / 2.0);

        renderer.quad(x + offsetX, y, s, height / 2, colorEdges, colorEdges, colorCenter, colorCenter);
        renderer.quad(x + offsetX, y + height / 2, s, height / 2, colorCenter, colorCenter, colorEdges, colorEdges);
    }
}
