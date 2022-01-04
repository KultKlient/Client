package kult.legacy.klient.gui.tabs.builtin;

import kult.legacy.klient.gui.GuiTheme;
import kult.legacy.klient.gui.tabs.Tab;
import kult.legacy.klient.gui.tabs.TabScreen;
import kult.legacy.klient.gui.tabs.WindowTabScreen;
import kult.legacy.klient.music.Music;
import kult.legacy.klient.renderer.text.Fonts;
import kult.legacy.klient.systems.config.Config;
import kult.legacy.klient.utils.misc.NbtUtils;
import kult.legacy.klient.utils.render.color.RainbowColors;
import kult.legacy.klient.settings.*;
import kult.legacy.klient.utils.render.prompts.YesNoPrompt;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.KeyBinding;

import static kult.legacy.klient.KultKlientLegacy.mc;

public class ConfigTab extends Tab {
    public static ConfigScreen currentScreen;

    private static final Settings settings = new Settings();
    private static final SettingGroup sgVisual = settings.createGroup("Visual");
    private static final SettingGroup sgChat = settings.createGroup("Chat");
    private static final SettingGroup sgToasts = settings.createGroup("Toasts");
    private static final SettingGroup sgMusic = settings.createGroup("Music");
    private static final SettingGroup sgMisc = settings.createGroup("Misc");

    // Visual

    public static final Setting<Boolean> customFont = sgVisual.add(new BoolSetting.Builder()
        .name("custom-font")
        .description("Use a custom font.")
        .defaultValue(true)
        .onChanged(aBoolean -> Config.get().customFont = aBoolean)
        .onModuleActivated(booleanSetting -> booleanSetting.set(Config.get().customFont))
        .build()
    );

    public static final Setting<String> font = sgVisual.add(new ProvidedStringSetting.Builder()
        .name("font")
        .description("Custom font to use (picked from .minecraft/KultKlient/Legacy/Fonts folder).")
        .supplier(Fonts::getAvailableFonts)
        .defaultValue(Fonts.DEFAULT_FONT)
        .onChanged(s -> {
            Config.get().font = s;
            Fonts.load();
        })
        .onModuleActivated(stringSetting -> stringSetting.set(Config.get().font))
        .visible(customFont::get)
        .build()
    );

    public static final Setting<Double> rainbowSpeed = sgVisual.add(new DoubleSetting.Builder()
        .name("rainbow-speed")
        .description("The global rainbow speed.")
        .defaultValue(0.5)
        .range(0, 10)
        .sliderMax(5)
        .onChanged(value -> RainbowColors.GLOBAL.setSpeed(value / 100))
        .onModuleActivated(setting -> setting.set(RainbowColors.GLOBAL.getSpeed() * 100))
        .build()
    );

    // Chat

    public static final Setting<String> prefix = sgChat.add(new StringSetting.Builder()
        .name("prefix")
        .description("The command prefix.")
        .defaultValue(".")
        .onChanged(newPrefix -> Config.get().prefix = newPrefix)
        .onModuleActivated(stringSetting -> stringSetting.set(Config.get().prefix))
        .build()
    );

    public static final Setting<Boolean> prefixOpensConsole = sgChat.add(new BoolSetting.Builder()
        .name("open-chat-on-prefix")
        .description("Open chat when command prefix is pressed. Works like pressing '/' in vanilla.")
        .defaultValue(true)
        .onChanged(aBoolean -> Config.get().prefixOpensConsole = aBoolean)
        .onModuleActivated(booleanSetting -> booleanSetting.set(Config.get().prefixOpensConsole))
        .build()
    );

    public static final Setting<Boolean> chatFeedback = sgChat.add(new BoolSetting.Builder()
        .name("chat-feedback")
        .description("Sends chat feedback when KultKlient performs certain actions.")
        .defaultValue(true)
        .onChanged(aBoolean -> Config.get().chatFeedback = aBoolean)
        .onModuleActivated(booleanSetting -> booleanSetting.set(Config.get().chatFeedback))
        .build()
    );

    public static final Setting<Boolean> deleteChatFeedback = sgChat.add(new BoolSetting.Builder()
        .name("delete-chat-feedback")
        .description("Delete previous matching chat feedback to keep chat clear.")
        .visible(chatFeedback::get)
        .defaultValue(true)
        .onChanged(aBoolean -> Config.get().deleteChatFeedback = aBoolean)
        .onModuleActivated(booleanSetting -> booleanSetting.set(Config.get().deleteChatFeedback))
        .build()
    );

    // Toasts

    public static final Setting<Boolean> toastFeedback = sgToasts.add(new BoolSetting.Builder()
        .name("toast-feedback")
        .description("Sends a toast feedback when KultKlient performs certain actions.")
        .defaultValue(true)
        .onChanged(aBoolean -> Config.get().toastFeedback = aBoolean)
        .onModuleActivated(booleanSetting -> booleanSetting.set(Config.get().toastFeedback))
        .build()
    );

    public static final Setting<Integer> toastDuration = sgToasts.add(new IntSetting.Builder()
        .name("duration")
        .description("Determines how long the toast will stay visible in milliseconds")
        .defaultValue(3000)
        .min(1)
        .sliderRange(1, 6000)
        .onChanged(integer -> Config.get().toastDuration = integer)
        .onModuleActivated(integerSetting -> integerSetting.set(Config.get().toastDuration))
        .build()
    );

    public static final Setting<Boolean> toastSound = sgToasts.add(new BoolSetting.Builder()
        .name("sound")
        .description("Plays a sound when a toast appears.")
        .defaultValue(true)
        .onChanged(aBoolean -> Config.get().toastSound = aBoolean)
        .onModuleActivated(booleanSetting -> booleanSetting.set(Config.get().toastSound))
        .build()
    );

    // Music

    public static final Setting<Integer> musicVolume = sgMusic.add(new IntSetting.Builder()
        .name("volume")
        .description("Determines the volume of the currently played music.")
        .defaultValue(100)
        .min(1)
        .sliderRange(1, 250)
        .onChanged(integer -> {
            Music.player.setVolume(integer);
            Config.get().musicVolume = integer;
        })
        .onModuleActivated(integerSetting -> {
            Music.player.setVolume(Config.get().musicVolume);
            integerSetting.set(Config.get().musicVolume);
        })
        .build()
    );

    // Misc

    public static final Setting<Integer> rotationHoldTicks = sgMisc.add(new IntSetting.Builder()
        .name("rotation-hold")
        .description("Hold long to hold server side rotation when not sending any packets.")
        .defaultValue(4)
        .onChanged(integer -> Config.get().rotationHoldTicks = integer)
        .onModuleActivated(integerSetting -> integerSetting.set(Config.get().rotationHoldTicks))
        .build()
    );

    public static final Setting<Boolean> useTeamColor = sgMisc.add(new BoolSetting.Builder()
        .name("use-team-color")
        .description("Uses player's team color for rendering things like esp and tracers.")
        .defaultValue(true)
        .onChanged(aBoolean -> Config.get().useTeamColor = aBoolean)
        .onModuleActivated(booleanSetting -> booleanSetting.set(Config.get().useTeamColor))
        .build()
    );

    public ConfigTab() {
        super("Config");
    }

    @Override
    public TabScreen createScreen(GuiTheme theme) {
        return currentScreen = new ConfigScreen(theme, this);
    }

    @Override
    public boolean isScreen(Screen screen) {
        return screen instanceof ConfigScreen;
    }

    public static class ConfigScreen extends WindowTabScreen {
        public ConfigScreen(GuiTheme theme, Tab tab) {
            super(theme, tab);

            settings.onActivated();

            onClosed(() -> {
                String prefix = Config.get().prefix;

                if (prefix.isBlank()) {
                    YesNoPrompt.create(theme, this.parent)
                        .title("Empty command prefix")
                        .message("You have set your command prefix to nothing.")
                        .message("This WILL prevent you from sending chat messages.")
                        .message("Do you want to reset your prefix back to '.'?")
                        .onYes(() -> Config.get().prefix = ".")
                        .id("empty-command-prefix")
                        .show();
                }
                else if (prefix.equals("/")) {
                    YesNoPrompt.create(theme, this.parent)
                        .title("Potential prefix conflict")
                        .message("You have set your command prefix to '/', which is used by minecraft.")
                        .message("This can cause conflict issues between KultKlient and Minecraft commands.")
                        .message("Do you want to reset your prefix to '.'?")
                        .onYes(() -> Config.get().prefix = ".")
                        .id("minecraft-prefix-conflict")
                        .show();
                }
                else if (prefix.length() > 7) {
                    YesNoPrompt.create(theme, this.parent)
                        .title("Long command prefix")
                        .message("You have set your command prefix to a very long string.")
                        .message("This means that in order to execute any command, you will need to type %s followed by the command you want to run.", prefix)
                        .message("Do you want to reset your prefix back to '.'?")
                        .onYes(() -> Config.get().prefix = ".")
                        .id("long-command-prefix")
                        .show();
                }
                else if (isUsedKey()) {
                    YesNoPrompt.create(theme, this.parent)
                        .title("Prefix keybind")
                        .message("You have \"Open Chat On Prefix\" setting enabled and your command prefix has a conflict with another keybind.")
                        .message("Do you want to disable \"Open Chat On Prefix\" setting?")
                        .onYes(() -> Config.get().prefixOpensConsole = false)
                        .id("prefix-keybind")
                        .show();
                }
            });
        }

        @Override
        public void initWidgets() {
            add(theme.settings(settings)).expandX();
        }

        @Override
        public void tick() {
            super.tick();

            settings.tick(window, theme);
        }

        @Override
        public boolean toClipboard() {
            return NbtUtils.toClipboard(Config.get());
        }

        @Override
        public boolean fromClipboard() {
            return NbtUtils.fromClipboard(Config.get());
        }
    }

    private static boolean isUsedKey() {
        if (!Config.get().prefixOpensConsole) return false;

        String prefixKeybindTranslation = String.format("key.keyboard.%s",  Config.get().prefix.toLowerCase().substring(0,1));
        for (KeyBinding key: mc.options.keysAll) {
            if (key.getBoundKeyTranslationKey().equals(prefixKeybindTranslation)) return true;
        }

        return false;
    }
}
