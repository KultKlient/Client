package kult.klient.systems.modules.client;

import kult.klient.systems.modules.Categories;
import kult.klient.systems.modules.Module;
import kult.klient.systems.modules.Modules;
import net.minecraft.item.Items;

import java.util.ArrayList;

public class Panic extends Module {

    public Panic(){
        super(Categories.Client, Items.COMMAND_BLOCK, "panic", "Disables all active modules.", true);
    }

    @Override
    public void onActivate() {
        info("All modules disabled!");
        new ArrayList<>(Modules.get().getActive()).forEach(Module::toggle);
    }
}
