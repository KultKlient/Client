package kultklient.legacy.client.gui.screens.settings;

import kultklient.legacy.client.gui.GuiTheme;
import kultklient.legacy.client.gui.widgets.WWidget;
import kultklient.legacy.client.settings.Setting;
import kultklient.legacy.client.systems.modules.Module;
import kultklient.legacy.client.systems.modules.Modules;

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
