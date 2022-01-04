package kult.legacy.klient.systems.modules.chat;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import kult.legacy.klient.eventbus.EventHandler;
import kult.legacy.klient.events.packets.PacketEvent;
import kult.legacy.klient.events.world.TickEvent;
import kult.legacy.klient.systems.modules.Categories;
import kult.legacy.klient.systems.modules.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import org.apache.commons.io.IOUtils;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

/*/-----------------------------------------------------------------------------------------------------------------/*/
/*/ Taken from Meteor Rejects                                                                                       /*/
/*/ https://github.com/AntiCope/meteor-rejects/blob/master/src/main/java/anticope/rejects/modules/AntiVanish.java /*/
/*/-----------------------------------------------------------------------------------------------------------------/*/

public class AntiVanish extends Module {
    private final Queue<UUID> toLookup = new ConcurrentLinkedQueue<UUID>();
    private long lastTick = 0;

    public AntiVanish() {
        super(Categories.Chat, Items.WHITE_STAINED_GLASS, "anti-vanish", "Notifies you when someone uses /vanish.");
    }

    @Override
    public void onDeactivate() {
        toLookup.clear();
    }

    @EventHandler
    public void onPacket(PacketEvent.Receive event) {
        if (event.packet instanceof PlayerListS2CPacket) {
            PlayerListS2CPacket packet = (PlayerListS2CPacket) event.packet;
            if (packet.getAction() == PlayerListS2CPacket.Action.UPDATE_LATENCY) {
                try {
                    for (PlayerListS2CPacket.Entry entry : packet.getEntries()) {
                        if (mc.getNetworkHandler().getPlayerListEntry(entry.getProfile().getId()) != null) continue;
                        toLookup.add(entry.getProfile().getId());
                    }
                } catch (Exception ignore) {}
            }
        }
    }

    @EventHandler
    public void onTick(TickEvent.Post event) {
        long time = mc.world.getTime();
        UUID lookup;

        if (Math.abs(lastTick - time) > 100 && (lookup = toLookup.poll()) != null) {
            try {
                String name = getPlayerNameFromUUID(lookup);
                if (name != null) warning("(highlight)%s(default) used (highlight)/vanish(default)!", name);
            } catch (Exception ignore) {}

            lastTick = time;
        }
    }

    public String getPlayerNameFromUUID(UUID id) {
        try {
            NameLookup process = new NameLookup(id, mc);
            Thread thread = new Thread(process);
            thread.start();
            thread.join();
            return process.getName();
        } catch (Exception ignored) {
            return null;
        }
    }

    public static class NameLookup implements Runnable {
        private final String uuidString;
        private final UUID uuid;
        private final MinecraftClient mc;
        private volatile String name;

        public NameLookup(String input, MinecraftClient mc) {
            this.uuidString = input;
            this.uuid = UUID.fromString(input);
            this.mc = mc;
        }

        public NameLookup(UUID input, MinecraftClient mc) {
            this.uuid = input;
            this.uuidString = input.toString();
            this.mc = mc;
        }

        @Override
        public void run() {
            name = lookUpName();
        }

        public String lookUpName() {
            PlayerEntity player = null;
            if (mc.world != null) player = mc.world.getPlayerByUuid(uuid);
            if (player == null) {
                String url = "https://api.mojang.com/user/profiles/" + uuidString.replace("-", "") + "/names";
                try {
                    JsonParser parser = new JsonParser();
                    String nameJson = IOUtils.toString(new URL(url), StandardCharsets.UTF_8);
                    JsonElement nameElement = parser.parse(nameJson);
                    JsonArray nameArray = nameElement.getAsJsonArray();
                    String playerSlot = nameArray.get(nameArray.size() - 1).toString();
                    JsonObject nameObject = parser.parse(playerSlot).getAsJsonObject();

                    return nameObject.get("name").toString();
                } catch (Exception e) {
                    return null;
                }
            }

            return player.getName().asString();
        }

        public String getName() {
            return this.name;
        }
    }
}
