package kult.legacy.klient.systems.modules.misc;

import kult.legacy.klient.events.world.PlaySoundEvent;
import kult.legacy.klient.settings.Setting;
import kult.legacy.klient.settings.SettingGroup;
import kult.legacy.klient.settings.SoundEventListSetting;
import kult.legacy.klient.systems.modules.Module;
import kult.legacy.klient.systems.modules.Categories;
import kult.legacy.klient.eventbus.EventHandler;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvent;

import java.util.List;

public class SoundBlocker extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

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
