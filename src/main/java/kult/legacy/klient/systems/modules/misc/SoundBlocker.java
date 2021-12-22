package kultklient.legacy.client.systems.modules.misc;

import kultklient.legacy.client.events.world.PlaySoundEvent;
import kultklient.legacy.client.settings.Setting;
import kultklient.legacy.client.settings.SettingGroup;
import kultklient.legacy.client.settings.SoundEventListSetting;
import kultklient.legacy.client.systems.modules.Module;
import kultklient.legacy.client.systems.modules.Categories;
import kultklient.legacy.client.eventbus.EventHandler;
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
