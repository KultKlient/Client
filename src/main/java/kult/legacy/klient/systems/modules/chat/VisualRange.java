package kultklient.legacy.client.systems.modules.chat;

import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import kultklient.legacy.client.eventbus.EventHandler;
import kultklient.legacy.client.events.entity.EntityAddedEvent;
import kultklient.legacy.client.events.entity.EntityRemovedEvent;
import kultklient.legacy.client.settings.*;
import kultklient.legacy.client.systems.friends.Friends;
import kultklient.legacy.client.systems.modules.Categories;
import kultklient.legacy.client.systems.modules.Module;
import kultklient.legacy.client.utils.entity.fakeplayer.FakePlayerEntity;
import kultklient.legacy.client.utils.misc.ChatUtils;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;

import static kultklient.legacy.client.utils.misc.ChatUtils.formatCoords;

public class VisualRange extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    // General

    private final Setting<Event> eventMode = sgGeneral.add(new EnumSetting.Builder<Event>()
        .name("event")
        .description("Determines when to log the entities.")
        .defaultValue(Event.Both)
        .build()
    );

    private final Setting<Object2BooleanMap<EntityType<?>>> entities = sgGeneral.add(new EntityTypeListSetting.Builder()
        .name("entities")
        .description("Determines which entities to notify about.")
        .defaultValue(EntityType.PLAYER)
        .build()
    );

    private final Setting<Boolean> ignoreFriends = sgGeneral.add(new BoolSetting.Builder()
        .name("ignore-friends")
        .description("Ignores friends.")
        .defaultValue(true)
        .build()
    );

    private final Setting<Boolean> ignoreFakes = sgGeneral.add(new BoolSetting.Builder()
        .name("ignore-fake-players")
        .description("Ignores fake players.")
        .defaultValue(true)
        .build()
    );

    // TODO: Notify modes

    public VisualRange() {
        super(Categories.Chat, Items.SPYGLASS, "visual-range", "Notifies you when an entity enters your render distance.");
    }

    @EventHandler
    private void onEntityAdded(EntityAddedEvent event) {
        if (event.entity.getUuid().equals(mc.player.getUuid()) || !entities.get().getBoolean(event.entity.getType()) || eventMode.get() == Event.Despawn) return;

        String entityName;
        if (event.entity instanceof PlayerEntity) {
            if ((!ignoreFriends.get() || !Friends.get().isFriend(((PlayerEntity) event.entity))) && (!ignoreFakes.get() || !(event.entity instanceof FakePlayerEntity))) {
                entityName = event.entity.getEntityName();
                ChatUtils.sendMsg(event.entity.getId() + 100, Formatting.GRAY, "(highlight)%s(default) has entered your visual range!", entityName);
            }
        } else {
            entityName = event.entity.getType().getName().getString();
            String entityPos = event.entity.getPos().toString();
            MutableText text = new LiteralText(entityName).formatted(Formatting.WHITE);
            text.append(new LiteralText(" has spawned at ").formatted(Formatting.GRAY));
            text.append(entityPos);
            text.append(new LiteralText(".").formatted(Formatting.GRAY));
            info(text);
        }
    }

    @EventHandler
    private void onEntityRemoved(EntityRemovedEvent event) {
        if (event.entity.getUuid().equals(mc.player.getUuid()) || !entities.get().getBoolean(event.entity.getType()) || eventMode.get() == Event.Spawn) return;

        if (event.entity instanceof PlayerEntity) {
            if ((!ignoreFriends.get() || !Friends.get().isFriend(((PlayerEntity) event.entity))) && (!ignoreFakes.get() || !(event.entity instanceof FakePlayerEntity))) ChatUtils.sendMsg(event.entity.getId() + 100, Formatting.GRAY, "(highlight)%s(default) has left your visual range!", event.entity.getEntityName());
        } else {
            MutableText text = new LiteralText(event.entity.getType().getName().getString()).formatted(Formatting.WHITE);
            text.append(new LiteralText(" has despawned at ").formatted(Formatting.GRAY));
            text.append(formatCoords(event.entity.getPos()));
            text.append(new LiteralText(".").formatted(Formatting.GRAY));
            info(text);
        }
    }

    public enum Event {
        Spawn,
        Despawn,
        Both
    }
}
