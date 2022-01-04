package kult.legacy.klient.systems.modules.movement;

import baritone.api.BaritoneAPI;
import kult.legacy.klient.events.packets.PacketEvent;
import kult.legacy.klient.events.world.TickEvent;
import kult.legacy.klient.mixininterface.IPlayerMoveC2SPacket;
import kult.legacy.klient.mixininterface.IVec3d;
import kult.legacy.klient.settings.BoolSetting;
import kult.legacy.klient.settings.EnumSetting;
import kult.legacy.klient.settings.Setting;
import kult.legacy.klient.settings.SettingGroup;
import kult.legacy.klient.mixin.PlayerMoveC2SPacketAccessor;
import kult.legacy.klient.systems.modules.Categories;
import kult.legacy.klient.systems.modules.Module;
import kult.legacy.klient.systems.modules.Modules;
import kult.legacy.klient.utils.entity.EntityUtils;
import kult.legacy.klient.utils.player.FindItemResult;
import kult.legacy.klient.utils.player.InvUtils;
import kult.legacy.klient.utils.player.PlayerUtils;
import kult.legacy.klient.utils.player.Rotations;
import kult.legacy.klient.utils.world.BlockUtils;
import kult.legacy.klient.eventbus.EventHandler;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.RaycastContext;

import java.util.function.Predicate;

public class NoFall extends Module {
    private boolean placedWater;
    private int preBaritoneFallHeight;

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Mode> mode = sgGeneral.add(new EnumSetting.Builder<Mode>()
        .name("mode")
        .description("The way you are saved from fall damage.")
        .defaultValue(Mode.Packet)
        .build()
    );

    private final Setting<PlaceMode> airPlaceMode = sgGeneral.add(new EnumSetting.Builder<PlaceMode>()
        .name("place-mode")
        .description("Whether place mode places before you die or before you take damage.")
        .defaultValue(PlaceMode.Before_Death)
        .visible(() -> mode.get() == Mode.AirPlace)
        .build()
    );

    private final Setting<Boolean> anchor = sgGeneral.add(new BoolSetting.Builder()
        .name("anchor")
        .description("Centers the player and reduces movement when using bucket or air place mode.")
        .defaultValue(true)
        .visible(() -> mode.get() != Mode.Packet)
        .build()
    );

    public NoFall() {
        super(Categories.Movement, Items.FEATHER, "no-fall", "Attempts to prevent you from taking fall damage.");
    }

    @Override
    public void onActivate() {
        preBaritoneFallHeight = BaritoneAPI.getSettings().maxFallHeightNoWater.value;
        if (mode.get() == Mode.Packet) BaritoneAPI.getSettings().maxFallHeightNoWater.value = 255;
        placedWater = false;
    }

    @Override
    public void onDeactivate() {
        BaritoneAPI.getSettings().maxFallHeightNoWater.value = preBaritoneFallHeight;
    }

    @EventHandler
    private void onSendPacket(PacketEvent.Send event) {
        if (mc.player.getAbilities().creativeMode
            || !(event.packet instanceof PlayerMoveC2SPacket)
            || mode.get() != Mode.Packet
            || ((IPlayerMoveC2SPacket) event.packet).getNbt() == 1337) return;


        if ((mc.player.isFallFlying() || Modules.get().isActive(Flight.class)) && mc.player.getVelocity().y < 1) {
            BlockHitResult result = mc.world.raycast(new RaycastContext(
                mc.player.getPos(),
                mc.player.getPos().subtract(0, 0.5, 0),
                RaycastContext.ShapeType.OUTLINE,
                RaycastContext.FluidHandling.NONE,
                mc.player)
            );

            if (result != null && result.getType() == HitResult.Type.BLOCK) {
                ((PlayerMoveC2SPacketAccessor) event.packet).setOnGround(true);
            }
        } else {
            ((PlayerMoveC2SPacketAccessor) event.packet).setOnGround(true);
        }
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        if (mc.player.getAbilities().creativeMode) return;

        // Airplace mode
        if (mode.get() == Mode.AirPlace) {
            // Test if fall damage setting is valid
            if (!airPlaceMode.get().test(mc.player.fallDistance)) return;

            // Center and place block
            if (anchor.get()) PlayerUtils.centerPlayer();

            Rotations.rotate(mc.player.getYaw(), 90, Integer.MAX_VALUE, () -> {
                double preY = mc.player.getVelocity().y;
                ((IVec3d) mc.player.getVelocity()).setY(0);

                BlockUtils.place(mc.player.getBlockPos().down(), InvUtils.findInHotbar(itemStack -> itemStack.getItem() instanceof BlockItem), false, 0, true);

                ((IVec3d) mc.player.getVelocity()).setY(preY);
            });
        }

        // Bucket mode
        if (mode.get() == Mode.Bucket) {
            if (mc.player.fallDistance > 3 && !EntityUtils.isAboveWater(mc.player)) {
                // Place water
                FindItemResult waterBucket = InvUtils.findInHotbar(Items.WATER_BUCKET);

                if (!waterBucket.found()) return;

                // Center player
                if (anchor.get()) PlayerUtils.centerPlayer();

                // Check if there is a block within 5 blocks
                BlockHitResult result = mc.world.raycast(new RaycastContext(mc.player.getPos(), mc.player.getPos().subtract(0, 5, 0), RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, mc.player));

                // Place water
                if (result != null && result.getType() == HitResult.Type.BLOCK) {
                    useBucket(waterBucket, true);
                }
            }

            // Remove water
            if (placedWater && mc.player.getBlockStateAtPos().getFluidState().getFluid() == Fluids.WATER) {
                useBucket(InvUtils.findInHotbar(Items.BUCKET), false);
            }
        }
    }

    private void useBucket(FindItemResult bucket, boolean placedWater) {
        if (!bucket.found()) return;

        Rotations.rotate(mc.player.getYaw(), 90, 10, true, () -> {
            if (bucket.isOffhand()) {
                mc.interactionManager.interactItem(mc.player, mc.world, Hand.OFF_HAND);
            } else {
                int preSlot = mc.player.getInventory().selectedSlot;
                InvUtils.swap(bucket.getSlot(), true);
                mc.interactionManager.interactItem(mc.player, mc.world, Hand.MAIN_HAND);
                InvUtils.swapBack();
            }

            this.placedWater = placedWater;
        });
    }

    @Override
    public String getInfoString() {
        return mode.get().toString();
    }

    public enum Mode {
        Packet,
        AirPlace,
        Bucket
    }

    public enum PlaceMode {
        Before_Damage(height -> height > 2),
        Before_Death(height -> height > Math.max(PlayerUtils.getTotalHealth(), 2));

        private final Predicate<Float> fallHeight;

        PlaceMode(Predicate<Float> fallHeight) {
            this.fallHeight = fallHeight;
        }

        public boolean test(float fallheight) {
            return fallHeight.test(fallheight);
        };

        @Override
        public String toString() {
            return super.toString().replace("_", " ");
        }
    }
}
