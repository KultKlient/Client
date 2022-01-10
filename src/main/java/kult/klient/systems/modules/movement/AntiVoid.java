package kult.klient.systems.modules.movement;

import kult.klient.events.world.TickEvent;
import kult.klient.settings.EnumSetting;
import kult.klient.settings.Setting;
import kult.klient.settings.SettingGroup;
import kult.klient.systems.modules.Categories;
import kult.klient.systems.modules.Module;
import kult.klient.systems.modules.Modules;
import kult.klient.utils.Utils;
import kult.klient.eventbus.EventHandler;
import net.minecraft.item.Items;

public class AntiVoid extends Module {
    private boolean wasFlightEnabled, hasRun;

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    // General

    private final Setting<Mode> mode = sgGeneral.add(new EnumSetting.Builder<Mode>()
        .name("mode")
        .description("The method to prevent you from falling into the void.")
        .defaultValue(Mode.Jump)
        .onChanged(a -> onActivate())
        .build()
    );

    public AntiVoid() {
        super(Categories.Movement, Items.BARRIER, "anti-void", "Attempts to prevent you from falling into the void.");
    }

    @Override
    public void onActivate() {
        if (mode.get() == Mode.Flight) wasFlightEnabled = Modules.get().isActive(Flight.class);
    }

    @Override
    public void onDeactivate() {
        if (!wasFlightEnabled && mode.get() == Mode.Flight && Utils.canUpdate() && Modules.get().isActive(Flight.class)) Modules.get().get(Flight.class).toggle();
    }

    @EventHandler
    private void onPreTick(TickEvent.Pre event) {
        int minY = mc.world.getBottomY();

        if (mc.player.getY() > minY || mc.player.getY() < minY - 15) {
            if (hasRun && mode.get() == Mode.Flight && Modules.get().isActive(Flight.class)) {
                Modules.get().get(Flight.class).toggle();
                hasRun = false;
            }

            return;
        }

        switch (mode.get()) {
            case Flight -> {
                if (!Modules.get().isActive(Flight.class)) Modules.get().get(Flight.class).toggle();
                hasRun = true;
            }
            case Jump -> mc.player.jump();
        }
    }

    public enum Mode {
        Flight("Flight"),
        Jump("Jump");

        private final String title;

        Mode(String title) {
            this.title = title;
        }

        @Override
        public String toString() {
            return title;
        }
    }
}
