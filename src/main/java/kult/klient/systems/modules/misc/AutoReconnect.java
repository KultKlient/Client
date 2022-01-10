package kult.klient.systems.modules.misc;

import kult.klient.settings.DoubleSetting;
import kult.klient.settings.Setting;
import kult.klient.settings.SettingGroup;
import kult.klient.systems.modules.Categories;
import kult.klient.systems.modules.Module;
import net.minecraft.item.Items;

public class AutoReconnect extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    // General

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
