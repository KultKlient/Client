package kultklient.legacy.client.gui.themes.meteor.widgets;

import kultklient.legacy.client.gui.renderer.GuiRenderer;
import kultklient.legacy.client.gui.themes.meteor.MeteorGuiTheme;
import kultklient.legacy.client.gui.themes.meteor.MeteorWidget;
import kultklient.legacy.client.gui.widgets.WVerticalSeparator;
import kultklient.legacy.client.utils.render.color.Color;

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
