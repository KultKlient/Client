package kult.klient.gui.screens.settings;

import kult.klient.gui.GuiTheme;
import kult.klient.gui.widgets.WWidget;
import kult.klient.settings.Setting;
import kult.klient.systems.modules.Module;
import kult.klient.systems.modules.Modules;

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
