package kultklient.legacy.client.gui.themes.kultklient.widgets;

import kultklient.legacy.client.gui.renderer.GuiRenderer;
import kultklient.legacy.client.gui.themes.kultklient.KultKlientWidget;
import kultklient.legacy.client.gui.widgets.WTooltip;

public class WKultKlientTooltip extends WTooltip implements KultKlientWidget {
    public WKultKlientTooltip(String text) {
        super(text);
    }

    @Override
    protected void onRender(GuiRenderer renderer, double mouseX, double mouseY, double delta) {
        renderer.quad(this, theme().backgroundColor.get());
    }
}
