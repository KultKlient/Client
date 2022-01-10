package kult.klient.systems.modules.movement;

import kult.klient.settings.EnumSetting;
import kult.klient.settings.Setting;
import kult.klient.settings.SettingGroup;
import kult.klient.systems.modules.Categories;
import kult.klient.systems.modules.Module;
import kult.klient.systems.modules.Modules;
import kult.klient.systems.modules.render.Freecam;
import net.minecraft.item.Items;

public class Sneak extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    // General

    private final Setting<Mode> mode = sgGeneral.add(new EnumSetting.Builder<Mode>()
        .name("mode")
        .description("Which method to sneak.")
        .defaultValue(Mode.Vanilla)
        .build()
    );

    public Sneak() {
        super(Categories.Movement, Items.LEATHER_BOOTS, "sneak", "Makes you sneak automatically.");
    }

    public boolean doPacket() {
        return isActive() && !Modules.get().isActive(Freecam.class) && mode.get() == Mode.Packet;
    }

    public boolean doVanilla() {
        return isActive() && !Modules.get().isActive(Freecam.class) && mode.get() == Mode.Vanilla;
    }

    public enum Mode {
        Packet("Packet"),
        Vanilla("Vanilla");

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
