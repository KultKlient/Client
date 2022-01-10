package kult.klient.systems.modules.player;

import kult.klient.systems.modules.Categories;
import kult.klient.systems.modules.Module;
import net.minecraft.item.Items;

public class LiquidInteract extends Module {
    public LiquidInteract() {
        super(Categories.Player, Items.WATER_BUCKET, "liquid-interact", "Allows you to interact with liquids.");
    }
}
