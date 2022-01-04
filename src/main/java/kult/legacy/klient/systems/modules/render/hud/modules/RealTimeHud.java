package kult.legacy.klient.systems.modules.render.hud.modules;

import kult.legacy.klient.settings.BoolSetting;
import kult.legacy.klient.settings.Setting;
import kult.legacy.klient.settings.SettingGroup;
import kult.legacy.klient.systems.modules.render.hud.DoubleTextHudElement;
import kult.legacy.klient.systems.modules.render.hud.HUD;
import kult.legacy.klient.systems.modules.render.hud.TripleTextHudElement;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RealTimeHud extends DoubleTextHudElement {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    private final SimpleDateFormat timeFormatSeconds = new SimpleDateFormat("HH:mm:ss");

    // General

    private final Setting<Boolean> seconds = sgGeneral.add(new BoolSetting.Builder()
        .name("seconds")
        .description("Shows seconds.")
        .defaultValue(true)
        .build()
    );

    public RealTimeHud(HUD hud) {
        super(hud, "real-time", "Displays real world time.", true);
    }

    @Override
    protected String getLeft() {
        return "Time: ";
    }

    @Override
    protected String getRight() {
        if (seconds.get()) return timeFormatSeconds.format(new Date());
        return timeFormat.format(new Date());
    }
}
