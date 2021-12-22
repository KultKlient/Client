package kultklient.legacy.client.gui.screens;

import kultklient.legacy.client.events.render.Render2DEvent;
import kultklient.legacy.client.gui.GuiTheme;
import kultklient.legacy.client.gui.WindowScreen;
import kultklient.legacy.client.gui.renderer.GuiRenderer;
import kultklient.legacy.client.gui.widgets.containers.WContainer;
import kultklient.legacy.client.gui.widgets.containers.WHorizontalList;
import kultklient.legacy.client.gui.widgets.pressable.WButton;
import kultklient.legacy.client.gui.widgets.pressable.WCheckbox;
import kultklient.legacy.client.systems.modules.render.hud.HUD;
import kultklient.legacy.client.systems.modules.render.hud.HudElement;
import kultklient.legacy.client.systems.modules.Modules;
import kultklient.legacy.client.utils.Utils;
import kultklient.legacy.client.utils.misc.NbtUtils;
import net.minecraft.nbt.NbtCompound;

public class HudElementScreen extends WindowScreen {
    private final HudElement element;
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
        if (!Utils.canUpdate()) Modules.get().get(HUD.class).onRender(Render2DEvent.get(0, 0, delta));
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
