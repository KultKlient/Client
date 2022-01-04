package kult.legacy.klient.gui.themes.kultklient.widgets;

import kult.legacy.klient.gui.renderer.GuiRenderer;
import kult.legacy.klient.gui.themes.kultklient.KultKlientWidget;
import kult.legacy.klient.gui.widgets.containers.WWindow;

public class WKultKlientWindow extends WWindow implements KultKlientWidget {
    public WKultKlientWindow(String title) {
        super(title);
    }

    @Override
    protected WHeader header() {
        return new WKultKlientHeader();
    }

    @Override
    protected void onRender(GuiRenderer renderer, double mouseX, double mouseY, double delta) {
        if (expanded || animProgress > 0) {
            renderer.quadRounded(x, y + header.height / 2, width, height - header.height / 2, theme().backgroundColor.get(), theme.roundAmount(), false);
        }
    }

    private class WKultKlientHeader extends WHeader {
        @Override
        protected void onRender(GuiRenderer renderer, double mouseX, double mouseY, double delta) {
            renderer.quadRounded(this, theme().mainColor.get(), theme.roundAmount());
        }
    }
}
