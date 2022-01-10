package kult.klient.systems.modules.chat;

import kult.klient.eventbus.EventHandler;
import kult.klient.eventbus.EventPriority;
import kult.klient.events.game.ReceiveMessageEvent;
import kult.klient.settings.Setting;
import kult.klient.settings.SettingGroup;
import kult.klient.settings.StringSetting;
import kult.klient.systems.modules.Categories;
import kult.klient.systems.modules.Module;
import net.minecraft.item.Items;

import java.util.ArrayList;

public class AutoLogin extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    // General

    private final Setting<String> password = sgGeneral.add(new StringSetting.Builder()
        .name("password")
        .description("The password to log in with.")
        .defaultValue("password")
        .build()
    );

    private final ArrayList<String> loginMessages = new ArrayList<>() {{
        add("/login ");
        add("/login <password>");
    }};

    public AutoLogin() {
        super(Categories.Chat, Items.COMPASS, "auto-login", "Automatically logs into servers that use /login.");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onMessageRecieve(ReceiveMessageEvent event) {
        for (String loginMessage: loginMessages) {
            if (event.getMessage().getString().contains(loginMessage)) mc.player.sendChatMessage("/login " + password.get());
        }
    }
}
