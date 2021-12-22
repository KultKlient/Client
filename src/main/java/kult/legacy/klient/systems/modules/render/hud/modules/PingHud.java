package kultklient.legacy.client.systems.modules.render.hud.modules;

import kultklient.legacy.client.systems.modules.render.hud.DoubleTextHudElement;
import kultklient.legacy.client.systems.modules.render.hud.HUD;
import kultklient.legacy.client.systems.modules.render.hud.TripleTextHudElement;
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
