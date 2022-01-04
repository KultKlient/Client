package kult.legacy.klient.systems;

import kult.legacy.klient.KultKlientLegacy;
import kult.legacy.klient.eventbus.EventHandler;
import kult.legacy.klient.events.game.GameJoinedEvent;
import kult.legacy.klient.events.game.GameLeftEvent;
import kult.legacy.klient.systems.accounts.Accounts;
import kult.legacy.klient.systems.commands.Commands;
import kult.legacy.klient.systems.enemies.Enemies;
import kult.legacy.klient.systems.macros.Macros;
import kult.legacy.klient.systems.profiles.Profiles;
import kult.legacy.klient.systems.waypoints.Waypoints;
import kult.legacy.klient.systems.config.Config;
import kult.legacy.klient.systems.friends.Friends;
import kult.legacy.klient.systems.modules.Modules;
import kult.legacy.klient.systems.proxies.Proxies;
import kult.legacy.klient.utils.Version;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Systems {
    @SuppressWarnings("rawtypes")
    private static final Map<Class<? extends System>, System<?>> systems = new HashMap<>();
    private static final List<Runnable> preLoadTasks = new ArrayList<>(1);

    public static void addPreLoadTask(Runnable task) {
        preLoadTasks.add(task);
    }

    public static void init() {
        System<?> config = add(new Config());
        config.init();
        config.load();

        add(new Modules());
        add(new Commands());
        add(new Friends());
        add(new Enemies());
        add(new Macros());
        add(new Accounts());
        add(new Waypoints());
        add(new Profiles());
        add(new Proxies());

        KultKlientLegacy.EVENT_BUS.subscribe(Systems.class);
    }

    private static System<?> add(System<?> system) {
        systems.put(system.getClass(), system);
        KultKlientLegacy.EVENT_BUS.subscribe(system);
        system.init();

        return system;
    }

    // Game join

    @EventHandler
    private void onGameJoined(GameJoinedEvent event) {
        Version.UpdateChecker.checkForLatest = true;
    }

    // Game leave

    @EventHandler
    private static void onGameLeft(GameLeftEvent event) {
        Version.UpdateChecker.checkForLatest = true;
        save();
    }

    // Save

    public static void save(File folder) {
        long start = java.lang.System.currentTimeMillis();
        KultKlientLegacy.LOG.info(KultKlientLegacy.logPrefix + "Systems are saving...");

        for (System<?> system : systems.values()) system.save(folder);

        KultKlientLegacy.LOG.info(KultKlientLegacy.logPrefix + "Systems saved in {} milliseconds.", java.lang.System.currentTimeMillis() - start);
    }

    public static void save() {
        save(null);
    }

    // Load

    public static void load(File folder) {
        long start = java.lang.System.currentTimeMillis();
        KultKlientLegacy.LOG.info(KultKlientLegacy.logPrefix + "Systems are loading...");

        for (Runnable task : preLoadTasks) task.run();

        for (System<?> system : systems.values()) system.load(folder);

        KultKlientLegacy.LOG.info(KultKlientLegacy.logPrefix + "Systems loaded in {} milliseconds.", java.lang.System.currentTimeMillis() - start);
    }

    public static void load() {
        load(null);
    }

    @SuppressWarnings("unchecked")
    public static <T extends System<?>> T get(Class<T> klass) {
        return (T) systems.get(klass);
    }
}
