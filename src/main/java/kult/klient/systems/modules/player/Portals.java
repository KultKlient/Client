package kult.klient.systems.modules.player;

import kult.klient.systems.modules.Categories;
import kult.klient.systems.modules.Module;
import net.minecraft.item.Items;

public class Portals extends Module {
    public Portals() {
        super(Categories.Player, Items.OBSIDIAN, "portals", "Allows you to use GUIs normally while in a Nether Portal.");
    }
}
