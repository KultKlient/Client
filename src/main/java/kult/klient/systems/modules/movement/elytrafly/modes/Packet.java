package kult.klient.systems.modules.movement.elytrafly.modes;

import kult.klient.KultKlient;
import kult.klient.events.packets.PacketEvent;
import kult.klient.systems.modules.movement.elytrafly.ElytraFlightMode;
import kult.klient.systems.modules.movement.elytrafly.ElytraFlightModes;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;

public class Packet extends ElytraFlightMode {
    private final Vec3d vec3d = new Vec3d(0,0,0);

    public Packet() {
        super(ElytraFlightModes.Packet);
    }

    @Override
    public void onDeactivate() {
        KultKlient.mc.player.getAbilities().flying = false;
        KultKlient.mc.player.getAbilities().allowFlying = false;
    }

    @Override
    public void onTick() {
        super.onTick();

        if (KultKlient.mc.player.getInventory().getArmorStack(2).getItem() != Items.ELYTRA || KultKlient.mc.player.fallDistance <= 0.2 || KultKlient.mc.options.keySneak.isPressed()) return;

        if (KultKlient.mc.options.keyForward.isPressed()) {
            vec3d.add(0, 0, elytraFly.horizontalSpeed.get());
            vec3d.rotateY(-(float) Math.toRadians(KultKlient.mc.player.getYaw()));
        } else if (KultKlient.mc.options.keyBack.isPressed()) {
            vec3d.add(0, 0, elytraFly.horizontalSpeed.get());
            vec3d.rotateY((float) Math.toRadians(KultKlient.mc.player.getYaw()));
        }

        if (KultKlient.mc.options.keyJump.isPressed()) vec3d.add(0, elytraFly.verticalSpeed.get(), 0);
        else if (!KultKlient.mc.options.keyJump.isPressed()) vec3d.add(0, -elytraFly.verticalSpeed.get(), 0);

        KultKlient.mc.player.setVelocity(vec3d);
        KultKlient.mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(KultKlient.mc.player, ClientCommandC2SPacket.Mode.START_FALL_FLYING));
        KultKlient.mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(true));
    }

    @Override
    public void onPacketSend(PacketEvent.Send event) {
        if (event.packet instanceof PlayerMoveC2SPacket) KultKlient.mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(KultKlient.mc.player, ClientCommandC2SPacket.Mode.START_FALL_FLYING));
    }

    @Override
    public void onPlayerMove() {
        KultKlient.mc.player.getAbilities().flying = true;
        KultKlient.mc.player.getAbilities().setFlySpeed(elytraFly.horizontalSpeed.get().floatValue() / 20);
    }
}
