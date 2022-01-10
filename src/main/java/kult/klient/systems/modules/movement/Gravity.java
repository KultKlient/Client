package kult.klient.systems.modules.movement;

import kult.klient.eventbus.EventHandler;
import kult.klient.events.world.TickEvent;
import kult.klient.mixininterface.IVec3d;
import kult.klient.systems.modules.Categories;
import kult.klient.systems.modules.Module;
import net.minecraft.item.Items;
import net.minecraft.util.math.Vec3d;

public class Gravity extends Module {
    public Gravity() {
        super(Categories.Movement, Items.FEATHER, "gravity", "Changes gravity to moon gravity.");
    }

    @EventHandler
    private void onTick(final TickEvent.Post event) {
        if (mc.options.keySneak.isPressed()) return;
        Vec3d velocity = mc.player.getVelocity();
        ((IVec3d) velocity).set(velocity.x, velocity.y + 0.0568000030517578, velocity.z);
    }
}
