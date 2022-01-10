package kult.klient.gui.widgets.music;

import kult.klient.gui.widgets.containers.WTable;
import kult.klient.gui.GuiTheme;
import kult.klient.gui.screens.music.PlaylistViewScreen;
import kult.klient.gui.tabs.builtin.MusicTab;
import kult.klient.gui.widgets.input.WTextBox;
import kult.klient.utils.music.SearchUtils;

import static kult.klient.KultKlient.mc;

public class WSearchBar extends WMusicWidget {
    @Override
    public void add(WTable parent, MusicTab.MusicScreen screen, GuiTheme theme) {
        WTextBox box = parent.add(theme.textBox("")).expandX().widget();
        parent.add(theme.button("Search")).widget().action = () -> SearchUtils.search(box.get(), playlist -> mc.setScreen(new PlaylistViewScreen(theme, playlist, screen)));
        super.add(parent, screen, theme);
    }
}
