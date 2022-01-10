package kult.klient.systems.modules.player;

import kult.klient.eventbus.EventHandler;
import kult.klient.eventbus.EventPriority;
import kult.klient.events.game.OpenScreenEvent;
import kult.klient.systems.modules.Categories;
import kult.klient.systems.modules.Module;
import kult.klient.systems.modules.Modules;
import kult.klient.systems.modules.render.WaypointsModule;
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
