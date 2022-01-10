package kult.klient.gui.tabs;

import kult.klient.gui.GuiTheme;
import net.minecraft.client.gui.screen.Screen;

import static kult.klient.KultKlient.mc;

public abstract class Tab {
    public final String name;

    public Tab(String name) {
        this.name = name;
    }

    public void openScreen(GuiTheme theme) {
        TabScreen screen = this.createScreen(theme);
        screen.addDirect(theme.topBar()).top().centerX();
        mc.setScreen(screen);
    }

    public abstract TabScreen createScreen(GuiTheme theme);

    public abstract boolean isScreen(Screen screen);
}
