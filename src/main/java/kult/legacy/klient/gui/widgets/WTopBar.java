package kultklient.legacy.client.gui.widgets;

import kultklient.legacy.client.gui.renderer.GuiRenderer;
import kultklient.legacy.client.gui.tabs.Tab;
import kultklient.legacy.client.gui.tabs.TabScreen;
import kultklient.legacy.client.gui.tabs.Tabs;
import kultklient.legacy.client.gui.widgets.containers.WHorizontalList;
import kultklient.legacy.client.gui.widgets.pressable.WPressable;
import kultklient.legacy.client.utils.render.color.Color;
import net.minecraft.client.gui.screen.Screen;

import static kultklient.legacy.client.KultKlientLegacy.mc;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPos;

public abstract class WTopBar extends WHorizontalList {
    protected abstract Color getButtonColor(boolean pressed, boolean hovered);

    protected abstract Color getNameColor();

    public WTopBar() {
        spacing = 0;
    }

    @Override
    public void init() {
        for (Tab tab : Tabs.get()) {
            add(new WTopBarButton(tab));
        }
    }

    protected int getState(WTopBarButton btn) {
        int a = 0;
        if (btn.equals(cells.get(0).widget()))
            a |= 1;
        if (btn.equals(cells.get(cells.size() - 1).widget()))
            a |= 2;
        return a;
    }

    protected class WTopBarButton extends WPressable {
        private final Tab tab;

        public WTopBarButton(Tab tab) {
            this.tab = tab;
        }

        @Override
        protected void onCalculateSize() {
            double pad = pad();

            width = pad + theme.textWidth(tab.name) + pad;
            height = pad + theme.textHeight() + pad;
        }

        @Override
        protected void onPressed(int button) {
            Screen screen = mc.currentScreen;

            if (!(screen instanceof TabScreen) || ((TabScreen) screen).tab != tab) {
                double mouseX = mc.mouse.getX();
                double mouseY = mc.mouse.getY();

                tab.openScreen(theme);
                glfwSetCursorPos(mc.getWindow().getHandle(), mouseX, mouseY);
            }
        }

        @Override
        protected void onRender(GuiRenderer renderer, double mouseX, double mouseY, double delta) {
            double pad = pad();
            Color color = getButtonColor(pressed || (mc.currentScreen instanceof TabScreen && ((TabScreen) mc.currentScreen).tab == tab), mouseOver);
            switch (getState(this)) {
                case 1 -> renderer.quadRoundedSide(this, color, theme.roundAmount(), false);
                case 2 -> renderer.quadRoundedSide(this, color, theme.roundAmount(), true);
                case 3 -> renderer.quadRounded(this, color, theme.roundAmount());
                default -> renderer.quad(this, color);
            }

            renderer.text(tab.name, x + pad, y + pad, getNameColor(), false);
        }
    }
}
