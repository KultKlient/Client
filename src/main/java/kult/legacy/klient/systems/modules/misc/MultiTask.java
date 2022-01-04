package kult.legacy.klient.systems.modules.misc;

import kult.legacy.klient.eventbus.EventHandler;
import kult.legacy.klient.events.entity.player.InteractEvent;
import kult.legacy.klient.systems.modules.Categories;
import kult.legacy.klient.systems.modules.Module;
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
