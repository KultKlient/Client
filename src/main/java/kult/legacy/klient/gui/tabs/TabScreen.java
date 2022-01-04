package kult.legacy.klient.gui.tabs;

import kult.legacy.klient.gui.GuiTheme;
import kult.legacy.klient.gui.WidgetScreen;
import kult.legacy.klient.gui.utils.Cell;
import kult.legacy.klient.gui.widgets.WWidget;

public abstract class TabScreen extends WidgetScreen {
    public final Tab tab;

    public TabScreen(GuiTheme theme, Tab tab) {
        super(theme, tab.name);
        this.tab = tab;
    }

    public <T extends WWidget> Cell<T> addDirect(T widget) {
        return super.add(widget);
    }
}
