package kult.legacy.klient.mixin;

import kult.legacy.klient.KultKlientLegacy;
import kult.legacy.klient.events.render.TooltipDataEvent;
import net.minecraft.client.item.TooltipData;
import net.minecraft.item.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(Item.class)
public class ItemMixin {
    @Inject(method = "getTooltipData", at=@At("HEAD"), cancellable = true)
    private void onTooltipData(ItemStack stack, CallbackInfoReturnable<Optional<TooltipData>> info) {
        TooltipDataEvent event = KultKlientLegacy.EVENT_BUS.post(TooltipDataEvent.get(stack));
        if (event.tooltipData != null) info.setReturnValue(Optional.of(event.tooltipData));
    }
}
