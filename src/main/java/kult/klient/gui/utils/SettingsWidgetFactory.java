package kult.klient.gui.utils;

import kult.klient.gui.widgets.WWidget;
import kult.klient.gui.GuiTheme;
import kult.klient.settings.Settings;

public interface SettingsWidgetFactory {
    WWidget create(GuiTheme theme, Settings settings, String filter);
}
