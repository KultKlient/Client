package kult.legacy.klient.gui.widgets.input;

import kult.legacy.klient.KultKlientLegacy;
import kult.legacy.klient.eventbus.EventHandler;
import kult.legacy.klient.events.entity.player.InteractBlockEvent;
import kult.legacy.klient.events.entity.player.StartBreakingBlockEvent;
import kult.legacy.klient.gui.widgets.containers.WHorizontalList;
import kult.legacy.klient.gui.widgets.pressable.WButton;
import kult.legacy.klient.systems.modules.Modules;
import kult.legacy.klient.systems.modules.render.marker.Marker;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;

import static kult.legacy.klient.utils.Utils.canUpdate;
import static kult.legacy.klient.KultKlientLegacy.mc;

public class WBlockPosEdit extends WHorizontalList {
    public Runnable action;
    public Runnable actionOnRelease;

    private WTextBox textBoxX, textBoxY, textBoxZ;

    private Screen previousScreen;

    private BlockPos value;
    private BlockPos lastValue;

    private boolean clicking;

    public WBlockPosEdit(BlockPos value) {
        this.value = value;
    }

    @Override
    public void init() {
        setTextBox();

        if (canUpdate()) {
            WButton click = add(theme.button("Click")).expandX().widget();
            click.action = () -> {
                StringBuilder sb = new StringBuilder("Click!\n");
                sb.append("Right click to pick a new position.\n");
                sb.append("Left click to cancel.");
                Modules.get().get(Marker.class).info(sb.toString());

                clicking = true;
                KultKlientLegacy.EVENT_BUS.subscribe(this);
                previousScreen = mc.currentScreen;
                mc.setScreen(null);
            };

            WButton here = add(theme.button("Set Here")).expandX().widget();
            here.action = () -> {
                lastValue = value;
                set(new BlockPos(mc.player.getBlockPos()));
                newValueCheck();

                clear();
                init();
            };
        }
    }

    @EventHandler
    private void onStartBreakingBlock(StartBreakingBlockEvent event) {
        if (clicking) {
            clicking = false;
            event.cancel();
            KultKlientLegacy.EVENT_BUS.unsubscribe(this);
            mc.setScreen(previousScreen);
        }
    }

    @EventHandler
    private void onInteractBlock(InteractBlockEvent event) {
        if (clicking) {
            if (event.result.getType() == HitResult.Type.MISS) return;
            lastValue = value;
            set(event.result.getBlockPos());
            newValueCheck();

            clicking = false;
            event.cancel();
            KultKlientLegacy.EVENT_BUS.unsubscribe(this);
            mc.setScreen(previousScreen);
        }
    }

    private boolean filter(String text, char c) {
        boolean good;
        boolean validate = true;

        if (c == '-' && text.isEmpty()) {
            good = true;
            validate = false;
        } else good = Character.isDigit(c);

        if (good && validate) {
            try {
                Integer.parseInt(text + c);
            } catch (NumberFormatException ignored) {
                good = false;
            }
        }

        return good;
    }

    public BlockPos get() {
        return value;
    }

    public void set(BlockPos value) {
        this.value = value;
    }

    private void setTextBox() {
        textBoxX = add(theme.textBox(Integer.toString(value.getX()), this::filter)).minWidth(75).widget();
        textBoxY = add(theme.textBox(Integer.toString(value.getY()), this::filter)).minWidth(75).widget();
        textBoxZ = add(theme.textBox(Integer.toString(value.getZ()), this::filter)).minWidth(75).widget();

        textBoxX.actionOnUnfocused = () -> {
            lastValue = value;
            textBoxCheck(textBoxX);
            newValueCheck();
        };

        textBoxY.actionOnUnfocused = () -> {
            lastValue = value;
            textBoxCheck(textBoxY);
            newValueCheck();
        };

        textBoxZ.actionOnUnfocused = () -> {
            lastValue = value;
            textBoxCheck(textBoxZ);
            newValueCheck();
        };
    }

    private void newValueCheck() {
        if (value != lastValue) {
            if (action != null) action.run();
            if (actionOnRelease != null) actionOnRelease.run();
        }
    }

    private void textBoxCheck(WTextBox textBox) {
        if (textBox.get().isEmpty()) set(new BlockPos(0, 0, 0));
        else {
            set(new BlockPos(Integer.parseInt(textBoxX.get()), value.getY(), value.getZ()));
        }
    }
}
