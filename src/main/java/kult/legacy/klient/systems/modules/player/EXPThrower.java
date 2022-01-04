package kult.legacy.klient.systems.modules.player;

import kult.legacy.klient.events.world.TickEvent;
import kult.legacy.klient.systems.modules.Categories;
import kult.legacy.klient.systems.modules.Module;
import kult.legacy.klient.utils.player.FindItemResult;
import kult.legacy.klient.utils.player.InvUtils;
import kult.legacy.klient.utils.player.Rotations;
import kult.legacy.klient.eventbus.EventHandler;
import net.minecraft.item.Items;

public class EXPThrower extends Module {
    public EXPThrower() {
        super(Categories.Player, Items.EXPERIENCE_BOTTLE, "exp-thrower", "Automatically throws XP bottles from your hotbar.");
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        FindItemResult exp = InvUtils.find(Items.EXPERIENCE_BOTTLE);

        if (exp.found()) {
            Rotations.rotate(mc.player.getYaw(), 90, () -> {
                if (exp.getHand() != null) mc.interactionManager.interactItem(mc.player, mc.world, exp.getHand());
                else {
                    InvUtils.swap(exp.getSlot(), true);
                    mc.interactionManager.interactItem(mc.player, mc.world, exp.getHand());
                    InvUtils.swapBack();
                }
            });
        }
    }

    public enum Mode {
        Automatic,
        Manual
    }
}
