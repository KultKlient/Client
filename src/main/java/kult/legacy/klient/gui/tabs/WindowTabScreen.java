package kultklient.legacy.client.gui.tabs;

import kultklient.legacy.client.gui.GuiTheme;
import kultklient.legacy.client.gui.utils.Cell;
import kultklient.legacy.client.gui.widgets.WWidget;
import kultklient.legacy.client.gui.widgets.containers.WWindow;

public abstract class WindowTabScreen extends TabScreen {
    protected final WWindow window;

    public WindowTabScreen(GuiTheme theme, Tab tab) {
        super(theme, tab);

        window = super.add(theme.window(tab.name)).center().widget();
    }

    @Override
    public <W extends WWidget> Cell<W> add(W widget) {
        return window.add(widget);
    }

    @Override
    public void clear() {
        window.clear();
    }
}
