package kult.legacy.klient.systems.modules.render;

import kult.legacy.klient.systems.modules.Categories;
import kult.legacy.klient.systems.modules.Module;
import net.minecraft.item.Items;

public class NoBob extends Module {
    public NoBob() {
        super(Categories.Render, Items.COMMAND_BLOCK, "no-bob", "Disables hand animation.");
    }
}
