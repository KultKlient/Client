package kult.klient.systems.hud.modules;

import kult.klient.utils.Utils;
import kult.klient.systems.hud.DoubleTextHudElement;
import kult.klient.systems.hud.HUD;

public class GameTimeHud extends DoubleTextHudElement {

    public GameTimeHud(HUD hud) {
        super(hud, "game-time", "Displays the in-game time.", true);
    }

    @Override
    protected String getLeft() {
        return "Game Time: ";
    }

    @Override
    protected String getRight() {
        if (isInEditor()) return "12:00:00";

        return Utils.getWorldTime();
    }
}
