package kult.legacy.klient.systems.modules.render.hud.modules;

import kult.legacy.klient.systems.modules.render.hud.DoubleTextHudElement;
import kult.legacy.klient.systems.modules.render.hud.HUD;
import kult.legacy.klient.systems.modules.render.hud.TripleTextHudElement;
import kult.legacy.klient.utils.misc.HorizontalDirection;

public class RotationHud extends DoubleTextHudElement {
    public RotationHud(HUD hud) {
        super(hud, "rotation", "Displays your rotation.", false);
    }

    @Override
    protected String getLeft() {
        HorizontalDirection dir = HorizontalDirection.get(mc.gameRenderer.getCamera().getYaw());
        return String.format("%s %s ", dir.name, dir.axis);
    }

    @Override
    protected String getRight() {
        float yaw = mc.gameRenderer.getCamera().getYaw() % 360;
        if (yaw < 0) yaw += 360;
        if (yaw > 180) yaw -= 360;

        float pitch = mc.gameRenderer.getCamera().getPitch() % 360;
        if (pitch < 0) pitch += 360;
        if (pitch > 180) pitch -= 360;

        return String.format("(%.1f, %.1f)", yaw, pitch);
    }
}
