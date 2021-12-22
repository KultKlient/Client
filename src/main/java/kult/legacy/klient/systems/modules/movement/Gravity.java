package kultklient.legacy.client.systems.modules.movement;

import kultklient.legacy.client.eventbus.EventHandler;
import kultklient.legacy.client.events.world.TickEvent;
import kultklient.legacy.client.mixininterface.IVec3d;
import kultklient.legacy.client.systems.modules.Categories;
import kultklient.legacy.client.systems.modules.Module;
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
