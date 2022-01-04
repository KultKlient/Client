package kult.legacy.klient.systems.modules.chat;

import kult.legacy.klient.events.entity.EntityAddedEvent;
import kult.legacy.klient.eventbus.EventHandler;
import kult.legacy.klient.settings.BoolSetting;
import kult.legacy.klient.settings.Setting;
import kult.legacy.klient.settings.SettingGroup;
import kult.legacy.klient.settings.StringSetting;
import kult.legacy.klient.systems.enemies.Enemies;
import kult.legacy.klient.systems.friends.Friends;
import kult.legacy.klient.systems.modules.Module;
import kult.legacy.klient.systems.modules.Categories;
import kult.legacy.klient.utils.misc.Placeholders;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;

public class MessageAura extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    // General

    private final Setting<String> message = sgGeneral.add(new StringSetting.Builder()
        .name("message")
        .description("The specified message sent to the player.")
        .defaultValue("Welcome to my render distance!")
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

    @EventHandler
    private void onEntityAdded(EntityAddedEvent event) {
        if (!(event.entity instanceof PlayerEntity player)) return;
        if (player == mc.player) return;

        if (ignoreFriends.get() && Friends.get().isFriend(player)) return;
        if (ignoreEnemies.get() && Enemies.get().isEnemy(player)) return;

        mc.player.sendChatMessage("/msg " + player.getName() + " " + Placeholders.apply(message.get()));
    }
}
