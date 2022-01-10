package kult.klient.systems.modules.combat;

import kult.klient.systems.modules.Categories;
import kult.klient.systems.modules.Module;
import net.minecraft.item.Items;

public class AutoMinecart extends Module {
    public AutoMinecart() {
        super(Categories.Combat, Items.TNT_MINECART, "auto-minecart", "Automatically places and explodes tnt minecarts under other players.");
    }
}
