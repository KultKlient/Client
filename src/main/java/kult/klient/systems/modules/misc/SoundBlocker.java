package kult.klient.systems.modules.misc;

import kult.klient.eventbus.EventHandler;
import kult.klient.events.world.PlaySoundEvent;
import kult.klient.settings.Setting;
import kult.klient.settings.SettingGroup;
import kult.klient.settings.SoundEventListSetting;
import kult.klient.systems.modules.Categories;
import kult.klient.systems.modules.Module;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvent;

import java.util.List;

public class SoundBlocker extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    // General

    private final Setting<List<SoundEvent>> sounds = sgGeneral.add(new SoundEventListSetting.Builder()
        .name("sounds")
        .description("Sounds to block.")
        .build()
    );

    public SoundBlocker() {
        super(Categories.Misc, Items.COMMAND_BLOCK, "sound-blocker", "Cancels out selected sounds.");
    }

    @EventHandler
    private void onPlaySound(PlaySoundEvent event) {
        for (SoundEvent sound : sounds.get()) {
            if (sound.getId().equals(event.sound.getId())) {
                event.cancel();
                break;
            }
        }
    }
}
