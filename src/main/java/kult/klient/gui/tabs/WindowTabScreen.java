package kult.klient.gui.tabs;

import kult.klient.gui.utils.Cell;
import kult.klient.gui.widgets.WWidget;
import kult.klient.gui.widgets.containers.WWindow;
import kult.klient.gui.GuiTheme;

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
