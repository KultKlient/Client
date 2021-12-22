package kultklient.legacy.client.gui.widgets.music;

import kultklient.legacy.client.gui.GuiTheme;
import kultklient.legacy.client.gui.screens.music.PlaylistViewScreen;
import kultklient.legacy.client.gui.tabs.builtin.MusicTab;
import kultklient.legacy.client.gui.widgets.containers.WTable;
import kultklient.legacy.client.gui.widgets.input.WTextBox;
import kultklient.legacy.client.utils.music.SearchUtils;

import static kultklient.legacy.client.KultKlientLegacy.mc;

public class WSearchBar extends WMusicWidget {
    @Override
    public void add(WTable parent, MusicTab.MusicScreen screen, GuiTheme theme) {
        WTextBox box = parent.add(theme.textBox("")).expandX().widget();
        parent.add(theme.button("Search")).widget().action = () -> SearchUtils.search(box.get(), playlist -> mc.setScreen(new PlaylistViewScreen(theme, playlist, screen)));
        super.add(parent, screen, theme);
    }
}
