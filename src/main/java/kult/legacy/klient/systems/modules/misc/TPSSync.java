package kult.legacy.klient.systems.modules.misc;

import kult.legacy.klient.events.world.TickEvent;
import kult.legacy.klient.systems.modules.Module;
import kult.legacy.klient.systems.modules.world.Timer;
import kult.legacy.klient.systems.modules.Categories;
import kult.legacy.klient.systems.modules.Modules;
import kult.legacy.klient.utils.world.TickRate;
import kult.legacy.klient.eventbus.EventHandler;
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
