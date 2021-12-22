package kultklient.legacy.client.systems.modules.player;

import kultklient.legacy.client.events.game.OpenScreenEvent;
import kultklient.legacy.client.systems.modules.render.WaypointsModule;
import kultklient.legacy.client.systems.modules.Categories;
import kultklient.legacy.client.systems.modules.Module;
import kultklient.legacy.client.systems.modules.Modules;
import kultklient.legacy.client.eventbus.EventHandler;
import kultklient.legacy.client.eventbus.EventPriority;
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
