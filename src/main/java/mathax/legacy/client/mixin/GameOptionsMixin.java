package mathax.legacy.client.mixin;

import mathax.legacy.client.MatHaxLegacy;
import mathax.legacy.client.events.game.ChangePerspectiveEvent;
import mathax.legacy.client.systems.modules.Modules;
import mathax.legacy.client.systems.modules.render.Freecam;
import mathax.legacy.client.utils.misc.input.KeyBinds;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.option.Perspective;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;

@Mixin(GameOptions.class)
public class GameOptionsMixin {
    @Shadow
    @Final
    @Mutable
    public KeyBinding[] keysAll;

    @Inject(method = "<init>", at = @At(value = "FIELD", target = "Lnet/minecraft/client/option/GameOptions;keysAll:[Lnet/minecraft/client/option/KeyBinding;", opcode = Opcodes.PUTFIELD, shift = At.Shift.AFTER))
    private void onInitAfterKeysAll(MinecraftClient client, File optionsFile, CallbackInfo info) {
        keysAll = KeyBinds.apply(keysAll);
    }

    @Inject(method = "setPerspective", at = @At("HEAD"), cancellable = true)
    private void setPerspective(Perspective perspective, CallbackInfo info) {
        ChangePerspectiveEvent event = MatHaxLegacy.EVENT_BUS.post(ChangePerspectiveEvent.get(perspective));

        if (event.isCancelled()) info.cancel();

        if (Modules.get().isActive(Freecam.class)) info.cancel();
    }
}
