package kultklient.legacy.client.gui.themes.kultklient.widgets.pressable;

import kultklient.legacy.client.gui.renderer.GuiRenderer;
import kultklient.legacy.client.gui.themes.kultklient.KultKlientGuiTheme;
import kultklient.legacy.client.gui.themes.kultklient.KultKlientWidget;
import kultklient.legacy.client.gui.widgets.pressable.WPlus;

public class WKultKlientPlus extends WPlus implements KultKlientWidget {
    @Override
    protected void onRender(GuiRenderer renderer, double mouseX, double mouseY, double delta) {
        KultKlientGuiTheme theme = theme();
        double pad = pad();
        double s = theme.scale(3);

        renderBackground(renderer, this, pressed, mouseOver);
        renderer.quad(x + pad, y + height / 2 - s / 2, width - pad * 2, s, theme.plusColor.get());
        renderer.quad(x + width / 2 - s / 2, y + pad, s, height - pad * 2, theme.plusColor.get());
    }
}
