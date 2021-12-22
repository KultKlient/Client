package kultklient.legacy.client.gui.utils;

import kultklient.legacy.client.gui.GuiTheme;
import kultklient.legacy.client.gui.WidgetScreen;

public interface IScreenFactory {
    WidgetScreen createScreen(GuiTheme theme);
}
