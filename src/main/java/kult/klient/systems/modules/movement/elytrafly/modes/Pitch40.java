package kult.klient.systems.modules.movement.elytrafly.modes;

import kult.klient.KultKlient;
import kult.klient.events.entity.player.PlayerMoveEvent;
import kult.klient.systems.modules.movement.elytrafly.ElytraFlightMode;
import kult.klient.systems.modules.movement.elytrafly.ElytraFlightModes;

public class Pitch40 extends ElytraFlightMode {
    private boolean pitchingDown = true;
    private int pitch;

    public Pitch40() {
        super(ElytraFlightModes.Pitch40);
    }

    @Override
    public void onActivate() {
        if (KultKlient.mc.player.getY() < elytraFly.pitch40upperBounds.get()) {
            elytraFly.error("You must be above upper bounds!");
            elytraFly.toggle();
        }

        pitch = 40;
    }

    @Override
    public void onDeactivate() {}

    @Override
    public void onTick() {
        if (pitchingDown && KultKlient.mc.player.getY() <= elytraFly.pitch40lowerBounds.get()) pitchingDown = false;
        else if (!pitchingDown && KultKlient.mc.player.getY() >= elytraFly.pitch40upperBounds.get()) pitchingDown = true;

        if (!pitchingDown && KultKlient.mc.player.getPitch() > -40) {
            pitch -= elytraFly.pitch40rotationSpeed.get();

            if (pitch < -40) pitch = -40;
        } else if (pitchingDown && KultKlient.mc.player.getPitch() < 40) {
            pitch += elytraFly.pitch40rotationSpeed.get();

            if (pitch > 40) pitch = 40;
        }

        KultKlient.mc.player.setPitch(pitch);
    }

    @Override
    public void autoTakeoff() {}

    @Override
    public void handleHorizontalSpeed(PlayerMoveEvent event) {
        velX = event.movement.x;
        velZ = event.movement.z;
    }

    @Override
    public void handleVerticalSpeed(PlayerMoveEvent event) {}

    @Override
    public void handleFallMultiplier() {}

    @Override
    public void handleAutopilot() {}
}
