package kult.klient.systems.modules.movement;

import baritone.api.BaritoneAPI;
import kult.klient.eventbus.EventHandler;
import kult.klient.eventbus.EventPriority;
import kult.klient.events.world.TickEvent;
import kult.klient.settings.EnumSetting;
import kult.klient.settings.Setting;
import kult.klient.settings.SettingGroup;
import kult.klient.systems.modules.Categories;
import kult.klient.systems.modules.Module;
import kult.klient.utils.misc.input.Input;
import kult.klient.utils.world.GoalDirection;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.item.Items;

public class AutoWalk extends Module {
    private int timer = 0;
    private GoalDirection goal;

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    // General

    private final Setting<Mode> mode = sgGeneral.add(new EnumSetting.Builder<Mode>()
        .name("mode")
        .description("Walking mode.")
        .defaultValue(Mode.Smart)
        .onChanged(mode -> {
                if (isActive()) {
                    if (mode == Mode.Simple) {
                        BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior().cancelEverything();
                        goal = null;
                    } else {
                        timer = 0;
                        createGoal();
                    }

                    unpress();
                }
            })
        .build()
    );

    private final Setting<Direction> direction = sgGeneral.add(new EnumSetting.Builder<Direction>()
        .name("simple-direction")
        .description("The direction to walk in simple mode.")
        .defaultValue(Direction.Forward)
        .onChanged(direction1 -> {
                if (isActive()) unpress();
            })
        .visible(() -> mode.get() == Mode.Simple)
        .build()
    );

    public AutoWalk() {
        super(Categories.Movement, Items.DIAMOND_BOOTS, "auto-walk", "Automatically walks forward.");
    }

    @Override
    public void onActivate() {
        if (mode.get() == Mode.Smart) createGoal();
    }

    @Override
    public void onDeactivate() {
        if (mode.get() == Mode.Simple) unpress();
        else BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior().cancelEverything();

        goal = null;
    }

    @EventHandler(priority = EventPriority.HIGH)
    private void onTick(TickEvent.Pre event) {
        if (mode.get() == Mode.Simple) {
            switch (direction.get()) {
                case Forward -> setPressed(mc.options.keyForward, true);
                case Backwards -> setPressed(mc.options.keyBack, true);
                case Left -> setPressed(mc.options.keyLeft, true);
                case Right -> setPressed(mc.options.keyRight, true);
            }
        } else {
            if (timer > 20) {
                timer = 0;
                goal.recalculate(mc.player.getPos());
            }

            timer++;
        }
    }

    private void unpress() {
        setPressed(mc.options.keyForward, false);
        setPressed(mc.options.keyBack, false);
        setPressed(mc.options.keyLeft, false);
        setPressed(mc.options.keyRight, false);
    }

    private void setPressed(KeyBinding key, boolean pressed) {
        key.setPressed(pressed);
        Input.setKeyState(key, pressed);
    }

    private void createGoal() {
        timer = 0;
        goal = new GoalDirection(mc.player.getPos(), mc.player.getYaw());
        BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().setGoalAndPath(goal);
    }

    public enum Mode {
        Simple("Simple"),
        Smart("Smart");

        private final String title;

        Mode(String title) {
            this.title = title;
        }

        @Override
        public String toString() {
            return title;
        }
    }

    public enum Direction {
        Forward("Forward"),
        Backwards("Backwards"),
        Left("Left"),
        Right("Right");

        private final String title;

        Direction(String title) {
            this.title = title;
        }

        @Override
        public String toString() {
            return title;
        }
    }
}
