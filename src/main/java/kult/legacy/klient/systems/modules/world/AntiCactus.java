package kultklient.legacy.client.systems.modules.world;

import kultklient.legacy.client.systems.modules.Categories;
import kultklient.legacy.client.systems.modules.Module;
import net.minecraft.item.Items;

public class AntiCactus extends Module {
    public AntiCactus() {
        super(Categories.World, Items.CACTUS, "anti-cactus", "Prevents you from taking damage from cactus.");
    }
}
