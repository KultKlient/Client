package kult.legacy.klient.systems.modules.render;

import kult.legacy.klient.systems.modules.Categories;
import kult.legacy.klient.systems.modules.Module;
import net.minecraft.item.Items;

public class InstantSneak extends Module {
    public InstantSneak() {
        super(Categories.Render, Items.COMMAND_BLOCK, "instant-sneak", "Removes sneak animation making it look like in older versions.");
    }
}
