package kultklient.legacy.client.utils.render.color;

import kultklient.legacy.client.KultKlientLegacy;
import kultklient.legacy.client.events.world.TickEvent;
import kultklient.legacy.client.gui.GuiThemes;
import kultklient.legacy.client.gui.WidgetScreen;
import kultklient.legacy.client.gui.tabs.builtin.ConfigTab;
import kultklient.legacy.client.settings.ColorSetting;
import kultklient.legacy.client.settings.Setting;
import kultklient.legacy.client.settings.SettingGroup;
import kultklient.legacy.client.systems.waypoints.Waypoint;
import kultklient.legacy.client.systems.waypoints.Waypoints;
import kultklient.legacy.client.utils.misc.UnorderedArrayList;
import kultklient.legacy.client.eventbus.EventHandler;

import java.util.List;

import static kultklient.legacy.client.KultKlientLegacy.mc;

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
