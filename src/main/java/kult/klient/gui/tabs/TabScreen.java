package kult.klient.gui.tabs;

import kult.klient.gui.utils.Cell;
import kult.klient.gui.widgets.WWidget;
import kult.klient.gui.GuiTheme;
import kult.klient.gui.WidgetScreen;

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
