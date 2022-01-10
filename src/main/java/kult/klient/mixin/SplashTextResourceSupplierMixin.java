package kult.klient.mixin;

import kult.klient.KultKlient;
import net.minecraft.client.resource.SplashTextResourceSupplier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(SplashTextResourceSupplier.class)
public class SplashTextResourceSupplierMixin {
    private final Random random = new Random();


    @Inject(method = "get", at = @At("HEAD"), cancellable = true)
    private void onApply(CallbackInfoReturnable<String> info) {
        info.setReturnValue(KultKlient.getSplashes().get(random.nextInt(KultKlient.getSplashes().size())));
    }
}
