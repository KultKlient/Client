package kult.legacy.klient.gui.widgets.music;

import kult.legacy.klient.gui.GuiTheme;
import kult.legacy.klient.gui.screens.music.PlaylistViewScreen;
import kult.legacy.klient.gui.tabs.builtin.MusicTab;
import kult.legacy.klient.gui.widgets.containers.WTable;
import kult.legacy.klient.gui.widgets.input.WTextBox;
import kult.legacy.klient.utils.music.SearchUtils;

import static kult.legacy.klient.KultKlientLegacy.mc;

public class WSearchBar extends WMusicWidget {
    @Override
    public void add(WTable parent, MusicTab.MusicScreen screen, GuiTheme theme) {
        WTextBox box = parent.add(theme.textBox("")).expandX().widget();
        parent.add(theme.button("Search")).widget().action = () -> SearchUtils.search(box.get(), playlist -> mc.setScreen(new PlaylistViewScreen(theme, playlist, screen)));
        super.add(parent, screen, theme);
    }
}
