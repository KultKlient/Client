package kult.klient.gui.themes.kultklient.widgets;

import kult.klient.gui.renderer.GuiRenderer;
import kult.klient.gui.themes.kultklient.KultKlientWidget;
import kult.klient.gui.widgets.WMultiLabel;
import kult.klient.utils.render.color.Color;

public class WKultKlientMultiLabel extends WMultiLabel implements KultKlientWidget {
    public WKultKlientMultiLabel(String text, boolean title, double maxWidth) {
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
