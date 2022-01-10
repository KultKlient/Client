package kult.klient.systems.modules.render;

import kult.klient.systems.modules.Categories;
import kult.klient.systems.modules.Module;
import net.minecraft.item.Items;

public class UnfocusedCPU extends Module {
    public UnfocusedCPU() {
        super(Categories.Render, Items.COMMAND_BLOCK, "unfocused-cpu", "Will not render anything when your Minecraft window is not focused.");
    }
}
