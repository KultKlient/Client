package kultklient.legacy.client.mixin;

import kultklient.legacy.client.systems.modules.movement.EntityControl;
import kultklient.legacy.client.systems.modules.Modules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.passive.LlamaEntity;


@Mixin(LlamaEntity.class)
public class LlamaEntityMixin {
    @Inject(method = "canBeControlledByRider", at = @At("HEAD"), cancellable = true)
    public void canBeControlledByRider(CallbackInfoReturnable<Boolean> info) {
        if (Modules.get().get(EntityControl.class).isActive()) info.setReturnValue(true);
    }
}
