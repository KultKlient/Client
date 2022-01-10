package kult.klient.systems.hud.modules;

import kult.klient.KultKlient;
import kult.klient.settings.ColorSetting;
import kult.klient.settings.EnumSetting;
import kult.klient.settings.Setting;
import kult.klient.settings.SettingGroup;
import kult.klient.systems.modules.Modules;
import kult.klient.utils.Utils;
import kult.klient.utils.render.color.SettingColor;
import kult.klient.systems.modules.misc.NameProtect;
import kult.klient.systems.hud.HUD;
import kult.klient.systems.hud.TripleTextHudElement;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WelcomeHud extends TripleTextHudElement {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    // General

    private final Setting<Message> message = sgGeneral.add(new EnumSetting.Builder<Message>()
        .name("message")
        .description("Determines what message style to use.")
        .defaultValue(WelcomeHud.Message.Welcome)
        .build()
    );

    private final Setting<SettingColor> usernameColor = sgGeneral.add(new ColorSetting.Builder()
        .name("username-color")
        .description("Color of the username.")
        .defaultValue(new SettingColor(KultKlient.INSTANCE.KULTKLIENT_COLOR.r, KultKlient.INSTANCE.KULTKLIENT_COLOR.g, KultKlient.INSTANCE.KULTKLIENT_COLOR.b, true))
        .build()
    );

    public WelcomeHud(HUD hud) {
        super(hud, "welcome", "Displays a welcome message.", true);
        rightColor = usernameColor.get();
    }

    @Override
    protected String getLeft() {
        switch (message.get()) {
            case Using -> {
                if (Modules.get().isActive(NameProtect.class)) return "You are using KultKlient, ";
                if (Utils.isDeveloper(mc.getSession().getUuid())) return "You are using KultKlient, Developer ";
                else return "You are using KultKlient, ";
            }
            case Time -> {
                if (Modules.get().isActive(NameProtect.class)) return getTime() + ", ";
                if (Utils.isDeveloper(mc.getSession().getUuid())) return getTime() + ", Developer ";
                else return getTime() + ", ";
            }
            case Retarded_Time -> {
                if (Modules.get().isActive(NameProtect.class)) return getRetardedTime() + ", ";
                if (Utils.isDeveloper(mc.getSession().getUuid())) return getRetardedTime() + ", Developer ";
                else return getRetardedTime() + ", ";
            }
            case Sussy -> {
                if (Modules.get().isActive(NameProtect.class)) return "You are a sussy baka, ";
                if (Utils.isDeveloper(mc.getSession().getUuid())) return "You are a sussy baka, Developer ";
                else return "You are a sussy baka, ";
            }
            default -> {
                if (Modules.get().isActive(NameProtect.class)) return "Welcome to KultKlient, ";
                if (Utils.isDeveloper(mc.getSession().getUuid())) return "Welcome to KultKlient, Developer ";
                else return "Welcome to KultKlient, ";
            }
        }
    }

    @Override
    protected String getRight() {
        return Modules.get().get(NameProtect.class).getName(mc.getSession().getUsername());
    }

    @Override
    public String getEnd() {
        return "!";
    }

    private String getTime() {
        final String hourDate = new SimpleDateFormat("k").format(new Date());
        final int hour = Integer.valueOf(hourDate);
        if (hour < 6) return "Good Night";
        if (hour < 12) return "Good Morning";
        if (hour < 17) return "Good Afternoon";
        if (hour < 20) return "Good Evening";
        return "Good Night";
    }

    private String getRetardedTime() {
        final String hourDate = new SimpleDateFormat("k").format(new Date());
        final int hour = Integer.valueOf(hourDate);
        if (hour < 3) return "Why are you killing newfags at this hour retard";
        if (hour < 6) return "You really need get some sleep retard";
        if (hour < 9) return "Ur awake already? such a retard";
        if (hour < 12) return "Retard moment";
        if (hour < 14) return "Go eat lunch retard";
        if (hour < 17) return "Retard playing minecraft";
        return "Time to sleep retard";
    }

    public enum Message {
        Welcome("Welcome"),
        Using("Using"),
        Time("Time"),
        Retarded_Time("Retarded Time"),
        Sussy("Sussy");

        private final String title;

        Message(String title) {
            this.title = title;
        }

        @Override
        public String toString() {
            return title;
        }
    }
}
