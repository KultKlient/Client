package kultklient.legacy.client.systems.modules.misc;

import kultklient.legacy.client.settings.DoubleSetting;
import kultklient.legacy.client.settings.Setting;
import kultklient.legacy.client.settings.SettingGroup;
import kultklient.legacy.client.systems.modules.Categories;
import kultklient.legacy.client.systems.modules.Module;
import net.minecraft.item.Items;

public class AutoReconnect extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    public final Setting<Double> time = sgGeneral.add(new DoubleSetting.Builder()
        .name("delay")
        .description("The amount of seconds to wait before reconnecting to the server.")
        .defaultValue(5)
        .min(0)
        .sliderRange(0, 15)
        .decimalPlaces(1)
        .build()
    );

    public AutoReconnect() {
        super(Categories.Misc, Items.REPEATER, "auto-reconnect", "Automatically reconnects when disconnected from a server.");
    }
}
