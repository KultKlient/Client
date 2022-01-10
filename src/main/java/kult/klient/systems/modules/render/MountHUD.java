package kult.klient.systems.modules.render;

import kult.klient.systems.modules.Categories;
import kult.klient.systems.modules.Module;
import net.minecraft.item.Items;

public class MountHUD extends Module {
    public MountHUD() {
        super(Categories.Render, Items.DIAMOND_CHESTPLATE, "mount-hud", "Display xp bar and hunger when riding.");
    }
}
