package kult.legacy.klient.systems.modules.movement;

import kult.legacy.klient.events.world.TickEvent;
import kult.legacy.klient.settings.BoolSetting;
import kult.legacy.klient.settings.Setting;
import kult.legacy.klient.settings.SettingGroup;
import kult.legacy.klient.systems.modules.Categories;
import kult.legacy.klient.systems.modules.Module;
import kult.legacy.klient.eventbus.EventHandler;
import net.minecraft.item.Items;

public class Sprint extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> whenStationary = sgGeneral.add(new BoolSetting.Builder()
        .name("when-stationary")
        .description("Continues sprinting even if you do not move.")
        .defaultValue(true)
        .build()
    );

    public Sprint() {
        super(Categories.Movement, Items.DIAMOND_BOOTS, "sprint", "Automatically sprints.");
    }

    @Override
    public void onDeactivate() {
        mc.player.setSprinting(false);
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (mc.player.forwardSpeed > 0 && !whenStationary.get()) {
            mc.player.setSprinting(true);
        } else if (whenStationary.get()) {
            mc.player.setSprinting(true);
        }
    }
}
