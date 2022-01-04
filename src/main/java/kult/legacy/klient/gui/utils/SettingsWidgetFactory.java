package kult.legacy.klient.gui.utils;

import kult.legacy.klient.gui.GuiTheme;
import kult.legacy.klient.gui.widgets.WWidget;
import kult.legacy.klient.settings.Settings;

public interface SettingsWidgetFactory {
    WWidget create(GuiTheme theme, Settings settings, String filter);
}
