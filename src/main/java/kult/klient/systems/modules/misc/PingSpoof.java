package kult.klient.systems.modules.misc;

import kult.klient.eventbus.EventHandler;
import kult.klient.events.packets.PacketEvent;
import kult.klient.events.render.Render3DEvent;
import kult.klient.settings.IntSetting;
import kult.klient.settings.Setting;
import kult.klient.settings.SettingGroup;
import kult.klient.systems.modules.Categories;
import kult.klient.systems.modules.Module;
import kult.klient.utils.misc.Timer;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.KeepAliveC2SPacket;

public class PingSpoof extends Module {
    private final Timer timer = new Timer();

    private KeepAliveC2SPacket packet;

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    // General

    private final Setting<Integer> ping = sgGeneral.add(new IntSetting.Builder()
        .name("ping")
        .description("The Ping to set.")
        .defaultValue(200)
        .sliderRange(0, 1000)
        .build()
    );

    public PingSpoof() {
        super(Categories.Misc, Items.COMMAND_BLOCK, "ping-spoof", "Modifies your ping.");
    }

    @EventHandler
    public void onPacketSend(PacketEvent.Send event) {
        if (event.packet instanceof KeepAliveC2SPacket && packet != event.packet && ping.get() != 0) {
            packet = (KeepAliveC2SPacket) event.packet;
            event.cancel();
            timer.reset();
        }
    }

    @EventHandler
    public void onRender3D(Render3DEvent event) {
        if (timer.passedMillis(ping.get()) && packet != null) {
            mc.getNetworkHandler().sendPacket(packet);
            packet = null;
        }
    }

    @Override
    public String getInfoString() {
        return ping.get() + "ms";
    }
}
