package kult.klient.systems.hud.modules;

import kult.klient.systems.hud.DoubleTextHudElement;
import kult.klient.systems.hud.HUD;
import net.minecraft.client.network.PlayerListEntry;

public class PingHud extends DoubleTextHudElement {
    public PingHud(HUD hud) {
        super(hud, "ping", "Displays your ping.", true);
    }

    @Override
    protected String getLeft() {
        return "Ping: ";
    }

    @Override
    protected String getRight() {
        if (isInEditor()) return "0";

        PlayerListEntry playerListEntry = mc.getNetworkHandler().getPlayerListEntry(mc.player.getUuid());

        if (playerListEntry != null) return Integer.toString(playerListEntry.getLatency());
        return "0";
    }
}
