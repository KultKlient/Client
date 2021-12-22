package kultklient.legacy.client.systems.modules.render;

import kultklient.legacy.client.systems.modules.Categories;
import kultklient.legacy.client.systems.modules.Module;
import net.minecraft.item.Items;

public class UnfocusedCPU extends Module {
    public UnfocusedCPU() {
        super(Categories.Render, Items.COMMAND_BLOCK, "unfocused-cpu", "Will not render anything when your Minecraft window is not focused.");
    }
}
