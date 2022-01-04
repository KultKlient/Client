package kult.legacy.klient.systems.modules.movement;

import kult.legacy.klient.settings.EnumSetting;
import kult.legacy.klient.settings.Setting;
import kult.legacy.klient.settings.SettingGroup;
import kult.legacy.klient.systems.modules.Categories;
import kult.legacy.klient.systems.modules.Module;
import kult.legacy.klient.systems.modules.Modules;
import kult.legacy.klient.systems.modules.render.Freecam;
import net.minecraft.item.Items;

public class Sneak extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

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
        Packet,
        Vanilla
    }
}
