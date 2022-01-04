package kult.legacy.klient.gui.themes.kultklient.widgets;

import kult.legacy.klient.gui.renderer.GuiRenderer;
import kult.legacy.klient.gui.themes.kultklient.KultKlientWidget;
import kult.legacy.klient.gui.widgets.WWidget;
import kult.legacy.klient.gui.widgets.containers.WSection;
import kult.legacy.klient.gui.widgets.pressable.WTriangle;

public class WKultKlientSection extends WSection {
    public WKultKlientSection(String title, boolean expanded, WWidget headerWidget) {
        super(title, expanded, headerWidget);
    }

    @Override
    protected WHeader createHeader() {
        return new WKultKlientHeader(title);
    }

    protected class WKultKlientHeader extends WHeader {
        private WTriangle triangle;

        public WKultKlientHeader(String title) {
            super(title);
        }

        @Override
        public void init() {
            add(theme.horizontalSeparator(title)).expandX();

            if (headerWidget != null) add(headerWidget);

            triangle = new WHeaderTriangle();
            triangle.theme = theme;
            triangle.action = this::onClick;

            add(triangle);
        }

        @Override
        protected void onRender(GuiRenderer renderer, double mouseX, double mouseY, double delta) {
            triangle.rotation = (1 - animProgress) * -90;
        }
    }

    protected static class WHeaderTriangle extends WTriangle implements KultKlientWidget {
        @Override
        protected void onRender(GuiRenderer renderer, double mouseX, double mouseY, double delta) {
            renderer.rotatedQuad(x, y, width, height, rotation, GuiRenderer.TRIANGLE, theme().textColor.get());
        }
    }
}
