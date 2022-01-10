package kult.klient.systems.modules.movement;

import kult.klient.eventbus.EventHandler;
import kult.klient.events.entity.player.JumpVelocityMultiplierEvent;
import kult.klient.settings.DoubleSetting;
import kult.klient.settings.Setting;
import kult.klient.settings.SettingGroup;
import kult.klient.systems.modules.Categories;
import kult.klient.systems.modules.Module;
import net.minecraft.item.Items;

public class HighJump extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    // General

    private final Setting<Double> multiplier = sgGeneral.add(new DoubleSetting.Builder()
        .name("jump-multiplier")
        .description("Jump height multiplier.")
        .defaultValue(1)
        .min(0)
        .sliderRange(0, 5)
        .build()
    );

    public HighJump() {
        super(Categories.Movement, Items.RABBIT, "high-jump", "Makes you jump higher than normal.");
    }

    @EventHandler
    private void onJumpVelocityMultiplier(JumpVelocityMultiplierEvent event) {
        event.multiplier *= multiplier.get();
    }
}
