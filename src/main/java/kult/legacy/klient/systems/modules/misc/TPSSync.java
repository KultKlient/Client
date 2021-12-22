package kultklient.legacy.client.systems.modules.misc;

import kultklient.legacy.client.events.world.TickEvent;
import kultklient.legacy.client.systems.modules.Module;
import kultklient.legacy.client.systems.modules.world.Timer;
import kultklient.legacy.client.systems.modules.Categories;
import kultklient.legacy.client.systems.modules.Modules;
import kultklient.legacy.client.utils.world.TickRate;
import kultklient.legacy.client.eventbus.EventHandler;
import net.minecraft.item.Items;

public class TPSSync extends Module {
    public TPSSync() {
        super(Categories.Misc, Items.COMMAND_BLOCK, "tps-sync", "Syncs the clients TPS with the server TPS.");
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        Modules.get().get(Timer.class).setOverride((TickRate.INSTANCE.getTickRate() >= 1 ? TickRate.INSTANCE.getTickRate() : 1) / 20);
    }

    @Override
    public void onDeactivate() {
        Modules.get().get(Timer.class).setOverride(Timer.OFF);
    }
}
