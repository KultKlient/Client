package kultklient.legacy.client.gui.tabs;

import kultklient.legacy.client.gui.GuiTheme;
import kultklient.legacy.client.gui.WidgetScreen;
import kultklient.legacy.client.gui.utils.Cell;
import kultklient.legacy.client.gui.widgets.WWidget;

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
