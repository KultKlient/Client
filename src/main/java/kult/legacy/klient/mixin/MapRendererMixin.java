package kultklient.legacy.client.mixin;

import kultklient.legacy.client.systems.modules.render.NoRender;
import kultklient.legacy.client.utils.misc.EmptyIterator;
import kultklient.legacy.client.systems.modules.Modules;
import net.minecraft.item.map.MapIcon;
import net.minecraft.item.map.MapState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets = "net.minecraft.client.render.MapRenderer$MapTexture")
public class MapRendererMixin {
    @Redirect(method = "draw(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ZI)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/map/MapState;getIcons()Ljava/lang/Iterable;"))
    private Iterable<MapIcon> getIconsProxy(MapState state) {
        if (Modules.get().get(NoRender.class).noMapMarkers()) return EmptyIterator::new;
        return state.getIcons();
    }
}
