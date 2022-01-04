package kult.legacy.klient.gui.screens.settings;

import kult.legacy.klient.gui.GuiTheme;
import kult.legacy.klient.gui.widgets.WWidget;
import kult.legacy.klient.settings.Setting;
import kult.legacy.klient.systems.modules.Module;
import kult.legacy.klient.systems.modules.Modules;

import java.util.List;

public class ModuleListSettingScreen extends LeftRightListSettingScreen<Module> {
    public ModuleListSettingScreen(GuiTheme theme, Setting<List<Module>> setting) {
        super(theme, "Select Modules", setting, setting.get(), Modules.REGISTRY);
    }

    @Override
    protected WWidget getValueWidget(Module value) {
        return theme.label(getValueName(value));
    }

    @Override
    protected String getValueName(Module value) {
        return value.title;
    }
}
