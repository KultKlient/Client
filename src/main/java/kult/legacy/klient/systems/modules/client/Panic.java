package kult.legacy.klient.systems.modules.client;

import kult.legacy.klient.systems.modules.Categories;
import kult.legacy.klient.systems.modules.Module;
import kult.legacy.klient.systems.modules.Modules;
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
