package kult.klient.systems.modules.misc;

import kult.klient.eventbus.EventHandler;
import kult.klient.events.world.TickEvent;
import kult.klient.settings.EnumSetting;
import kult.klient.settings.IntSetting;
import kult.klient.settings.Setting;
import kult.klient.settings.SettingGroup;
import kult.klient.systems.modules.Categories;
import kult.klient.systems.modules.Module;
import kult.klient.utils.Utils;
import net.minecraft.item.Items;

public class AutoClicker extends Module {
    private int timer;

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    // General

    private final Setting<Mode> mode = sgGeneral.add(new EnumSetting.Builder<Mode>()
        .name("mode")
        .description("The method of clicking.")
        .defaultValue(Mode.Press)
        .build()
    );

    private final Setting<Button> button = sgGeneral.add(new EnumSetting.Builder<Button>()
        .name("button")
        .description("Which button to press.")
        .defaultValue(Button.Right)
        .build()
    );

    private final Setting<Integer> delay = sgGeneral.add(new IntSetting.Builder()
        .name("click-delay")
        .description("The amount of delay between clicks in ticks.")
        .defaultValue(2)
        .min(0)
        .sliderRange(0, 60)
        .build()
    );

    public AutoClicker() {
        super(Categories.Misc, Items.STONE_BUTTON, "auto-clicker", "Automatically clicks.");
    }

    @Override
    public void onActivate() {
        timer = 0;
        mc.options.keyAttack.setPressed(false);
        mc.options.keyUse.setPressed(false);
    }

    @Override
    public void onDeactivate() {
        mc.options.keyAttack.setPressed(false);
        mc.options.keyUse.setPressed(false);
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        switch (mode.get()) {
            case Hold:
                switch (button.get()) {
                    case Left:
                        mc.options.keyAttack.setPressed(true);
                        break;
                    case Right:
                        mc.options.keyUse.setPressed(true);
                        break;
                }
                break;
            case Press:
                timer++;
                if (!(delay.get() > timer)) {
                    switch (button.get()) {
                        case Left:
                            Utils.leftClick();
                            break;
                        case Right:
                            Utils.rightClick();
                            break;
                    }
                    timer = 0;
                }
                break;
        }
    }

    public enum Mode {
        Hold("Hold"),
        Press("Press");

        private final String title;

        Mode(String title) {
            this.title = title;
        }

        @Override
        public String toString() {
            return title;
        }
    }

    public enum Button {
        Right("Right"),
        Left("Left");

        private final String title;

        Button(String title) {
            this.title = title;
        }

        @Override
        public String toString() {
            return title;
        }
    }
}
