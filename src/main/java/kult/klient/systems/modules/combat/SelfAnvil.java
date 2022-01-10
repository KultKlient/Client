package kult.klient.systems.modules.combat;

import kult.klient.eventbus.EventHandler;
import kult.klient.events.game.OpenScreenEvent;
import kult.klient.events.world.TickEvent;
import kult.klient.systems.modules.Categories;
import kult.klient.systems.modules.Module;
import kult.klient.utils.player.InvUtils;
import kult.klient.utils.world.BlockUtils;
import net.minecraft.block.AnvilBlock;
import net.minecraft.block.Block;
import net.minecraft.client.gui.screen.ingame.AnvilScreen;
import net.minecraft.item.Items;

public class SelfAnvil extends Module {
    public SelfAnvil() {
        super(Categories.Combat, Items.ANVIL, "self-anvil", "Automatically places an anvil on you to prevent other players from going into your hole.");
    }

    @EventHandler
    private void onOpenScreen(OpenScreenEvent event) {
        if (event.screen instanceof AnvilScreen) event.cancel();
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        if (BlockUtils.place(mc.player.getBlockPos().add(0, 2, 0), InvUtils.findInHotbar(itemStack -> Block.getBlockFromItem(itemStack.getItem()) instanceof AnvilBlock), 0)) toggle();
    }
}
