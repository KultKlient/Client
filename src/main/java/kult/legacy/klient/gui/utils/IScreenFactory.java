package kult.legacy.klient.gui.utils;

import kult.legacy.klient.gui.GuiTheme;
import kult.legacy.klient.gui.WidgetScreen;

public interface IScreenFactory {
    WidgetScreen createScreen(GuiTheme theme);
}
