package kult.legacy.klient.mixin;

import kult.legacy.klient.systems.modules.Modules;
import kult.legacy.klient.systems.modules.render.NoRender;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ToastManager.class)
public class ToastManagerMixin {
    @Inject(method="add", at = @At("HEAD"), cancellable = true)
    public void preventAdd(Toast toast, CallbackInfo info) {
        if (Modules.get().get(NoRender.class).noToasts()) info.cancel();
    }
}
