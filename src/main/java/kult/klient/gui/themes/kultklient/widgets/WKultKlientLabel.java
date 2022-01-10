package kult.klient.gui.themes.kultklient.widgets;

import kult.klient.gui.renderer.GuiRenderer;
import kult.klient.gui.themes.kultklient.KultKlientWidget;
import kult.klient.gui.widgets.WLabel;

public class WKultKlientLabel extends WLabel implements KultKlientWidget {
    public WKultKlientLabel(String text, boolean title) {
        super(text, title);
    }

    @Override
    protected void onRender(GuiRenderer renderer, double mouseX, double mouseY, double delta) {
        if (!text.isEmpty()) {
            renderer.text(text, x, y, color != null ? color : (title ? theme().titleTextColor.get() : theme().textColor.get()), title);
        }
    }
}
