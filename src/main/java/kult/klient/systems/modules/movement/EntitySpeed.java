package kult.klient.systems.modules.movement;

import kult.klient.eventbus.EventHandler;
import kult.klient.events.entity.LivingEntityMoveEvent;
import kult.klient.mixininterface.IVec3d;
import kult.klient.settings.BoolSetting;
import kult.klient.settings.DoubleSetting;
import kult.klient.settings.Setting;
import kult.klient.settings.SettingGroup;
import kult.klient.systems.modules.Categories;
import kult.klient.systems.modules.Module;
import kult.klient.utils.player.PlayerUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Items;
import net.minecraft.util.math.Vec3d;

public class EntitySpeed extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    // General

    private final Setting<Double> speed = sgGeneral.add(new DoubleSetting.Builder()
        .name("speed")
        .description("Horizontal speed in blocks per second.")
        .defaultValue(10)
        .min(0)
        .sliderRange(0, 50)
        .build()
    );

    private final Setting<Boolean> onlyOnGround = sgGeneral.add(new BoolSetting.Builder()
        .name("only-on-ground")
        .description("Use speed only when standing on a block.")
        .defaultValue(false)
        .build()
    );

    private final Setting<Boolean> inWater = sgGeneral.add(new BoolSetting.Builder()
        .name("in-water")
        .description("Use speed when in water.")
        .defaultValue(false)
        .build()
    );

    public EntitySpeed() {
        super(Categories.Movement, Items.DIAMOND_HORSE_ARMOR, "entity-speed", "Makes you go faster when riding entities.");
    }

    @EventHandler
    private void onLivingEntityMove(LivingEntityMoveEvent event) {
        if (event.entity.getPrimaryPassenger() != mc.player) return;

        LivingEntity entity = event.entity;
        if (onlyOnGround.get() && !entity.isOnGround()) return;
        if (!inWater.get() && entity.isTouchingWater()) return;

        Vec3d vel = PlayerUtils.getHorizontalVelocity(speed.get());
        ((IVec3d) event.movement).setXZ(vel.x, vel.z);
    }
}
