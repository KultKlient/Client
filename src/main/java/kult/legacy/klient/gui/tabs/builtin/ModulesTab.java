package kult.legacy.klient.gui.tabs.builtin;

import kult.legacy.klient.gui.GuiTheme;
import kult.legacy.klient.gui.GuiThemes;
import kult.legacy.klient.gui.tabs.Tab;
import kult.legacy.klient.gui.tabs.TabScreen;
import net.minecraft.client.gui.screen.Screen;

public class ModulesTab extends Tab {
    public ModulesTab() {
        super("Modules");
    }

    @Override
    public TabScreen createScreen(GuiTheme theme) {
        return theme.modulesScreen();
    }

    @Override
    public boolean isScreen(Screen screen) {
        return GuiThemes.get().isModulesScreen(screen);
    }
}
