package kult.legacy.klient.gui.themes.kultklient.widgets;

import kult.legacy.klient.gui.renderer.GuiRenderer;
import kult.legacy.klient.gui.themes.kultklient.KultKlientGuiTheme;
import kult.legacy.klient.gui.themes.kultklient.KultKlientWidget;
import kult.legacy.klient.gui.widgets.WVerticalSeparator;
import kult.legacy.klient.utils.render.color.Color;

public class WKultKlientVerticalSeparator extends WVerticalSeparator implements KultKlientWidget {
    @Override
    protected void onRender(GuiRenderer renderer, double mouseX, double mouseY, double delta) {
        KultKlientGuiTheme theme = theme();
        Color colorEdges = theme.separatorEdges.get();
        Color colorCenter = theme.separatorCenter.get();

        double s = theme.scale(1);
        double offsetX = Math.round(width / 2.0);

        renderer.quad(x + offsetX, y, s, height / 2, colorEdges, colorEdges, colorCenter, colorCenter);
        renderer.quad(x + offsetX, y + height / 2, s, height / 2, colorCenter, colorCenter, colorEdges, colorEdges);
    }
}
