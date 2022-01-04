package kult.legacy.klient.systems.modules.movement;

import kult.legacy.klient.events.kultklientlegacy.KeyEvent;
import kult.legacy.klient.events.world.TickEvent;
import kult.legacy.klient.settings.BoolSetting;
import kult.legacy.klient.settings.Setting;
import kult.legacy.klient.settings.SettingGroup;
import kult.legacy.klient.systems.modules.Categories;
import kult.legacy.klient.systems.modules.Module;
import kult.legacy.klient.systems.modules.Modules;
import kult.legacy.klient.systems.modules.render.Freecam;
import kult.legacy.klient.utils.misc.input.KeyAction;
import kult.legacy.klient.eventbus.EventHandler;
import net.minecraft.item.Items;

public class AirJump extends Module {
    private int level;

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> maintainLevel = sgGeneral.add(new BoolSetting.Builder()
        .name("maintain-level")
        .description("Maintains your current Y level when holding the jump key.")
        .defaultValue(false)
        .build()
    );

    public AirJump() {
        super(Categories.Movement, Items.BARRIER, "air-jump", "Lets you jump in the air.");
    }

    @Override
    public void onActivate() {
        level = mc.player.getBlockPos().getY();
    }

    @EventHandler
    private void onKey(KeyEvent event) {
        if (Modules.get().isActive(Freecam.class) || mc.currentScreen != null || mc.player.isOnGround()) return;

        if (event.action != KeyAction.Press) return;

        if (mc.options.keyJump.matchesKey(event.key, 0)) {
            level = mc.player.getBlockPos().getY();
            mc.player.jump();
        } else if (mc.options.keySneak.matchesKey(event.key, 0)) {
            level--;
        }
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        if (Modules.get().isActive(Freecam.class) || mc.player.isOnGround()) return;

        if (maintainLevel.get() && mc.player.getBlockPos().getY() == level && mc.options.keyJump.isPressed()) {
            mc.player.jump();
        }
    }
}
