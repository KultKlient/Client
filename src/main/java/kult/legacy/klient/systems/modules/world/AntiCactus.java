package kult.legacy.klient.systems.modules.world;

import kult.legacy.klient.systems.modules.Categories;
import kult.legacy.klient.systems.modules.Module;
import net.minecraft.item.Items;

public class AntiCactus extends Module {
    public AntiCactus() {
        super(Categories.World, Items.CACTUS, "anti-cactus", "Prevents you from taking damage from cactus.");
    }
}
