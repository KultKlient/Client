package kultklient.legacy.client.gui.themes.kultklient.widgets.pressable;

import kultklient.legacy.client.gui.renderer.GuiRenderer;
import kultklient.legacy.client.gui.renderer.packer.GuiTexture;
import kultklient.legacy.client.gui.themes.kultklient.KultKlientGuiTheme;
import kultklient.legacy.client.gui.themes.kultklient.KultKlientWidget;
import kultklient.legacy.client.gui.widgets.pressable.WButton;

public class WKultKlientButton extends WButton implements KultKlientWidget {
    public WKultKlientButton(String text, GuiTexture texture) {
        super(text, texture);
    }

    @Override
    protected void onRender(GuiRenderer renderer, double mouseX, double mouseY, double delta) {
        KultKlientGuiTheme theme = theme();
        double pad = pad();

        renderBackground(renderer, this, pressed, mouseOver);

        if (text != null) {
            renderer.text(text, x + width / 2 - textWidth / 2, y + pad, theme.textColor.get(), false);
        } else {
            double ts = theme.textHeight();
            renderer.quad(x + width / 2 - ts / 2, y + pad, ts, ts, texture, theme.textColor.get());
        }
    }
}
