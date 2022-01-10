package kult.klient.systems.modules.misc;

import kult.klient.eventbus.EventHandler;
import kult.klient.events.packets.PacketEvent;
import kult.klient.events.world.TickEvent;
import kult.klient.settings.ModuleListSetting;
import kult.klient.settings.Setting;
import kult.klient.settings.SettingGroup;
import kult.klient.systems.modules.Categories;
import kult.klient.systems.modules.Module;
import kult.klient.systems.modules.Modules;
import kult.klient.systems.modules.movement.PacketFly;
import kult.klient.systems.modules.movement.Phase;
import kult.klient.systems.modules.movement.Step;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.TeleportConfirmC2SPacket;

import java.util.ArrayList;
import java.util.List;

public class AntiDesync extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    // General

    private final Setting<List<Module>> modules = sgGeneral.add(new ModuleListSetting.Builder()
        .name("modules")
        .description("Determines which modules to ignore.")
        .defaultValue(
            PacketFly.class,
            Phase.class,
            Step.class
        )
        .build()
    );

    private ArrayList<Integer> teleportIDs;

    public AntiDesync() {
        super(Categories.Misc, Items.COMMAND_BLOCK, "anti-desync", "Stops you from desyncing with the server.");
    }

    @Override
    public void onActivate() {
        teleportIDs = new ArrayList<>();
    }

    @EventHandler
    private void onSentPacket(PacketEvent.Send event) {
        if (checkModules() && event.packet instanceof TeleportConfirmC2SPacket packet) teleportIDs.add(packet.getTeleportId());
    }

    @EventHandler
    private void onPreTick(TickEvent.Pre event) {
        if (!teleportIDs.isEmpty() && checkModules()) {
            mc.getNetworkHandler().sendPacket(new TeleportConfirmC2SPacket(teleportIDs.get(0)));
            teleportIDs.remove(0);
        }
    }

    private boolean checkModules() {
        List<Module> all = Modules.get().getList();

        for (Module module : modules.get()) {
            if (all.contains(module) && module.isActive()) return false;
        }

        return true;
    }
}
