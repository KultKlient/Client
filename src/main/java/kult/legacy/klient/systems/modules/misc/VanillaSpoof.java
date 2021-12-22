package kultklient.legacy.client.systems.modules.misc;

import io.netty.buffer.Unpooled;
import kultklient.legacy.client.KultKlientLegacy;
import kultklient.legacy.client.events.packets.PacketEvent;
import kultklient.legacy.client.mixin.ICustomPayloadC2SPacketAccessor;
import kultklient.legacy.client.systems.modules.Categories;
import kultklient.legacy.client.systems.modules.Module;
import kultklient.legacy.client.eventbus.EventHandler;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;

import java.nio.charset.StandardCharsets;

public class VanillaSpoof extends Module {
    public VanillaSpoof() {
        super(Categories.Misc, Items.COMMAND_BLOCK, "vanilla-spoof", "When connecting to a server it spoofs the client name to be 'vanilla'.");

        KultKlientLegacy.EVENT_BUS.subscribe(new Listener());
    }

    private class Listener {
        @EventHandler
        private void onPacketSend(PacketEvent.Send event) {
            if (!isActive()) return;
            if (event.packet instanceof CustomPayloadC2SPacket packet) {
                ICustomPayloadC2SPacketAccessor accessor = (ICustomPayloadC2SPacketAccessor) packet;
                if (accessor.getChannel().equals(CustomPayloadC2SPacket.BRAND)) accessor.setData(new PacketByteBuf(Unpooled.buffer()).writeString("vanilla"));
                else if (accessor.getData().toString(StandardCharsets.UTF_8).toLowerCase().contains("fabric")) event.setCancelled(true);
            }
        }
    }
}
