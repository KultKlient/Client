package kultklient.legacy.client.systems.modules.chat;

import kultklient.legacy.client.eventbus.EventHandler;
import kultklient.legacy.client.eventbus.EventPriority;
import kultklient.legacy.client.events.game.ReceiveMessageEvent;
import kultklient.legacy.client.settings.Setting;
import kultklient.legacy.client.settings.SettingGroup;
import kultklient.legacy.client.settings.StringSetting;
import kultklient.legacy.client.systems.modules.Categories;
import kultklient.legacy.client.systems.modules.Module;
import net.minecraft.item.Items;

import java.util.ArrayList;

public class AutoLogin extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<String> password = sgGeneral.add(new StringSetting.Builder()
        .name("password")
        .description("The password to log in with.")
        .defaultValue("password")
        .build()
    );

    private final ArrayList<String> loginMessages = new ArrayList<String>() {{
        add("/login ");
        add("/login <password>");
    }};

    public AutoLogin() {
        super(Categories.Chat, Items.COMPASS, "auto-login", "Automatically logs into servers that use /login.");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onMessageRecieve(ReceiveMessageEvent event) {
        if (mc.world == null || mc.player == null) return;
        String msg = event.getMessage().getString();
        if (msg.startsWith(">")) return; // Ignore chat messages
        for (String loginMsg: loginMessages) {
            if (msg.contains(loginMsg)) {
                mc.player.sendChatMessage("/login " + password.get());
                break;
            }
        }
    }
}
