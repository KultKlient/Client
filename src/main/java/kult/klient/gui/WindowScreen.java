package kult.klient.gui;

import kult.klient.gui.utils.Cell;
import kult.klient.gui.widgets.WWidget;
import kult.klient.gui.widgets.containers.WWindow;

public abstract class WindowScreen extends WidgetScreen {
    protected final WWindow window;

    public WindowScreen(GuiTheme theme, String title) {
        super(theme, title);

        window = super.add(theme.window(title)).center().widget();
        window.view.scrollOnlyWhenMouseOver = false;
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
