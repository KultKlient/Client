package kult.legacy.klient.utils.render.color;

import kult.legacy.klient.KultKlientLegacy;
import kult.legacy.klient.events.world.TickEvent;
import kult.legacy.klient.gui.GuiThemes;
import kult.legacy.klient.gui.WidgetScreen;
import kult.legacy.klient.gui.tabs.builtin.ConfigTab;
import kult.legacy.klient.settings.ColorSetting;
import kult.legacy.klient.settings.Setting;
import kult.legacy.klient.settings.SettingGroup;
import kult.legacy.klient.systems.waypoints.Waypoint;
import kult.legacy.klient.systems.waypoints.Waypoints;
import kult.legacy.klient.utils.misc.UnorderedArrayList;
import kult.legacy.klient.eventbus.EventHandler;

import java.util.List;

import static kult.legacy.klient.KultKlientLegacy.mc;

public class RainbowColors {
    public static final RainbowColor GLOBAL = new RainbowColor().setSpeed(ConfigTab.rainbowSpeed.get() / 100);

    private static final List<Setting<SettingColor>> colorSettings = new UnorderedArrayList<>();
    private static final List<SettingColor> colors = new UnorderedArrayList<>();
    private static final List<Runnable> listeners = new UnorderedArrayList<>();

    public static void init() {
        KultKlientLegacy.EVENT_BUS.subscribe(RainbowColors.class);
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
