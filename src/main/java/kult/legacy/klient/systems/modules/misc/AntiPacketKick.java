package kult.legacy.klient.systems.modules.misc;

import kult.legacy.klient.systems.modules.Categories;
import kult.legacy.klient.systems.modules.Module;
import net.minecraft.item.Items;

public class AntiPacketKick extends Module {
    public AntiPacketKick() {
        super(Categories.Misc, Items.COMPARATOR, "anti-packet-kick", "Attempts to prevent you from being disconnected by large packets.");
    }
}
