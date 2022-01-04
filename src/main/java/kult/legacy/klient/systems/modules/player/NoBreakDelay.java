package kult.legacy.klient.systems.modules.player;

import kult.legacy.klient.systems.modules.Categories;
import kult.legacy.klient.systems.modules.Module;
import net.minecraft.item.Items;

public class NoBreakDelay extends Module {
    public NoBreakDelay() {
        super(Categories.Player, Items.STONE, "no-break-delay", "Completely removes the delay between breaking blocks.");
    }
}
