package kult.legacy.klient.systems.modules.render.hud.modules;

import kult.legacy.klient.mixin.ClientPlayerInteractionManagerAccessor;
import kult.legacy.klient.systems.modules.render.hud.DoubleTextHudElement;
import kult.legacy.klient.systems.modules.render.hud.HUD;
import kult.legacy.klient.systems.modules.render.hud.TripleTextHudElement;

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
