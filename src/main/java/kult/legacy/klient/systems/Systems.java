package kultklient.legacy.client.systems;

import kultklient.legacy.client.KultKlientLegacy;
import kultklient.legacy.client.eventbus.EventHandler;
import kultklient.legacy.client.events.game.GameJoinedEvent;
import kultklient.legacy.client.events.game.GameLeftEvent;
import kultklient.legacy.client.systems.accounts.Accounts;
import kultklient.legacy.client.systems.commands.Commands;
import kultklient.legacy.client.systems.enemies.Enemies;
import kultklient.legacy.client.systems.macros.Macros;
import kultklient.legacy.client.systems.profiles.Profiles;
import kultklient.legacy.client.systems.waypoints.Waypoints;
import kultklient.legacy.client.systems.config.Config;
import kultklient.legacy.client.systems.friends.Friends;
import kultklient.legacy.client.systems.modules.Modules;
import kultklient.legacy.client.systems.proxies.Proxies;
import kultklient.legacy.client.utils.Version;

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
