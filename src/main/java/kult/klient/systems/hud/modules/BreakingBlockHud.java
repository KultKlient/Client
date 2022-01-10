package kult.klient.systems.hud.modules;

import kult.klient.mixin.ClientPlayerInteractionManagerAccessor;
import kult.klient.systems.hud.DoubleTextHudElement;
import kult.klient.systems.hud.HUD;

public class BreakingBlockHud extends DoubleTextHudElement {
    public BreakingBlockHud(HUD hud) {
        super(hud, "breaking-block", "Displays percentage of the block you are breaking.", true);
    }

    @Override
    protected String getLeft() {
        return "Breaking Block: ";
    }

    @Override
    protected String getRight() {
        if (isInEditor()) return "0%";
        return String.format("%.0f%%", ((ClientPlayerInteractionManagerAccessor) mc.interactionManager).getBreakingProgress() * 100);
    }
}
