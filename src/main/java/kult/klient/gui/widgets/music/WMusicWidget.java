package kult.klient.gui.widgets.music;

import kult.klient.gui.widgets.containers.WTable;
import kult.klient.gui.GuiTheme;
import kult.klient.gui.tabs.builtin.MusicTab;

import java.util.ArrayList;
import java.util.List;

public abstract class WMusicWidget {
    protected List<WMusicWidget> childWidgets = new ArrayList<>();
    public void add(WTable parent, MusicTab.MusicScreen screen, GuiTheme theme) {
        for (WMusicWidget child : childWidgets) {
            child.add(parent, screen, theme);
        }
    }
}
