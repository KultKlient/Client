package kultklient.legacy.client.mixin;

import kultklient.legacy.client.KultKlientLegacy;
import kultklient.legacy.client.events.render.TooltipDataEvent;
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
