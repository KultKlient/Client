package kult.klient.systems.modules.misc;

import kult.klient.eventbus.EventHandler;
import kult.klient.events.world.TickEvent;
import kult.klient.settings.IntSetting;
import kult.klient.settings.Setting;
import kult.klient.settings.SettingGroup;
import kult.klient.systems.modules.Categories;
import kult.klient.systems.modules.Module;
import kult.klient.systems.modules.Modules;
import kult.klient.systems.modules.render.Freecam;
import net.minecraft.item.Items;

public class Twerk extends Module {
    private boolean hasTwerked = false;

    private int timer;

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    // General

    private final Setting<Integer> speed = sgGeneral.add(new IntSetting.Builder()
        .name("speed")
        .description("The speed of twerking.")
        .defaultValue(1)
        .min(1)
        .sliderRange(1, 100)
        .build()
    );

    public Twerk() {
        super(Categories.Misc, Items.DRIED_KELP, "twerk", "Makes you twerk like Miley Cyrus.");
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        timer++;
        if (timer < 10 - speed.get()) return;

        hasTwerked = !hasTwerked;

        timer = -1;
    }

    public boolean doVanilla() {
        return hasTwerked && !Modules.get().isActive(Freecam.class);
    }

    @Override
    public void onActivate() {
        timer = 0;
    }

    @Override
    public void onDeactivate() {
        hasTwerked = false;
    }
}
