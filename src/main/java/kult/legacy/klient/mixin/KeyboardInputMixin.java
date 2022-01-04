package kult.legacy.klient.mixin;

import kult.legacy.klient.systems.modules.Modules;
import kult.legacy.klient.systems.modules.misc.Twerk;
import kult.legacy.klient.systems.modules.movement.Sneak;
import net.minecraft.client.input.Input;
import net.minecraft.client.input.KeyboardInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
public class KeyboardInputMixin extends Input {
    @Inject(method = "tick", at = @At("TAIL"))
    private void isPressed(boolean slowDown, CallbackInfo info) {
        if (Modules.get().get(Sneak.class).doVanilla()) sneaking = true;
        if (Modules.get().get(Twerk.class).doVanilla()) sneaking = true;
    }
}
