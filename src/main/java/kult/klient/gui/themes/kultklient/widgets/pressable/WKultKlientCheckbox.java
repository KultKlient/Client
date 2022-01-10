package kult.klient.gui.themes.kultklient.widgets.pressable;

import kult.klient.gui.renderer.GuiRenderer;
import kult.klient.gui.themes.kultklient.KultKlientGuiTheme;
import kult.klient.gui.themes.kultklient.KultKlientWidget;
import kult.klient.gui.widgets.pressable.WCheckbox;
import kult.klient.utils.Utils;

public class WKultKlientCheckbox extends WCheckbox implements KultKlientWidget {
    private double animProgress;

    public WKultKlientCheckbox(boolean checked) {
        super(checked);
        animProgress = checked ? 1 : 0;
    }

    @Override
    protected void onRender(GuiRenderer renderer, double mouseX, double mouseY, double delta) {
        KultKlientGuiTheme theme = theme();

        animProgress += (checked ? 1 : -1) * delta * 14;
        animProgress = Utils.clamp(animProgress, 0, 1);

        renderBackground(renderer, this, pressed, mouseOver);

        if (animProgress > 0) {
            double cs = (width - theme.scale(2)) / 1.75 * animProgress;
            renderer.quadRounded(x + (width - cs) / 2, y + (height - cs) / 2, cs, cs, theme.checkboxColor.get(), theme.roundAmount());
        }
    }
}
