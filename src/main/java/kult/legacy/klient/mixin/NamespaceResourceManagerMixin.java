package kult.legacy.klient.mixin;

import kult.legacy.klient.KultKlientLegacy;
import net.minecraft.resource.NamespaceResourceManager;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceImpl;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** This mixin is only active when fabric-resource-loader mod is not present */
@Mixin(NamespaceResourceManager.class)
public class NamespaceResourceManagerMixin {
    @Inject(method = "getResource", at = @At("HEAD"), cancellable = true)
    private void onGetResource(Identifier id, CallbackInfoReturnable<Resource> info) {
        if (id.getNamespace().equals("kultklientlegacy")) info.setReturnValue(new ResourceImpl("KultKlient Legacy", id, KultKlientLegacy.class.getResourceAsStream("/assets/kultklientlegacy/" + id.getPath()), null));
    }
}
