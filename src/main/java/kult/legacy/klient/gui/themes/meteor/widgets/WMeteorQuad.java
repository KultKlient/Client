package kultklient.legacy.client.gui.themes.meteor.widgets;

import kultklient.legacy.client.gui.renderer.GuiRenderer;
import kultklient.legacy.client.gui.widgets.WQuad;
import kultklient.legacy.client.utils.render.color.Color;

public class WMeteorQuad extends WQuad {
    public WMeteorQuad(Color color) {
        super(color);
    }

    @Override
    protected void onRender(GuiRenderer renderer, double mouseX, double mouseY, double delta) {
        renderer.quadRounded(x, y, width, height, color, theme.roundAmount());
    }
}
