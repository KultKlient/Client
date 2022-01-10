package kult.klient.systems.modules.movement;

import kult.klient.eventbus.EventHandler;
import kult.klient.events.packets.PacketEvent;
import kult.klient.events.world.TickEvent;
import kult.klient.mixin.PlayerMoveC2SPacketAccessor;
import kult.klient.settings.*;
import kult.klient.systems.modules.Categories;
import kult.klient.systems.modules.Module;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;

public class Flight extends Module {
    private double lastY = Double.MAX_VALUE;

    private long lastModifiedTime = 0;

    private float lastYaw;

    private boolean flip;

    private int delayLeft;
    private int offLeft;

    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final SettingGroup sgAntiKick = settings.createGroup("Anti Kick");

    // General

    private final Setting<Mode> mode = sgGeneral.add(new EnumSetting.Builder<Mode>()
        .name("mode")
        .description("The mode for Flight.")
        .defaultValue(Mode.Abilities)
        .build()
    );

    private final Setting<Double> speed = sgGeneral.add(new DoubleSetting.Builder()
        .name("speed")
        .description("Your speed when flying.")
        .defaultValue(0.1)
        .min(0.0)
        .sliderRange(0.0, 2.0)
        .build()
    );

    private final Setting<Boolean> verticalSpeedMatch = sgGeneral.add(new BoolSetting.Builder()
        .name("vertical-speed-match")
        .description("Matches your vertical speed to your horizontal speed, otherwise uses vanilla ratio.")
        .defaultValue(false)
        .build()
    );

    // Anti Kick

    private final Setting<AntiKickMode> antiKickMode = sgAntiKick.add(new EnumSetting.Builder<AntiKickMode>()
        .name("mode")
        .description("The mode for anti kick.")
        .defaultValue(AntiKickMode.Packet)
        .build()
    );

    private final Setting<Integer> delay = sgAntiKick.add(new IntSetting.Builder()
        .name("delay")
        .description("The amount of delay, in ticks, between toggles in normal mode.")
        .defaultValue(80)
        .range(1, 5000)
        .sliderRange(1, 200)
        .visible(() -> antiKickMode.get() == AntiKickMode.Normal)
        .build()
    );

    private final Setting<Integer> offTime = sgAntiKick.add(new IntSetting.Builder()
        .name("off-time")
        .description("The amount of delay, in ticks, that Flight is toggled off for in normal mode.")
        .defaultValue(5)
        .range(1, 20)
        .sliderRange(1, 20)
        .visible(() -> antiKickMode.get() == AntiKickMode.Normal)
        .build()
    );

    public Flight() {
        super(Categories.Movement, Items.COMMAND_BLOCK, "flight", "Allows you to fly. No Fall is recommended with this module.");
    }

    @Override
    public void onActivate() {
        delayLeft = delay.get();
        offLeft = offTime.get();

        if (mode.get() == Mode.Abilities && !mc.player.isSpectator()) {
            mc.player.getAbilities().flying = true;
            if (mc.player.getAbilities().creativeMode) return;
            mc.player.getAbilities().allowFlying = true;
        }
    }

    @Override
    public void onDeactivate() {
        if (mode.get() == Mode.Abilities && !mc.player.isSpectator()) {
            mc.player.getAbilities().flying = false;
            mc.player.getAbilities().setFlySpeed(0.05f);
            if (mc.player.getAbilities().creativeMode) return;
            mc.player.getAbilities().allowFlying = false;
        }
    }

    @EventHandler
    private void onPreTick(TickEvent.Pre event) {
        float currentYaw = mc.player.getYaw();

        if (mc.player.fallDistance >= 3f && currentYaw == lastYaw && mc.player.getVelocity().length() < 0.003d) {
            mc.player.setYaw(currentYaw + (flip ? 1 : -1));
            flip = !flip;
        }

        lastYaw = currentYaw;
    }

    @EventHandler
    private void onPostTick(TickEvent.Post event) {
        if (antiKickMode.get() == AntiKickMode.Normal && delayLeft > 0) delayLeft--;

        else if (antiKickMode.get() == AntiKickMode.Normal && delayLeft <= 0 && offLeft > 0) {
            offLeft --;

            if (mode.get() == Mode.Abilities) {
                mc.player.getAbilities().flying = false;
                mc.player.getAbilities().setFlySpeed(0.05f);
                if (mc.player.getAbilities().creativeMode) return;
                mc.player.getAbilities().allowFlying = false;
            }

            return;
        } else if (antiKickMode.get() == AntiKickMode.Normal && delayLeft <=0 && offLeft <= 0) {
            delayLeft = delay.get();
            offLeft = offTime.get();
        }

        if (mc.player.getYaw() != lastYaw) mc.player.setYaw(lastYaw);

        switch (mode.get()) {
            case Velocity -> {
                mc.player.getAbilities().flying = false;
                mc.player.airStrafingSpeed = speed.get().floatValue() * (mc.player.isSprinting() ? 15f : 10f);
                mc.player.setVelocity(0, 0, 0);
                Vec3d initialVelocity = mc.player.getVelocity();
                if (mc.options.keyJump.isPressed()) mc.player.setVelocity(initialVelocity.add(0, speed.get() * (verticalSpeedMatch.get() ? 10f : 5f), 0));
                if (mc.options.keySneak.isPressed()) mc.player.setVelocity(initialVelocity.subtract(0, speed.get() * (verticalSpeedMatch.get() ? 10f : 5f), 0));
            }
            case Abilities -> {
                if (mc.player.isSpectator()) return;
                mc.player.getAbilities().setFlySpeed(speed.get().floatValue());
                mc.player.getAbilities().flying = true;
                if (mc.player.getAbilities().creativeMode) return;
                mc.player.getAbilities().allowFlying = true;
            }
        }
    }

    @EventHandler
    private void onSendPacket(PacketEvent.Send event) {
        if (!(event.packet instanceof PlayerMoveC2SPacket) || antiKickMode.get() != AntiKickMode.Packet) return;

        PlayerMoveC2SPacket packet = (PlayerMoveC2SPacket) event.packet;
        long currentTime = System.currentTimeMillis();
        double currentY = packet.getY(Double.MAX_VALUE);
        if (currentY != Double.MAX_VALUE) {
            if (currentTime - lastModifiedTime > 1000 && lastY != Double.MAX_VALUE && mc.world.getBlockState(mc.player.getBlockPos().down()).isAir()) {
                ((PlayerMoveC2SPacketAccessor) packet).setY(lastY - 0.03130D);
                lastModifiedTime = currentTime;
            } else lastY = currentY;
        }
    }

    public enum Mode {
        Abilities("Abilities"),
        Velocity("Velocity");

        private final String title;

        Mode(String title) {
            this.title = title;
        }

        @Override
        public String toString() {
            return title;
        }
    }

    public enum AntiKickMode {
        Normal("Normal"),
        Packet("Packet"),
        None("None");

        private final String title;

        AntiKickMode(String title) {
            this.title = title;
        }

        @Override
        public String toString() {
            return title;
        }
    }
}
