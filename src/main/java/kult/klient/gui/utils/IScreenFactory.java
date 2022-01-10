package kult.klient.gui.utils;

import kult.klient.gui.GuiTheme;
import kult.klient.gui.WidgetScreen;

public interface IScreenFactory {
    WidgetScreen createScreen(GuiTheme theme);
}
