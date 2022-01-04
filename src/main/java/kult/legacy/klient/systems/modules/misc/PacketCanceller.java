package kult.legacy.klient.systems.modules.misc;

import kult.legacy.klient.events.packets.PacketEvent;
import kult.legacy.klient.settings.PacketListSetting;
import kult.legacy.klient.settings.Setting;
import kult.legacy.klient.settings.SettingGroup;
import kult.legacy.klient.systems.modules.Module;
import kult.legacy.klient.systems.modules.Categories;
import kult.legacy.klient.utils.network.PacketUtils;
import kult.legacy.klient.eventbus.EventHandler;
import kult.legacy.klient.eventbus.EventPriority;
import net.minecraft.item.Items;
import net.minecraft.network.Packet;

import java.util.Set;

public class PacketCanceller extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Set<Class<? extends Packet<?>>>> s2cPackets = sgGeneral.add(new PacketListSetting.Builder()
        .name("S2C-packets")
        .description("Server-to-client packets to cancel.")
        .filter(aClass -> PacketUtils.getS2CPackets().contains(aClass))
        .build()
    );

    private final Setting<Set<Class<? extends Packet<?>>>> c2sPackets = sgGeneral.add(new PacketListSetting.Builder()
        .name("C2S-packets")
        .description("Client-to-server packets to cancel.")
        .filter(aClass -> PacketUtils.getC2SPackets().contains(aClass))
        .build()
    );

    public PacketCanceller() {
        super(Categories.Misc, Items.COMMAND_BLOCK, "packet-canceller", "Allows you to cancel certain packets.");
    }

    @EventHandler(priority = EventPriority.HIGHEST + 1)
    private void onReceivePacket(PacketEvent.Receive event) {
        if (s2cPackets.get().contains(event.packet.getClass())) event.cancel();
    }

    @EventHandler(priority = EventPriority.HIGHEST + 1)
    private void onSendPacket(PacketEvent.Send event) {
        if (c2sPackets.get().contains(event.packet.getClass())) event.cancel();
    }
}
