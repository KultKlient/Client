package kultklient.legacy.client.systems.modules.misc;

import kultklient.legacy.client.eventbus.EventHandler;
import kultklient.legacy.client.events.entity.player.InteractEvent;
import kultklient.legacy.client.systems.modules.Categories;
import kultklient.legacy.client.systems.modules.Module;
import net.minecraft.item.Items;

public class MultiTask extends Module {

    public MultiTask() {
        super(Categories.Misc, Items.NETHERITE_PICKAXE, "multi-task", "Allows you to eat while mining a block.");
    }

    @EventHandler
    public void onInteractEvent(InteractEvent event) {
        event.usingItem = false;
    }
}
