package kult.klient.systems.modules.movement;

import kult.klient.eventbus.EventHandler;
import kult.klient.events.world.TickEvent;
import kult.klient.mixininterface.IVec3d;
import kult.klient.settings.BoolSetting;
import kult.klient.settings.DoubleSetting;
import kult.klient.settings.Setting;
import kult.klient.settings.SettingGroup;
import kult.klient.systems.modules.Categories;
import kult.klient.systems.modules.Module;
import kult.klient.systems.modules.Modules;
import kult.klient.systems.modules.world.Timer;
import net.minecraft.item.Items;

public class ReverseStep extends Module {
    private boolean valid = false;

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    // General

    private final Setting<Boolean> strict = sgGeneral.add(new BoolSetting.Builder()
        .name("strict")
        .description("Attempts to bypass strict anti-cheats.")
        .defaultValue(true)
        .build()
    );

    private final Setting<Boolean> timer = sgGeneral.add(new BoolSetting.Builder()
        .name("timer")
        .description("Speeds up the game while falling.")
        .defaultValue(false)
        .build()
    );

    private final Setting<Double> fallDistance = sgGeneral.add(new DoubleSetting.Builder()
        .name("fall-distance")
        .description("The maximum fall distance.")
        .defaultValue(3)
        .min(0)
        .sliderRange(0, 5)
        .build()
    );

    public ReverseStep() {
        super(Categories.Movement, Items.DIAMOND_BOOTS, "reverse-step", "Allows you to fall down blocks at a greater speed.");
    }

    @Override
    public void onActivate() {
        valid = false;
    }

    @Override
    public void onDeactivate() {
        Modules.get().get(Timer.class).setOverride(Timer.OFF);
    }

    @EventHandler
    public void onPostTick(TickEvent.Post event) {
        if (onGround()) Modules.get().get(Timer.class).setOverride(Timer.OFF);
        if (mc.world.isSpaceEmpty(mc.player.getBoundingBox().offset(0.0, (float) -(fallDistance.get() + 0.01), 0.0))) return;

        if (timer.get()) {
            if (!onGround()) {
                if (mc.player.getVelocity().y < 0 && valid) Modules.get().get(Timer.class).setOverride(strict.get() ? 2.5 : 5);
                else valid = false;
            } else if (onGround() && !(mc.player.isTouchingWater() || mc.player.isInLava())) {
                ((IVec3d) mc.player.getVelocity()).setY(-0.08);
                Modules.get().get(Timer.class).setOverride(Timer.OFF);
                valid = true;
            }
        } else if (mc.player.isOnGround() && !onGround() && !(mc.player.isTouchingWater() || mc.player.isInLava())) {
            ((IVec3d) mc.player.getVelocity()).setY(strict.get() ? -1 : -5);
            Modules.get().get(Timer.class).setOverride(Timer.OFF);
        }
    }

    private boolean onGround() {
        return mc.world.getBlockCollisions(mc.player, mc.player.getBoundingBox().offset(0, -0.05, 0)).iterator().hasNext();
    }
}
