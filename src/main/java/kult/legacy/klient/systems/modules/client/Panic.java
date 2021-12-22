package kultklient.legacy.client.systems.modules.client;

import kultklient.legacy.client.systems.modules.Categories;
import kultklient.legacy.client.systems.modules.Module;
import kultklient.legacy.client.systems.modules.Modules;
import net.minecraft.item.Items;

import java.util.ArrayList;

public class Panic extends Module {

    public Panic(){
        super(Categories.Client, Items.COMMAND_BLOCK, "panic", "Disables all active modules.");

        runInMainMenu = true;
    }

    @Override
    public void onActivate() {
        info("All modules disabled!");
        new ArrayList<>(Modules.get().getActive()).forEach(Module::toggle);
    }
}
