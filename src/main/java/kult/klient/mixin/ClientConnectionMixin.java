package kult.klient.mixin;

import io.netty.channel.ChannelHandlerContext;
import kult.klient.KultKlient;
import kult.klient.events.packets.PacketEvent;
import kult.klient.events.world.ConnectToServerEvent;
import kult.klient.systems.modules.Modules;
import kult.klient.systems.modules.misc.AntiPacketKick;
import kult.klient.systems.modules.world.HighwayBuilder;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.IOException;
import java.net.InetSocketAddress;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {
    @Inject(method = "handlePacket", at = @At("HEAD"), cancellable = true)
    private static <T extends PacketListener> void onHandlePacket(Packet<T> packet, PacketListener listener, CallbackInfo info) {
        if (KultKlient.EVENT_BUS.post(PacketEvent.Receive.get(packet)).isCancelled()) info.cancel();
    }

    @Inject(method = "disconnect", at = @At("HEAD"))
    private void disconnect(Text disconnectReason, CallbackInfo info) {
        if (Modules.get().get(HighwayBuilder.class).isActive()) {
            MutableText text = new LiteralText(String.format("\n\n%s[%sHighway Builder%s] Statistics:", Formatting.GRAY, Formatting.BLUE, Formatting.GRAY)).append("\n");
            text.append(Modules.get().get(HighwayBuilder.class).getStatsText());

            ((MutableText) disconnectReason).append(text);
        }
    }

    @Inject(method = "connect", at = @At("HEAD"))
    private static void onConnect(InetSocketAddress address, boolean useEpoll, CallbackInfoReturnable<ClientConnection> info) {
        KultKlient.EVENT_BUS.post(ConnectToServerEvent.get());
    }

    @Inject(method = "exceptionCaught", at = @At("HEAD"), cancellable = true)
    private void exceptionCaught(ChannelHandlerContext context, Throwable throwable, CallbackInfo info) {
        if (throwable instanceof IOException && Modules.get().isActive(AntiPacketKick.class)) info.cancel();
    }

    @Inject(at = @At("HEAD"), method = "send(Lnet/minecraft/network/Packet;)V", cancellable = true)
    private void onSendPacketHead(Packet<?> packet, CallbackInfo info) {
        if (KultKlient.EVENT_BUS.post(PacketEvent.Send.get(packet)).isCancelled()) info.cancel();
    }

    @Inject(method = "send(Lnet/minecraft/network/Packet;)V", at = @At("TAIL"))
    private void onSendPacketTail(Packet<?> packet, CallbackInfo info) {
        KultKlient.EVENT_BUS.post(PacketEvent.Sent.get(packet));
    }
}
