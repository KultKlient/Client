package kult.klient.systems.hud.modules;

import kult.klient.utils.misc.HorizontalDirection;
import kult.klient.systems.hud.DoubleTextHudElement;
import kult.klient.systems.hud.HUD;

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
