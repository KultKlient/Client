package kultklient.legacy.client.gui.utils;

import kultklient.legacy.client.gui.GuiTheme;
import kultklient.legacy.client.gui.widgets.WWidget;
import kultklient.legacy.client.settings.Settings;

public interface SettingsWidgetFactory {
    WWidget create(GuiTheme theme, Settings settings, String filter);
}
