package kult.klient.systems.modules.movement;

import kult.klient.settings.BoolSetting;
import kult.klient.settings.Setting;
import kult.klient.settings.SettingGroup;
import kult.klient.systems.modules.Categories;
import kult.klient.systems.modules.Module;
import net.minecraft.item.Items;

public class AntiLevitation extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    // General

    private final Setting<Boolean> applyGravity = sgGeneral.add(new BoolSetting.Builder()
        .name("gravity")
        .description("Applies gravity.")
        .defaultValue(false)
        .build()
    );

    public AntiLevitation() {
        super(Categories.Movement, Items.ANVIL, "anti-levitation", "Prevents the levitation effect from working.");
    }

    public boolean isApplyGravity() {
        return applyGravity.get();
    }
}
