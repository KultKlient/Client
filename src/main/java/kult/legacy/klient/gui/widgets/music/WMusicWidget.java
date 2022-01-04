package kult.legacy.klient.gui.widgets.music;

import kult.legacy.klient.gui.GuiTheme;
import kult.legacy.klient.gui.tabs.builtin.MusicTab;
import kult.legacy.klient.gui.widgets.containers.WTable;

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
