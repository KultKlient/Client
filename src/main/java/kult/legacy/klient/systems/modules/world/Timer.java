package kult.legacy.klient.systems.modules.world;

import kult.legacy.klient.gui.GuiTheme;
import kult.legacy.klient.gui.widgets.WWidget;
import kult.legacy.klient.gui.widgets.containers.WHorizontalList;
import kult.legacy.klient.gui.widgets.pressable.WButton;
import kult.legacy.klient.settings.DoubleSetting;
import kult.legacy.klient.settings.Setting;
import kult.legacy.klient.settings.SettingGroup;
import kult.legacy.klient.systems.modules.Categories;
import kult.legacy.klient.systems.modules.Module;
import kult.legacy.klient.systems.modules.Modules;
import kult.legacy.klient.systems.modules.misc.TPSSync;
import net.minecraft.item.Items;

public class Timer extends Module {
    public static final double OFF = 1;
    private double override = 1;

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Double> multiplier = sgGeneral.add(new DoubleSetting.Builder()
        .name("multiplier")
        .description("The timer multiplier amount.")
        .defaultValue(1)
        .min(0.1)
        .sliderRange(0.1, 2)
        .build()
    );

    // Buttons

    @Override
    public WWidget getWidget(GuiTheme theme) {
        if (Modules.get().get(TPSSync.class).isActive()) {
            WHorizontalList list = theme.horizontalList();
            list.add(theme.label("Multiplier is overwritten by TPSSync."));
            WButton disableBtn = list.add(theme.button("Disable TPSSync")).widget();
            disableBtn.action = () -> {
                TPSSync tpsSync = Modules.get().get(TPSSync.class);
                if (tpsSync.isActive()) tpsSync.toggle();
                list.visible = false;
            };
            return list;
        }

        return null;
    }

    public Timer() {
        super(Categories.World, Items.CLOCK, "timer", "Changes the speed of everything in your game.");
    }

    public double getMultiplier() {
        return override != OFF ? override : (isActive() ? multiplier.get() : OFF);
    }

    public void setOverride(double override) {
        this.override = override;
    }
}
