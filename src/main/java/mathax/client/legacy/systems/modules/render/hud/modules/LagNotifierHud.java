package mathax.client.legacy.systems.modules.render.hud.modules;

import mathax.client.legacy.settings.ColorSetting;
import mathax.client.legacy.settings.Setting;
import mathax.client.legacy.settings.SettingGroup;
import mathax.client.legacy.systems.modules.render.hud.HUD;
import mathax.client.legacy.systems.modules.render.hud.TripleTextHudElement;
import mathax.client.legacy.utils.render.color.SettingColor;
import mathax.client.legacy.utils.world.TickRate;

public class LagNotifierHud extends TripleTextHudElement {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    // General

    private final Setting<SettingColor> badColor = sgGeneral.add(new ColorSetting.Builder()
        .name("bad-color")
        .description("Color of lag length.")
        .defaultValue(new SettingColor(255, 165, 50))
        .build()
    );

    private final Setting<SettingColor> severeColor = sgGeneral.add(new ColorSetting.Builder()
        .name("severe-color")
        .description("Color of lag length.")
        .defaultValue(new SettingColor(80, 0, 0))
        .build()
    );

    public LagNotifierHud(HUD hud) {
        super(hud, "lag-notifier", "Displays if the server is lagging in seconds.");
    }

    @Override
    protected String getLeft() {
        return "Server is lagging ";
    }

    @Override
    protected String getRight() {
        if (isInEditor()) {
            rightColor = hud.secondaryColor.get();
            visible = true;
            return "4,3";
        }

        float timeSinceLastTick = TickRate.INSTANCE.getTimeSinceLastTick();

        if (timeSinceLastTick > 10) rightColor = severeColor.get();
        else rightColor = badColor.get();

        visible = timeSinceLastTick >= 1f;

        return String.format("%.1f", timeSinceLastTick);
    }

    @Override
    public String getEnd() {
        return "";
    }
}