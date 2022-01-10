package kult.klient.systems.modules.render;

import kult.klient.systems.modules.Categories;
import kult.klient.systems.modules.Module;
import net.minecraft.item.Items;

public class NoBob extends Module {
    public NoBob() {
        super(Categories.Render, Items.COMMAND_BLOCK, "no-bob", "Disables hand animation.");
    }
}
