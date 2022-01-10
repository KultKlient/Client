package kult.klient.gui.tabs.builtin;

import kult.klient.gui.GuiTheme;
import kult.klient.gui.GuiThemes;
import kult.klient.gui.tabs.Tab;
import kult.klient.gui.tabs.TabScreen;
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
