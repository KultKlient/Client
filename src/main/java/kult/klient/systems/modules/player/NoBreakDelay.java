package kult.klient.systems.modules.player;

import kult.klient.systems.modules.Categories;
import kult.klient.systems.modules.Module;
import net.minecraft.item.Items;

public class NoBreakDelay extends Module {
    public NoBreakDelay() {
        super(Categories.Player, Items.STONE, "no-break-delay", "Completely removes the delay between breaking blocks.");
    }
}
