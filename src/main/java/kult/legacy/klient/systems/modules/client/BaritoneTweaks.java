package kultklient.legacy.client.systems.modules.client;

import baritone.api.BaritoneAPI;
import kultklient.legacy.client.eventbus.EventHandler;
import kultklient.legacy.client.events.world.TickEvent;
import kultklient.legacy.client.settings.BoolSetting;
import kultklient.legacy.client.settings.IntSetting;
import kultklient.legacy.client.settings.Setting;
import kultklient.legacy.client.settings.SettingGroup;
import kultklient.legacy.client.systems.modules.Categories;
import kultklient.legacy.client.systems.modules.Module;
import net.minecraft.item.Items;

public class BaritoneTweaks extends Module {
    private final SettingGroup sgSmartSprint = settings.createGroup("Smart Sprint");

    // Smart Sprint

    private final Setting<Boolean> smartSprintActive = sgSmartSprint.add(new BoolSetting.Builder()
        .name("active")
        .description("Sprint with enough food saturation only.")
        .defaultValue(true)
        .build()
    );

    private final Setting<Integer> smartSprintHunger = sgSmartSprint.add(new IntSetting.Builder()
        .name("hunger")
        .description("Smart sprint minimum food saturation level.")
        .defaultValue(8)
        .sliderRange(1, 20)
        .build()
    );

    public BaritoneTweaks() {
        super(Categories.Misc, Items.COMMAND_BLOCK, "baritone-tweaks", "Various baritone related tweaks.");
    }

    @EventHandler
    private void onTickPost(TickEvent.Post event) {
        if (smartSprintActive.get()) {
            if (mc.player.getHungerManager().getFoodLevel() >= smartSprintHunger.get()) BaritoneAPI.getSettings().allowSprint.value = true;
            else BaritoneAPI.getSettings().allowSprint.value = false;
        }
    }
}
