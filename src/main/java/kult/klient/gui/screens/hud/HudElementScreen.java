package kult.klient.gui.screens.hud;

import kult.klient.events.render.Render2DEvent;
import kult.klient.gui.renderer.GuiRenderer;
import kult.klient.gui.widgets.containers.WContainer;
import kult.klient.gui.widgets.containers.WHorizontalList;
import kult.klient.gui.widgets.pressable.WButton;
import kult.klient.gui.widgets.pressable.WCheckbox;
import kult.klient.systems.Systems;
import kult.klient.utils.misc.NbtUtils;
import kult.klient.gui.GuiTheme;
import kult.klient.gui.WindowScreen;
import kult.klient.systems.hud.HUD;
import kult.klient.systems.hud.HudElement;
import kult.klient.utils.Utils;
import net.minecraft.nbt.NbtCompound;

public class HudElementScreen extends WindowScreen {
    public final HudElement element;
    private WContainer settings;

    public HudElementScreen(GuiTheme theme, HudElement element) {
        super(theme, element.title);

        this.element = element;
    }

    @Override
    public void initWidgets() {
        // Description
        add(theme.label(element.description, Utils.getWindowWidth() / 2.0));

        // Settings
        if (element.settings.sizeGroups() > 0) {
            settings = add(theme.verticalList()).expandX().widget();
            settings.add(theme.settings(element.settings)).expandX();

            add(theme.horizontalSeparator()).expandX();
        }

        // Bottom
        WHorizontalList bottomList = add(theme.horizontalList()).expandX().widget();

        // Active
        bottomList.add(theme.label("Active:"));
        WCheckbox active = bottomList.add(theme.checkbox(element.active)).widget();
        active.action = () -> {
            if (element.active != active.checked) element.toggle();
        };

        WButton reset = bottomList.add(theme.button(GuiRenderer.RESET)).expandCellX().right().widget();
        reset.action = () -> {
            if (element.active != element.defaultActive) element.active = active.checked = element.defaultActive;
        };
    }

    @Override
    public void tick() {
        super.tick();

        if (settings != null) element.settings.tick(settings, theme);
    }

    @Override
    protected void onRenderBefore(float delta) {
        if (!Utils.canUpdate()) Systems.get(HUD.class).onRender(Render2DEvent.get(0, 0, delta));
    }

    @Override
    public boolean toClipboard() {
        return NbtUtils.toClipboard(element.title, element.toTag());
    }

    @Override
    public boolean fromClipboard() {
        NbtCompound clipboard = NbtUtils.fromClipboard(element.toTag());

        if (clipboard != null) {
            element.fromTag(clipboard);
            return true;
        }

        return false;
    }
}
