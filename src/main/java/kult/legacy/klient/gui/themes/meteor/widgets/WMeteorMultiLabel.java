package kultklient.legacy.client.gui.themes.meteor.widgets;

import kultklient.legacy.client.gui.renderer.GuiRenderer;
import kultklient.legacy.client.gui.themes.meteor.MeteorWidget;
import kultklient.legacy.client.gui.widgets.WMultiLabel;
import kultklient.legacy.client.utils.render.color.Color;

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
