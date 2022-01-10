package kult.klient.systems.modules.misc;

import kult.klient.eventbus.EventHandler;
import kult.klient.events.world.TickEvent;
import kult.klient.systems.modules.Categories;
import kult.klient.systems.modules.Module;
import kult.klient.systems.modules.Modules;
import kult.klient.systems.modules.world.Timer;
import kult.klient.utils.world.TickRate;
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
