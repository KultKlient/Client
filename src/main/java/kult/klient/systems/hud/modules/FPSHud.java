package kult.klient.systems.hud.modules;

import kult.klient.mixin.MinecraftClientAccessor;
import kult.klient.systems.hud.DoubleTextHudElement;
import kult.klient.systems.hud.HUD;

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
