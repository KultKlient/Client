package kult.legacy.klient.mixin;

import kult.legacy.klient.KultKlientLegacy;
import kult.legacy.klient.events.entity.player.FinishUsingItemEvent;
import kult.legacy.klient.events.entity.player.StoppedUsingItemEvent;
import kult.legacy.klient.events.game.ItemStackTooltipEvent;
import kult.legacy.klient.utils.Utils;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

import static kult.legacy.klient.KultKlientLegacy.mc;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Inject(method = "getTooltip", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void onGetTooltip(PlayerEntity player, TooltipContext context, CallbackInfoReturnable<List<Text>> info, List<Text> list) {
        if (Utils.canUpdate()) KultKlientLegacy.EVENT_BUS.post(ItemStackTooltipEvent.get((ItemStack) (Object) this, list));
    }

    @Inject(method = "finishUsing", at = @At("HEAD"))
    private void onFinishUsing(World world, LivingEntity user, CallbackInfoReturnable<ItemStack> info) {
        if (user == mc.player) KultKlientLegacy.EVENT_BUS.post(FinishUsingItemEvent.get((ItemStack) (Object) this));
    }

    @Inject(method = "onStoppedUsing", at = @At("HEAD"))
    private void onStoppedUsing(World world, LivingEntity user, int remainingUseTicks, CallbackInfo info) {
        if (user == mc.player) KultKlientLegacy.EVENT_BUS.post(StoppedUsingItemEvent.get((ItemStack) (Object) this));
    }
}
