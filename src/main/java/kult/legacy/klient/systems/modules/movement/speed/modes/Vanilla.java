package kult.legacy.klient.systems.modules.movement.speed.modes;

import kult.legacy.klient.events.entity.player.PlayerMoveEvent;
import kult.legacy.klient.mixininterface.IVec3d;
import kult.legacy.klient.systems.modules.Modules;
import kult.legacy.klient.systems.modules.movement.Anchor;
import kult.legacy.klient.systems.modules.movement.speed.SpeedMode;
import kult.legacy.klient.systems.modules.movement.speed.SpeedModes;
import kult.legacy.klient.utils.player.PlayerUtils;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.math.Vec3d;

public class Vanilla extends SpeedMode {
    public Vanilla() {
        super(SpeedModes.Vanilla);
    }

    @Override
    public void onMove(PlayerMoveEvent event) {
        Vec3d vel = PlayerUtils.getHorizontalVelocity(settings.vanillaSpeed.get());
        double velX = vel.getX();
        double velZ = vel.getZ();

        if (mc.player.hasStatusEffect(StatusEffects.SPEED)) {
            double value = (mc.player.getStatusEffect(StatusEffects.SPEED).getAmplifier() + 1) * 0.205;
            velX += velX * value;
            velZ += velZ * value;
        }

        Anchor anchor = Modules.get().get(Anchor.class);
        if (anchor.isActive() && anchor.controlMovement) {
            velX = anchor.deltaX;
            velZ = anchor.deltaZ;
        }

        ((IVec3d) event.movement).set(velX, event.movement.y, velZ);
    }
}
