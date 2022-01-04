package kult.legacy.klient.systems.modules.player;

import kult.legacy.klient.systems.modules.Categories;
import kult.legacy.klient.systems.modules.Module;
import net.minecraft.item.Items;

public class LiquidInteract extends Module {
    public LiquidInteract() {
        super(Categories.Player, Items.WATER_BUCKET, "liquid-interact", "Allows you to interact with liquids.");
    }
}
