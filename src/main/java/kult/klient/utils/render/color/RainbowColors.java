package kult.klient.utils.render.color;

import kult.klient.KultKlient;
import kult.klient.events.world.TickEvent;
import kult.klient.gui.GuiThemes;
import kult.klient.gui.WidgetScreen;
import kult.klient.settings.ColorSetting;
import kult.klient.settings.Setting;
import kult.klient.settings.SettingGroup;
import kult.klient.systems.config.Config;
import kult.klient.systems.waypoints.Waypoint;
import kult.klient.systems.waypoints.Waypoints;
import kult.klient.utils.misc.UnorderedArrayList;
import kult.klient.eventbus.EventHandler;

import java.util.List;

import static kult.klient.KultKlient.mc;

public class RainbowColors {
    public static final RainbowColor GLOBAL = new RainbowColor().setSpeed(Config.get().rainbowSpeed.get() / 100);

    private static final List<Setting<SettingColor>> colorSettings = new UnorderedArrayList<>();
    private static final List<SettingColor> colors = new UnorderedArrayList<>();
    private static final List<Runnable> listeners = new UnorderedArrayList<>();

    public static void init() {
        KultKlient.EVENT_BUS.subscribe(RainbowColors.class);
    }

    public static void addSetting(Setting<SettingColor> setting) {
        colorSettings.add(setting);
    }

    public static void removeSetting(Setting<SettingColor> setting) {
        colorSettings.remove(setting);
    }

    public static void add(SettingColor color) {
        colors.add(color);
    }

    public static void register(Runnable runnable) {
        listeners.add(runnable);
    }

    @EventHandler
    private static void onTick(TickEvent.Post event) {
        GLOBAL.getNext();

        for (Setting<SettingColor> setting : colorSettings) {
            if (setting.module == null || setting.module.isActive()) setting.get().update();
        }

        for (SettingColor color : colors) {
            color.update();
        }

        for (Waypoint waypoint : Waypoints.get()) {
            waypoint.color.update();
        }

        if (mc.currentScreen instanceof WidgetScreen) {
            for (SettingGroup group : GuiThemes.get().settings) {
                for (Setting<?> setting : group) {
                    if (setting instanceof ColorSetting) ((SettingColor) setting.get()).update();
                }
            }
        }

        for (Runnable listener : listeners) listener.run();
    }
}
