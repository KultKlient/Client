package kult.klient.systems.modules.chat;

import kult.klient.eventbus.EventHandler;
import kult.klient.events.packets.PacketEvent;
import kult.klient.events.world.TickEvent;
import kult.klient.settings.*;
import kult.klient.systems.modules.combat.*;
import kult.klient.systems.friends.Friends;
import kult.klient.systems.modules.Categories;
import kult.klient.systems.modules.Module;
import kult.klient.systems.modules.Modules;
import kult.klient.utils.misc.Placeholders;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.*;

import java.util.*;

public class AutoEZ extends Module {
    private final Random random = new Random();

    private boolean canSendPop;

    private int ticks;

    private final SettingGroup sgKills = settings.createGroup("Kills");
    private final SettingGroup sgTotemPops = settings.createGroup("Totem Pops");

    // Kills

    private final Setting<Boolean> kills = sgKills.add(new BoolSetting.Builder()
        .name("enabled")
        .description("Enables the kill messages.")
        .defaultValue(true)
        .build()
    );

    private final Setting<Mode> killMode = sgKills.add(new EnumSetting.Builder<Mode>()
        .name("mode")
        .description("Determines what messages to use.")
        .defaultValue(Mode.KultKlient)
        .build()
    );

    private final Setting<MessageStyle> killMessageStyle = sgKills.add(new EnumSetting.Builder<MessageStyle>()
        .name("style")
        .description("Determines what message style to use.")
        .defaultValue(MessageStyle.EZ)
        .visible(() -> killMode.get() == Mode.KultKlient)
        .build()
    );

    private final Setting<Boolean> killIgnoreFriends = sgKills.add(new BoolSetting.Builder()
        .name("ignore-friends")
        .description("Ignores friends.")
        .defaultValue(true)
        .build()
    );

    private final Setting<List<String>> killMessages = sgKills.add(new StringListSetting.Builder()
        .name("messages")
        .description("Custom messages when you kill someone.")
        .defaultValue(Arrays.asList(
            "haha %player% is a noob! EZZz",
            "I just raped %player%!",
            "I just ended %player%!",
            "I just EZZz'd %player%!",
            "I just fucked %player%!",
            "Take the L nerd %player%! You just got ended!",
            "I just nae nae'd %player%!",
            "I am too good for %player%!"
        ))
        .visible(() -> killMode.get() == Mode.Custom)
        .build()
    );

    // Totem Pops

    private final Setting<Boolean> totems = sgTotemPops.add(new BoolSetting.Builder()
        .name("enabled")
        .description("Enables the totem pop messages.")
        .defaultValue(true)
        .build()
    );

    private final Setting<Integer> delay = sgTotemPops.add(new IntSetting.Builder()
        .name("delay")
        .description("Determines how often to send totem pop messages.")
        .defaultValue(600)
        .min(0)
        .sliderRange(0, 600)
        .build()
    );

    private final Setting<Mode> totemMode = sgTotemPops.add(new EnumSetting.Builder<Mode>()
        .name("mode")
        .description("Determines what messages to use.")
        .defaultValue(Mode.KultKlient)
        .build()
    );

    private final Setting<Boolean> totemIgnoreFriends = sgTotemPops.add(new BoolSetting.Builder()
        .name("ignore-friends")
        .description("Ignores friends.")
        .defaultValue(true)
        .build()
    );

    private final Setting<List<String>> totemMessages = sgTotemPops.add(new StringListSetting.Builder()
        .name("messages")
        .description("Custom messages when you pop someone.")
        .defaultValue(Arrays.asList(
            "%player% just lost 1 totem thanks to my skill!",
            "I just ended %player%'s totem!",
            "I just popped %player%!",
            "I just easily popped %player%!"
        ))
        .visible(() -> totemMode.get() == Mode.Custom)
        .build()
    );

    public AutoEZ() {
        super(Categories.Chat, Items.LIGHTNING_ROD, "auto-ez", "Announces EASY or GG when you kill or pop someone.");
    }

    @Override
    public void onActivate() {
        canSendPop = true;
        ticks = 0;
    }

    @EventHandler
    public void onPacketReadMessage(PacketEvent.Receive event) {
        if (mc.player == null || mc.world == null) return;
        if (!kills.get() || killMessages.get().isEmpty() && killMode.get() == Mode.Custom) return;

        if (event.packet instanceof GameMessageS2CPacket) {
            String msg = ((GameMessageS2CPacket) event.packet).getMessage().getString();
            for (PlayerEntity player : mc.world.getPlayers()) {
                if (player == mc.player) return;
                if (mc.player.isCreative() || mc.player.isSpectator()) return;
                if (killIgnoreFriends.get() && Friends.get().isFriend(player)) return;
                if (player.getGameProfile().getName().equals(mc.player.getGameProfile().getName())) return; // TODO: People say it sometimes says their own nickname idfk why.

                String message = getMessageStyle();
                if (msg.contains(player.getName().getString())) {
                    if (msg.contains("by " + mc.getSession().getUsername()) || msg.contains("whilst fighting " + mc.getSession().getUsername()) || msg.contains(mc.getSession().getUsername() + " sniped") || msg.contains(mc.getSession().getUsername() + " annaly fucked") || msg.contains(mc.getSession().getUsername() + " destroyed") || msg.contains(mc.getSession().getUsername() + " killed") || msg.contains(mc.getSession().getUsername() + " fucked") || msg.contains(mc.getSession().getUsername() + " separated") || msg.contains(mc.getSession().getUsername() + " punched") || msg.contains(mc.getSession().getUsername() + " shoved")) {
                        if (msg.contains("end crystal") || msg.contains("end-crystal")) {
                            if (Modules.get().isActive(CrystalAura.class) && mc.player.distanceTo(player) < Modules.get().get(CrystalAura.class).targetRange.get()) mc.player.sendChatMessage(Placeholders.apply(message).replace("%player%", player.getName().getString()));
                            else if (Modules.get().isActive(PistonAura.class) && mc.player.distanceTo(player) < Modules.get().get(PistonAura.class).targetRange.get()) mc.player.sendChatMessage(Placeholders.apply(message).replace("%player%", player.getName().getString()));
                            else if (Modules.get().isActive(CEVBreaker.class) && mc.player.distanceTo(player) < Modules.get().get(CEVBreaker.class).targetRange.get()) mc.player.sendChatMessage(Placeholders.apply(message).replace("%player%", player.getName().getString()));
                            else if (mc.player.distanceTo(player) < 7) mc.player.sendChatMessage(Placeholders.apply(message).replace("%player%", player.getName().getString()));
                        } else {
                            if (Modules.get().isActive(KillAura.class) && mc.player.distanceTo(player) < Modules.get().get(KillAura.class).targetRange.get()) mc.player.sendChatMessage(Placeholders.apply(message).replace("%player%", player.getName().getString()));
                            else if (mc.player.distanceTo(player) < 8) mc.player.sendChatMessage(Placeholders.apply(message).replace("%player%", player.getName().getString()));
                        }
                    } else {
                        if ((msg.contains("bed") || msg.contains("[Intentional Game Design]")) && Modules.get().isActive(BedAura.class) && mc.player.distanceTo(player) < Modules.get().get(BedAura.class).targetRange.get()) mc.player.sendChatMessage(Placeholders.apply(message).replace("%player%", player.getName().getString()));
                        else if ((msg.contains("anchor") || msg.contains("[Intentional Game Design]")) && Modules.get().isActive(AnchorAura.class) && mc.player.distanceTo(player) < Modules.get().get(AnchorAura.class).targetRange.get()) mc.player.sendChatMessage(Placeholders.apply(message).replace("%player%", player.getName().getString()));
                    }
                }
            }
        }
    }

    public String getMessageStyle() {
        return switch (killMode.get()) {
            case KultKlient -> switch (killMessageStyle.get()) {
                case EZ -> getMessages().get(random.nextInt(getMessages().size()));
                case GG -> getGGMessages().get(random.nextInt(getGGMessages().size()));
            };
            case Custom -> killMessages.get().get(random.nextInt(killMessages.get().size()));
        };
    }

    private static List<String> getMessages() {
        return Arrays.asList(
            "%player% just got raped by KultKlient!",
            "%player% just got ended by KultKlient!",
            "haha %player% is a noob! KultKlient on top!",
            "I just EZZz'd %player% using KultKlient!",
            "I just fucked %player% using KultKlient!",
            "Take the L nerd %player%! You just got ended by KultKlient!",
            "I just nae nae'd %player% using KultKlient!",
            "I am too good for %player%! KultKlient on top!"
        );
    }

    private static List<String> getGGMessages() {
        return Arrays.asList(
            "GG %player%! KultKlient is so op!",
            "Nice fight but KultKlient is better, %player%! I really enjoyed it!",
            "Close fight %player%, but KultKlient won!",
            "Good fight, %player%! KultKlient on top!"
        );
    }

    @EventHandler
    private void onReceivePacket(PacketEvent.Receive event) {
        if (mc.player == null || mc.world == null) return;
        if (!totems.get() || totemMessages.get().isEmpty() && totemMode.get() == Mode.Custom) return;

        if (!(event.packet instanceof EntityStatusS2CPacket)) return;
        EntityStatusS2CPacket packet = (EntityStatusS2CPacket) event.packet;
        if (packet.getStatus() != 35) return;

        Entity entity = packet.getEntity(mc.world);
        if (!(entity instanceof PlayerEntity player)) return;

        if (player == mc.player) return;
        if (mc.player.distanceTo(player) > 8) return;
        if (mc.player.isCreative() || mc.player.isSpectator()) return;
        if (totemIgnoreFriends.get() && Friends.get().isFriend(player)) return;
        if (player.getGameProfile().getName().equals(mc.player.getGameProfile().getName())) return; // TODO: People say it sometimes says their own nickname idfk why.

        if (canSendPop) {
            mc.player.sendChatMessage(Placeholders.apply(getTotemMessageStyle()).replace("%player%", player.getName().getString()));
            canSendPop = false;
        }
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (ticks >= delay.get()) {
            canSendPop = true;
            ticks = 0;
        }

        if (!canSendPop) ticks++;
    }

    public String getTotemMessageStyle() {
        return switch (totemMode.get()) {
            case KultKlient -> getTotemMessages().get(random.nextInt(getTotemMessages().size()));
            case Custom -> totemMessages.get().get(random.nextInt(totemMessages.get().size()));
        };
    }

    private static List<String> getTotemMessages() {
        return Arrays.asList(
            "%player% just got popped by KultKlient!",
            "Keep popping %player%! KultKlient owns you!",
            "%player%'s totem just got ended by KultKlient!",
            "%player% just lost 1 totem thanks to KultKlient!",
            "I just easily popped %player% using KultKlient!"
        );
    }

    public enum Mode {
        KultKlient("KultKlient"),
        Custom("Custom");

        private final String title;

        Mode(String title) {
            this.title = title;
        }

        @Override
        public String toString() {
            return title;
        }
    }

    public enum MessageStyle {
        EZ("EZ"),
        GG("GG");

        private final String title;

        MessageStyle(String title) {
            this.title = title;
        }

        @Override
        public String toString() {
            return title;
        }
    }
}
