package kult.klient.gui.widgets.music;

import kult.klient.gui.widgets.containers.WHorizontalList;
import kult.klient.gui.widgets.containers.WTable;
import kult.klient.gui.GuiTheme;
import kult.klient.gui.tabs.builtin.MusicTab;

import java.util.function.Consumer;

public class WPaginationProvider extends WMusicWidget {
    private int currentPage = 0;
    private int maxPage = 0;
    private final Consumer<Integer> onPageChange;

    @Override
    public void add(WTable parent, MusicTab.MusicScreen screen, GuiTheme theme) {
        if (maxPage > 1) {
            parent.row();
            WHorizontalList list = parent.add(theme.horizontalList()).widget();
            for (int i = 0; i < maxPage; i++) {
                int j = i;
                String pageName = Integer.toString(j + 1);
                if (j == currentPage) list.add(theme.label(pageName));
                else {
                    list.add(theme.button(pageName)).widget().action = () -> {
                        currentPage = j;
                        if (onPageChange != null) onPageChange.accept(j);
                    };
                }
            }
        }
        super.add(parent, screen, theme);
    }

    public void setMaxPage(int max) {
        maxPage = max;
        if (currentPage > max) currentPage = max;
    }

    public int getPageOffset() {
        return currentPage * MusicTab.MusicScreen.pageSize;
    }

    public WPaginationProvider(Consumer<Integer> onPageChange) {
        this.onPageChange = onPageChange;
    }
}
