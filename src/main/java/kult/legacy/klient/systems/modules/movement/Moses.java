package kult.legacy.klient.systems.modules.movement;

import kult.legacy.klient.settings.BoolSetting;
import kult.legacy.klient.settings.Setting;
import kult.legacy.klient.settings.SettingGroup;
import kult.legacy.klient.systems.modules.Categories;
import kult.legacy.klient.systems.modules.Module;
import net.minecraft.item.Items;

public class Moses extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    // General

    public final Setting<Boolean> lava = sgGeneral.add(new BoolSetting.Builder().name("lava").description("Applies to lava too.").defaultValue(false).build());

    public Moses() {
        super(Categories.Movement, Items.ACACIA_LEAVES, "moses", "Lets you walk through water as if it was air.");
    }
}
