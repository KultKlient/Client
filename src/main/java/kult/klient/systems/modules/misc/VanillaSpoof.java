package kult.klient.systems.modules.misc;

import io.netty.buffer.Unpooled;
import kult.klient.KultKlient;
import kult.klient.eventbus.EventHandler;
import kult.klient.events.packets.PacketEvent;
import kult.klient.mixin.CustomPayloadC2SPacketAccessor;
import kult.klient.systems.modules.Categories;
import kult.klient.systems.modules.Module;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;

public class VanillaSpoof extends Module {
    public VanillaSpoof() {
        super(Categories.Misc, Items.COMMAND_BLOCK, "vanilla-spoof", "When connecting to a server it spoofs the client name to be 'vanilla'.");

        KultKlient.EVENT_BUS.subscribe(new Listener());
    }

    private class Listener {
        @EventHandler
        private void onPacketSend(PacketEvent.Send event) {
            if (!isActive() || !(event.packet instanceof CustomPayloadC2SPacket)) return;
            CustomPayloadC2SPacketAccessor packet = (CustomPayloadC2SPacketAccessor) event.packet;
            if (packet.getChannel().equals(CustomPayloadC2SPacket.BRAND)) packet.setData(new PacketByteBuf(Unpooled.buffer()).writeString("vanilla"));
            else if (StringUtils.containsIgnoreCase(packet.getData().toString(StandardCharsets.UTF_8), "fabric")) event.cancel();
        }
    }
}
