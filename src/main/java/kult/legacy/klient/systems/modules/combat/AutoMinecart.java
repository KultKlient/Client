package kultklient.legacy.client.systems.modules.combat;

import kultklient.legacy.client.systems.modules.Categories;
import kultklient.legacy.client.systems.modules.Module;
import net.minecraft.item.Items;

public class AutoMinecart extends Module {
    public AutoMinecart() {
        super(Categories.Combat, Items.TNT_MINECART, "auto-minecart", "Automatically places and explodes tnt minecarts under other players.");
    }
}
