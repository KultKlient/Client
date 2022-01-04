package kult.legacy.klient.gui.widgets.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import kult.legacy.klient.gui.GuiTheme;
import kult.legacy.klient.gui.screens.music.PlaylistsScreen;
import kult.legacy.klient.gui.tabs.builtin.MusicTab;
import kult.legacy.klient.gui.widgets.containers.WHorizontalList;
import kult.legacy.klient.gui.widgets.containers.WTable;
import kult.legacy.klient.gui.widgets.pressable.WButton;
import kult.legacy.klient.music.Music;

import java.util.Collections;

import static kult.legacy.klient.KultKlientLegacy.mc;

public class WPlaybackControls extends WMusicWidget {
    @Override
    public void add(WTable parent, MusicTab.MusicScreen screen, GuiTheme theme) {
        WHorizontalList list = parent.add(theme.horizontalList()).widget();
        WButton pauseButton = list.add(theme.button(Music.player.isPaused() ? "Resume" : "Pause")).widget();
        pauseButton.action = () -> {
            if (Music.player.isPaused()) {
                Music.trackScheduler.setPaused(false);
                pauseButton.set("Pause");
            } else {
                Music.trackScheduler.setPaused(true);
                pauseButton.set("Resume");
            }
        };

        if (Music.trackScheduler.hasNext()) {
            list.add(theme.button("Shuffle")).widget().action = () -> {
                Collections.shuffle(Music.trackScheduler.tracks);
                screen.construct();
            };
            list.add(theme.button("Clear")).widget().action = () -> {
                Music.trackScheduler.tracks.clear();
                screen.construct();
            };
        }

        list.add(theme.button("Playlists")).widget().action = () -> {
            mc.setScreen(new PlaylistsScreen(theme, screen));
        };

        String duration;
        AudioTrack current = Music.player.getPlayingTrack();
        if (current == null) duration = "Not playing";
        else duration = Music.getTime();

        list.add(theme.label("Duration: " + duration)).right();

        parent.row();
        super.add(parent, screen, theme);
    }
}
