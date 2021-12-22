package kultklient.legacy.client.gui.widgets.music;

import kultklient.legacy.client.gui.GuiTheme;
import kultklient.legacy.client.gui.tabs.builtin.MusicTab;
import kultklient.legacy.client.gui.widgets.containers.WTable;

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
