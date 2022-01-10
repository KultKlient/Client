package kult.klient.systems.modules.chat;

import kult.klient.events.entity.EntityAddedEvent;
import kult.klient.eventbus.EventHandler;
import kult.klient.settings.*;
import kult.klient.systems.enemies.Enemies;
import kult.klient.systems.friends.Friends;
import kult.klient.systems.modules.Module;
import kult.klient.systems.modules.Categories;
import kult.klient.utils.Utils;
import kult.klient.utils.misc.Placeholders;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;

import java.util.List;

public class MessageAura extends Module {
    private int messageI;

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    // General

    private final Setting<List<String>> messages = sgGeneral.add(new StringListSetting.Builder()
        .name("messages")
        .description("The specified messages sent to the player. Use %player% for player name.")
        .defaultValue(
            "Welcome to my render distance, %player%!",
            "Hello %player%, welcome to my render distance!"
        )
        .build()
    );

    private final Setting<Boolean> random = sgGeneral.add(new BoolSetting.Builder()
        .name("random")
        .description("Picks a random message.")
        .defaultValue(true)
        .build()
    );

    private final Setting<Boolean> publicChat = sgGeneral.add(new BoolSetting.Builder()
        .name("public-chat")
        .description("The messages will be sent to public chat.")
        .defaultValue(false)
        .build()
    );

    private final Setting<Boolean> ignoreFriends = sgGeneral.add(new BoolSetting.Builder()
        .name("ignore-friends")
        .description("Will not send any messages to people which are your friends.")
        .defaultValue(false)
        .build()
    );

    private final Setting<Boolean> ignoreEnemies = sgGeneral.add(new BoolSetting.Builder()
        .name("ignore-enemies")
        .description("Will not send any messages to people which are your enemies.")
        .defaultValue(false)
        .build()
    );

    public MessageAura() {
        super(Categories.Chat, Items.DISPENSER, "message-aura", "Sends a specified message to any player that enters render distance.");
    }

    @Override
    public void onActivate() {
        messageI = 0;
    }

    @EventHandler
    private void onEntityAdded(EntityAddedEvent event) {
        if (!(event.entity instanceof PlayerEntity player)) return;
        if (player == mc.player) return;

        if (ignoreFriends.get() && Friends.get().isFriend(player)) return;
        if (ignoreEnemies.get() && Enemies.get().isEnemy(player)) return;

        int i;
        if (random.get()) i = Utils.random(0, messages.get().size());
        else {
            if (messageI >= messages.get().size()) messageI = 0;
            i = messageI++;
        }

        String text = messages.get().get(i);

        if (publicChat.get()) mc.player.sendChatMessage(Placeholders.apply(text).replace("%player%", player.getName().getString()));
        else mc.player.sendChatMessage("/msg " + player.getName().getString() + " " + Placeholders.apply(text));
    }
}
