package kult.klient.systems.hud.modules;

import kult.klient.utils.Utils;
import kult.klient.systems.hud.DoubleTextHudElement;
import kult.klient.systems.hud.HUD;

public class SpeedHud extends DoubleTextHudElement {
    public SpeedHud(HUD hud) {
        super(hud, "speed", "Displays your horizontal speed.", true);
    }

    @Override
    protected String getLeft() {
        return "Speed: ";
    }

    @Override
    protected String getRight() {
        if (isInEditor()) return "0";

        return String.format("%.1f", Utils.getPlayerSpeed());
    }
}
