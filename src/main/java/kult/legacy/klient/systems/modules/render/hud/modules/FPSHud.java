package kultklient.legacy.client.systems.modules.render.hud.modules;

import kultklient.legacy.client.mixin.MinecraftClientAccessor;
import kultklient.legacy.client.systems.modules.render.hud.DoubleTextHudElement;
import kultklient.legacy.client.systems.modules.render.hud.HUD;
import kultklient.legacy.client.systems.modules.render.hud.TripleTextHudElement;

public class FPSHud extends DoubleTextHudElement {
    public FPSHud(HUD hud) {
        super(hud, "fps", "Displays your FPS.", true);
    }

    @Override
    protected String getLeft() {
        return "FPS: ";
    }

    @Override
    protected String getRight() {
        return Integer.toString(MinecraftClientAccessor.getFps());
    }
}
