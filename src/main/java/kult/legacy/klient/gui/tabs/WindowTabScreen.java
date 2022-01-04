package kult.legacy.klient.gui.tabs;

import kult.legacy.klient.gui.GuiTheme;
import kult.legacy.klient.gui.utils.Cell;
import kult.legacy.klient.gui.widgets.WWidget;
import kult.legacy.klient.gui.widgets.containers.WWindow;

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
