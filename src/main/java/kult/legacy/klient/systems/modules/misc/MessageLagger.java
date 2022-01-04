package kult.legacy.klient.systems.modules.misc;

import kult.legacy.klient.eventbus.EventHandler;
import kult.legacy.klient.events.game.GameLeftEvent;
import kult.legacy.klient.events.world.TickEvent;
import kult.legacy.klient.settings.BoolSetting;
import kult.legacy.klient.settings.IntSetting;
import kult.legacy.klient.settings.Setting;
import kult.legacy.klient.settings.SettingGroup;
import kult.legacy.klient.systems.modules.Categories;
import kult.legacy.klient.systems.modules.Module;
import kult.legacy.klient.utils.Utils;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;

import java.util.List;
import java.util.Random;

/*/----------------------------------------------------------------------------------------------------------------------------/*/
/*/ Used from Meteor Crash Addon by Wide Cat                                                                                   /*/
/*/ https://github.com/Wide-Cat/meteor-crash-addon/blob/main/src/main/java/widecat/meteorcrashaddon/modules/MessageLagger.java /*/
/*/----------------------------------------------------------------------------------------------------------------------------/*/

public class MessageLagger extends Module {
    private int timer;

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Integer> messageLength = sgGeneral.add(new IntSetting.Builder()
        .name("message-length")
        .description("The length of the message.")
        .defaultValue(200)
        .min(1)
        .sliderRange(1, 1000)
        .build()
    );

    private final Setting<Boolean> keepSending = sgGeneral.add(new BoolSetting.Builder()
        .name("keep-sending")
        .description("Keeps sending the lag messages repeatedly.")
        .defaultValue(false)
        .build()
    );

    private final Setting<Integer> delay = sgGeneral.add(new IntSetting.Builder()
        .name("delay")
        .description("The delay between lag messages in ticks.")
        .defaultValue(100)
        .min(0)
        .sliderRange(0, 1000)
        .visible(keepSending::get)
        .build()
    );

    private final Setting<Boolean> whisper = sgGeneral.add(new BoolSetting.Builder()
        .name("whisper")
        .description("Whispers the lag message to a random person on the server.")
        .defaultValue(false)
        .build()
    );

    private final Setting<Boolean> autoDisable = sgGeneral.add(new BoolSetting.Builder()
        .name("auto-disable")
        .description("Disables the module on kick/leave.")
        .defaultValue(true)
        .build()
    );

    public MessageLagger() {
        super(Categories.Misc, Items.COMMAND_BLOCK, "message-lagger", "Sends dense messages that lag other players on the server.");
    }

    @Override
    public void onActivate() {
        if (Utils.canUpdate() && !keepSending.get()) {
            if (!whisper.get()) sendLagMessage();
            else sendLagWhisper();
            toggle();
        }

        if (keepSending.get()) timer = delay.get();
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        if (timer <= 0) {
            if (Utils.canUpdate() && keepSending.get()) {
                if (!whisper.get()) sendLagMessage();
                else sendLagWhisper();
            }

            timer = delay.get();
        } else timer--;
    }

    @EventHandler
    private void onGameLeft(GameLeftEvent event) {
        if (autoDisable.get() && isActive()) toggle();
    }

    private void sendLagMessage() {
        String message = generateLagMessage();
        mc.player.sendChatMessage(message);
    }

    private void sendLagWhisper() {
        List<AbstractClientPlayerEntity> players = mc.world.getPlayers();
        PlayerEntity player = players.get(new Random().nextInt(players.size()));
        String message = generateLagMessage();

        mc.player.sendChatMessage("/msg " + player.getGameProfile().getName() + " " + message);
    }

    private String generateLagMessage() {
        StringBuilder message = null;
        for (int i = 0; i < messageLength.get(); i++) {
            message.append((char) (Math.floor(Math.random() * 0x1D300) + 0x800));
        }

        return message.toString();
    }
}
