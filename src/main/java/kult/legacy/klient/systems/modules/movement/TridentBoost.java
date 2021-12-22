package kultklient.legacy.client.systems.modules.movement;

import kultklient.legacy.client.settings.BoolSetting;
import kultklient.legacy.client.settings.DoubleSetting;
import kultklient.legacy.client.settings.Setting;
import kultklient.legacy.client.settings.SettingGroup;
import kultklient.legacy.client.systems.modules.Categories;
import kultklient.legacy.client.systems.modules.Module;
import net.minecraft.item.Items;

public class TridentBoost extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Double> multiplier = sgGeneral.add(new DoubleSetting.Builder()
        .name("boost")
        .description("How much your velocity is multiplied by when using riptide.")
        .defaultValue(2)
        .min(0.1)
        .sliderMin(1)
        .build()
    );

    private final Setting<Boolean> allowOutOfWater = sgGeneral.add(new BoolSetting.Builder()
        .name("out-of-water")
        .description("Whether riptide should work out of water")
        .defaultValue(true)
        .build()
    );

    public TridentBoost() {
        super(Categories.Movement, Items.TRIDENT, "trident-boost", "Boosts you when using riptide with a trident.");
    }

    public double getMultiplier() {
        return isActive() ? multiplier.get() : 1;
    }

    public boolean allowOutOfWater() {
        return isActive() ? allowOutOfWater.get() : false;
    }
}
