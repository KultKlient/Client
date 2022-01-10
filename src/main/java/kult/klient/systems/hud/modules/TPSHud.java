package kult.klient.systems.hud.modules;

import kult.klient.utils.world.TickRate;
import kult.klient.systems.hud.DoubleTextHudElement;
import kult.klient.systems.hud.HUD;

public class TPSHud extends DoubleTextHudElement {
    public TPSHud(HUD hud) {
        super(hud, "tps", "Displays the server's TPS.", true);
    }

    @Override
    protected String getLeft() {
        return "TPS: ";
    }

    @Override
    protected String getRight() {
        return String.format("%.1f", TickRate.INSTANCE.getTickRate()).replace(",", ".");
    }
}
