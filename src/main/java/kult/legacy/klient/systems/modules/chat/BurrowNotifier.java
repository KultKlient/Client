package kult.legacy.klient.systems.modules.chat;

import kult.legacy.klient.eventbus.EventHandler;
import kult.legacy.klient.events.world.TickEvent;
import kult.legacy.klient.settings.IntSetting;
import kult.legacy.klient.settings.Setting;
import kult.legacy.klient.settings.SettingGroup;
import kult.legacy.klient.systems.modules.Categories;
import kult.legacy.klient.systems.modules.Module;
import kult.legacy.klient.utils.player.PlayerUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;

import java.util.ArrayList;
import java.util.List;

public class BurrowNotifier extends Module {
    public static List<PlayerEntity> burrowedPlayers = new ArrayList<>();

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    // General

    private final Setting<Integer> range = sgGeneral.add(new IntSetting.Builder()
        .name("range")
        .description("How far away from you to check for burrowed players.")
        .defaultValue(3)
        .min(0)
        .sliderMax(15)
        .build()
    );

    // TODO: Notify modes

    public BurrowNotifier() {
        super(Categories.Chat, Items.OBSIDIAN, "burrow-notifier", "Notifies you when a player burrows in your render distance.");
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        for (PlayerEntity player : mc.world.getPlayers()) {
            if (isBurrowValid(player)) {
                burrowedPlayers.add(player);
                warning("(highlight)%s(default) is burrowed!", player.getEntityName());
            }

            if (burrowedPlayers.contains(player) && !PlayerUtils.isBurrowed(player, true)) {
                burrowedPlayers.remove(player);
                warning("(highlight)%s(default) is no longer burrowed.", player.getEntityName());
            }
        }
    }

    private boolean isBurrowValid(PlayerEntity p) {
        if (p == mc.player) return false;
        return mc.player.distanceTo(p) <= range.get() && !burrowedPlayers.contains(p) && PlayerUtils.isBurrowed(p, true) && !PlayerUtils.isPlayerMoving(p);
    }
}
