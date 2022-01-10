package kult.klient.systems.modules.client;

import kult.klient.eventbus.EventHandler;
import kult.klient.events.world.TickEvent;
import kult.klient.gui.GuiTheme;
import kult.klient.gui.widgets.WWidget;
import kult.klient.gui.widgets.containers.WHorizontalList;
import kult.klient.gui.widgets.pressable.WButton;
import kult.klient.settings.BoolSetting;
import kult.klient.settings.Setting;
import kult.klient.settings.SettingGroup;
import kult.klient.systems.modules.Categories;
import kult.klient.systems.modules.Module;
import kult.klient.utils.network.Capes;
import net.minecraft.item.Items;

public class CapesModule extends Module {
    private int timer = 0;

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    // General

    private final Setting<Boolean> autoReload = sgGeneral.add(new BoolSetting.Builder()
        .name("auto-reload")
        .description("Automatically reloads the capes every 10 minutes.")
        .defaultValue(true)
        .build()
    );

    // Buttons

    @Override
    public WWidget getWidget(GuiTheme theme) {
        WHorizontalList w = theme.horizontalList();

        WButton reload = w.add(theme.button("Reload")).widget();
        reload.action = () -> {
            if (isActive()) Capes.init();
        };
        w.add(theme.label("Reloads the capes."));

        return w;
    }

    public CapesModule() {
        super(Categories.Client, Items.CAKE, "capes", "Shows very cool capes on users which have them.");
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        if (!autoReload.get()) {
            timer = 0;
            return;
        }

        if (timer > 12000) {
            timer = 0;
            Capes.init();
        }

        timer++;
    }

    @Override
    public void onActivate() {
        Capes.init();
    }

    @Override
    public void onDeactivate() {
        Capes.disable();
    }
}
