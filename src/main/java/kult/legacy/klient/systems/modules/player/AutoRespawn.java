package kult.legacy.klient.systems.modules.player;

import kult.legacy.klient.events.game.OpenScreenEvent;
import kult.legacy.klient.systems.modules.render.WaypointsModule;
import kult.legacy.klient.systems.modules.Categories;
import kult.legacy.klient.systems.modules.Module;
import kult.legacy.klient.systems.modules.Modules;
import kult.legacy.klient.eventbus.EventHandler;
import kult.legacy.klient.eventbus.EventPriority;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.item.Items;

public class AutoRespawn extends Module {
    public AutoRespawn() {
        super(Categories.Player, Items.COMMAND_BLOCK, "auto-respawn", "Automatically respawns after death.");
    }

    @EventHandler(priority = EventPriority.HIGH)
    private void onOpenScreenEvent(OpenScreenEvent event) {
        if (!(event.screen instanceof DeathScreen)) return;

        Modules.get().get(WaypointsModule.class).addDeath(mc.player.getPos());
        mc.player.requestRespawn();
        event.cancel();
    }
}
