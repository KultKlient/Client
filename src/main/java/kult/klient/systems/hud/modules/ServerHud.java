package kult.klient.systems.hud.modules;

import kult.klient.utils.Utils;
import kult.klient.systems.hud.DoubleTextHudElement;
import kult.klient.systems.hud.HUD;

public class ServerHud extends DoubleTextHudElement {
    public ServerHud(HUD hud) {
        super(hud, "server", "Displays the server you're currently in.", true);
    }

    @Override
    protected String getLeft() {
        return "Server: ";
    }

    @Override
    protected String getRight() {
        if (!Utils.canUpdate()) return "None";

        return Utils.getWorldName();
    }
}
