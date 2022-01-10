package kult.klient;

import kult.klient.eventbus.EventBus;
import kult.klient.eventbus.EventHandler;
import kult.klient.eventbus.IEventBus;
import kult.klient.events.kultklient.KeyEvent;
import kult.klient.events.kultklient.MouseButtonEvent;
import kult.klient.events.world.TickEvent;
import kult.klient.gui.GuiThemes;
import kult.klient.gui.renderer.GuiRenderer;
import kult.klient.gui.tabs.Tabs;
import kult.klient.music.Music;
import kult.klient.renderer.GL;
import kult.klient.renderer.PostProcessRenderer;
import kult.klient.renderer.Renderer2D;
import kult.klient.renderer.Shaders;
import kult.klient.renderer.text.Fonts;
import kult.klient.systems.Systems;
import kult.klient.systems.config.Config;
import kult.klient.systems.hud.HUD;
import kult.klient.systems.modules.Categories;
import kult.klient.systems.modules.Modules;
import kult.klient.systems.modules.client.CapesModule;
import kult.klient.systems.modules.client.ClientSpoof;
import kult.klient.systems.modules.client.DiscordRPC;
import kult.klient.systems.modules.client.MiddleClickFriend;
import kult.klient.systems.modules.combat.*;
import kult.klient.utils.Utils;
import kult.klient.utils.Version;
import kult.klient.utils.misc.FakeClientPlayer;
import kult.klient.utils.misc.KeyBind;
import kult.klient.utils.misc.Names;
import kult.klient.utils.misc.WindowUtils;
import kult.klient.utils.misc.input.KeyAction;
import kult.klient.utils.misc.input.KeyBinds;
import kult.klient.utils.network.KultKlientExecutor;
import kult.klient.utils.player.DamageUtils;
import kult.klient.utils.player.EChestMemory;
import kult.klient.utils.player.Rotations;
import kult.klient.utils.render.EntityShaders;
import kult.klient.utils.render.color.Color;
import kult.klient.utils.render.color.RainbowColors;
import kult.klient.utils.world.BlockIterator;
import kult.klient.utils.world.BlockUtils;
import kult.klient.systems.modules.render.Background;
import kult.klient.systems.modules.render.Zoom;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.SplashOverlay;
import net.minecraft.util.Formatting;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import java.io.File;
import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.List;

/*/------------------------------------------------------------------/*/
/*/ THIS CLIENT IS A FORK OF METEOR CLIENT BY MINEGAME159 & SEASNAIL /*/
/*/ https://meteorclient.com                                         /*/
/*/ https://github.com/MeteorDevelopment/meteor-client               /*/
/*/------------------------------------------------------------------/*/
/*/ Music player used from Motor Tunez made by JFronny               /*/
/*/ https://github.com/JFronny/MotorTunez                            /*/
/*/------------------------------------------------------------------/*/

public class KultKlient implements ClientModInitializer {
    public static KultKlient INSTANCE;
    public static MinecraftClient mc;
    public static final IEventBus EVENT_BUS = new EventBus();

    public static final File GAME_FOLDER = new File(FabricLoader.getInstance().getGameDir().toString());
    public static final File FOLDER = new File(GAME_FOLDER, "KultKlient");
    public static final File VERSION_FOLDER = new File(FOLDER + "/" + Version.getMinecraft());
    public static final File MUSIC_FOLDER = new File(FOLDER + "/Music");

    public final Color KULTKLIENT_COLOR = new Color(230, 75, 100, 255);
    public final int KULTKLIENT_COLOR_INT = Color.fromRGBA(230, 75, 100, 255);
    public final Color KULTKLIENT_BACKGROUND_COLOR = new Color(30, 30, 45, 255);
    public final int KULTKLIENT_BACKGROUND_COLOR_INT = Color.fromRGBA(30, 30, 45, 255);

    public static final Logger LOG = LogManager.getLogger();
    public static String logPrefix = "[KultKlient] ";

    public static final String URL = "https://kultklient.github.io/";
    public static final String API_URL = "https://kultklient.github.io/kultklient.github.io-api/";

    public static List<String> getDeveloperUUIDs() {
        return Arrays.asList(

            // 71Zombie
            "ccd031c5-5779-492d-8b25-934c9ca2bb27",
            "1dbfd020-5e78-349c-bda6-f4317e1e89e5"

        );
    }

    public static List<String> getSplashes() {
        return Arrays.asList(

            // SPLASHES
            Formatting.RED + "KultKlient on top!",
            Formatting.GRAY + "71Zombie" + Formatting.RED + " based god",
            Formatting.RED + "kultklient.github.io",
            Formatting.RED + "kultklient.github.io/Discord",
            Formatting.RED + Version.getStylized(),
            Formatting.RED + Version.getMinecraft(),

            // MEME SPLASHES
            Formatting.YELLOW + "cope",
            Formatting.YELLOW + "I am funny -HiIAmFunny",
            Formatting.YELLOW + "IntelliJ IDEa",
            Formatting.YELLOW + "I <3 nns",
            Formatting.YELLOW + "haha 69",
            Formatting.YELLOW + "420 XDDDDDD",
            Formatting.YELLOW + "ayy",
            Formatting.YELLOW + "too ez",
            Formatting.YELLOW + "owned",
            Formatting.YELLOW + "your mom :joy:",
            Formatting.YELLOW + "BOOM BOOM BOOM!",
            Formatting.YELLOW + "I <3 forks",
            Formatting.YELLOW + "based",
            Formatting.YELLOW + "Pog",
            Formatting.YELLOW + "Big Rat on top!",
            Formatting.YELLOW + "bigrat.monster",

            // PERSONALIZED
            Formatting.YELLOW + "You're cool, " + Formatting.GRAY + MinecraftClient.getInstance().getSession().getUsername(),
            Formatting.YELLOW + "Owning with " + Formatting.GRAY + MinecraftClient.getInstance().getSession().getUsername(),
            Formatting.YELLOW + "Who is " + Formatting.GRAY + MinecraftClient.getInstance().getSession().getUsername() + Formatting.YELLOW + "?"

        );
    }

    @Override
    public void onInitializeClient() {
        // Instance
        if (INSTANCE == null) {
            INSTANCE = this;
            return;
        }

        // Log
        LOG.info(logPrefix + "Initializing KultKlient " + Version.getStylized() + " for Minecraft " + Version.getMinecraft() + "...");

        // Global Minecraft client accessor
        mc = MinecraftClient.getInstance();

        // Icon & Title
        WindowUtils.KultKlient.setIcon();
        WindowUtils.KultKlient.setTitleLoading();

        // Register event handlers
        EVENT_BUS.registerLambdaFactory("kult.klient", (lookupInMethod, klass) -> (MethodHandles.Lookup) lookupInMethod.invoke(null, klass, MethodHandles.lookup()));

        // Pre-load
        Systems.addPreLoadTask(() -> {
            if (!Modules.get().getFile().exists()) {
                // ACTIVATE
                Modules.get().get(CapesModule.class).forceToggle(true); // CAPES
                Modules.get().get(DiscordRPC.class).forceToggle(true); // DISCORD RPC
                Modules.get().get(Background.class).forceToggle(true); // BACKGROUND
                Modules.get().get(MiddleClickFriend.class).forceToggle(true); // MIDDLE CLICK FRIEND

                // VISIBILITY
                Modules.get().get(ClientSpoof.class).setVisible(false); // CLIENT SPOOF
                Modules.get().get(CapesModule.class).setVisible(false); // CAPES
                Modules.get().get(DiscordRPC.class).setVisible(false); // DISCORD RPC
                Modules.get().get(Background.class).setVisible(false); // BACKGROUND
                Modules.get().get(MiddleClickFriend.class).setVisible(false); // MIDDLE CLICK FRIEND
                Modules.get().get(Zoom.class).setVisible(false); // ZOOM

                // KEYBINDS
                Modules.get().get(Zoom.class).keybind.set(KeyBind.fromKey(GLFW.GLFW_KEY_C)); // ZOOM

                // KEYBIND OPTIONS
                Modules.get().get(Zoom.class).toggleOnBindRelease = true; // ZOOM

                // TOASTS
                Modules.get().get(AnchorAura.class).setToggleToast(true); // ANCHOR AURA
                Modules.get().get(BedAura.class).setToggleToast(true); // BED AURA
                Modules.get().get(CEVBreaker.class).setToggleToast(true); // CEV BREAKER
                Modules.get().get(CrystalAura.class).setToggleToast(true); // CRYSTAL AURA
                Modules.get().get(KillAura.class).setToggleToast(true); // KILL AURA

                // MESSAGES
                Modules.get().get(Zoom.class).setToggleMessage(false); // ZOOM
            }

            // RESET HUD LOCATIONS
            if (!Systems.get(HUD.class).getFile().exists()) Systems.get(HUD.class).reset.run(); // HUD
        });

        // Pre init
        Utils.init();
        GL.init();
        Shaders.init();
        Renderer2D.init();
        EntityShaders.initOutlines();
        KultKlientExecutor.init();
        BlockIterator.init();
        EChestMemory.init();
        Rotations.init();
        Names.init();
        FakeClientPlayer.init();
        PostProcessRenderer.init();
        Tabs.init();
        GuiThemes.init();
        Fonts.init();
        DamageUtils.init();
        BlockUtils.init();
        Music.init();

        // Register module categories
        Categories.init();

        // Load systems
        Systems.init();

        // Event bus
        EVENT_BUS.subscribe(this);

        // Sorting modules
        Modules.get().sortModules();

        // Load saves
        Systems.load();

        // Post init
        Fonts.load();
        GuiRenderer.init();
        GuiThemes.postInit();
        RainbowColors.init();

        // Title
        WindowUtils.KultKlient.setTitleLoaded();

        // Shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            net.arikia.dev.drpc.DiscordRPC.discordClearPresence();
            net.arikia.dev.drpc.DiscordRPC.discordShutdown();
            Systems.save();
            GuiThemes.save();
        }));

        // Icon & Title
        ClientSpoof cs = Modules.get().get(ClientSpoof.class);
        if (cs.isActive() && cs.changeWindowIcon()) WindowUtils.Meteor.setIcon();
        else WindowUtils.KultKlient.setIcon();
        if (cs.isActive() && cs.changeWindowTitle()) WindowUtils.Meteor.setTitle();
        else WindowUtils.KultKlient.setTitle();

        // Log
        LOG.info(logPrefix + "KultKlient " + Version.getStylized() + " initialized for Minecraft " + Version.getMinecraft() + "!");
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (mc.currentScreen == null && mc.getOverlay() == null && KeyBinds.OPEN_COMMANDS.wasPressed()) mc.setScreen(new ChatScreen(Config.get().prefix.get()));

        if (Music.player == null) return;
        if (Music.player.getVolume() != Config.get().musicVolume.get()) Music.player.setVolume(Config.get().musicVolume.get());
    }

    @EventHandler
    private void onKey(KeyEvent event) {
        if (mc.getOverlay() instanceof SplashOverlay) return;
        if (event.action == KeyAction.Press && KeyBinds.OPEN_CLICK_GUI.matchesKey(event.key, 0)) openClickGUI();
    }

    private void onMouseButton(MouseButtonEvent event) {
        if (mc.getOverlay() instanceof SplashOverlay) return;
        if (event.action == KeyAction.Press && KeyBinds.OPEN_CLICK_GUI.matchesMouse(event.button)) openClickGUI();
    }

    private void openClickGUI() {
        if (Utils.canOpenClickGUI()) Tabs.get().get(0).openScreen(GuiThemes.get());
    }
}
