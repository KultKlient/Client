package kult.klient.systems.modules.misc;

import kult.klient.eventbus.EventHandler;
import kult.klient.events.entity.player.InteractEvent;
import kult.klient.systems.modules.Categories;
import kult.klient.systems.modules.Module;
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
