package kultklient.legacy.client.systems.modules.chat;

import kultklient.legacy.client.events.world.TickEvent;
import kultklient.legacy.client.settings.*;
import kultklient.legacy.client.systems.modules.Module;
import kultklient.legacy.client.systems.modules.Categories;
import kultklient.legacy.client.utils.Utils;
import kultklient.legacy.client.eventbus.EventHandler;
import net.minecraft.item.Items;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.List;

public class Spam extends Module {
    private int messageI, timer;

    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final SettingGroup sgAntiSpamBypass = settings.createGroup("Anti Spam Bypass");

    // General

    private final Setting<List<String>> messages = sgGeneral.add(new StringListSetting.Builder()
        .name("messages")
        .description("Messages to use for spam.")
        .defaultValue(
            "KultKlient on top!",
            "KultKollektive on top!"
        )
        .build()
    );

    private final Setting<Integer> delay = sgGeneral.add(new IntSetting.Builder()
        .name("delay")
        .description("The delay between specified messages in ticks.")
        .defaultValue(20)
        .min(0)
        .sliderRange(0, 200)
        .build()
    );

    private final Setting<Boolean> random = sgGeneral.add(new BoolSetting.Builder()
        .name("randomise")
        .description("Selects a random message from your spam message list.")
        .defaultValue(false)
        .build()
    );

    // Anti Spam Bypass

    private final Setting<Boolean> antiSpamBypass = sgAntiSpamBypass.add(new BoolSetting.Builder()
        .name("enabled")
        .description("Adds random text at the bottom of the text.")
        .defaultValue(false)
        .build()
    );

    private final Setting<Integer> length = sgAntiSpamBypass.add(new IntSetting.Builder()
        .name("length")
        .description("Text length of anti spam bypass.")
        .defaultValue(16)
        .sliderRange(1, 256)
        .build()
    );

    public Spam() {
        super(Categories.Chat, Items.PAPER, "spam", "Spams specified messages in chat.");
    }

    @Override
    public void onActivate() {
        timer = delay.get();
        messageI = 0;
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (messages.get().isEmpty()) return;

        if (timer <= 0) {
            int i;
            if (random.get()) i = Utils.random(0, messages.get().size());
            else {
                if (messageI >= messages.get().size()) messageI = 0;
                i = messageI++;
            }

            String text = messages.get().get(i);
            if (antiSpamBypass.get()) text += RandomStringUtils.randomAlphabetic(length.get()).toLowerCase();
            mc.player.sendChatMessage(text.replace("%random_player%", Utils.getRandomPlayer()));
            timer = delay.get();
        } else timer--;
    }
}
