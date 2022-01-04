package kult.legacy.klient.systems.modules.chat;

import kult.legacy.klient.eventbus.EventHandler;
import kult.legacy.klient.events.packets.PacketEvent;
import kult.legacy.klient.events.world.TickEvent;
import kult.legacy.klient.settings.*;
import kult.legacy.klient.systems.friends.Friends;
import kult.legacy.klient.systems.modules.Categories;
import kult.legacy.klient.systems.modules.Module;
import kult.legacy.klient.systems.modules.Modules;
import kult.legacy.klient.systems.modules.combat.*;
import kult.legacy.klient.utils.entity.EntityUtils;
import kult.legacy.klient.utils.misc.Placeholders;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.*;

import java.util.*;

public class AutoEZ extends Module {
    private int ticks;

    private boolean canSendPop;

    private final Random random = new Random();

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
            "haha %killed_player% is a noob! EZZz",
            "I just raped %killed_player%!",
            "I just ended %killed_player%!",
            "I just EZZz'd %killed_player%!",
            "I just fucked %killed_player%!",
            "Take the L nerd %killed_player%! You just got ended!",
            "I just nae nae'd %killed_player%!",
            "I am too good for %killed_player%!"
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
        .defaultValue(Arrays.asList("%popped_player% just lost 1 totem thanks to my skill!", "I just ended %popped_player%'s totem!", "I just popped %popped_player%!", "I just easily popped %popped_player%!"))
        .visible(() -> totemMode.get() == Mode.Custom)
        .build()
    );

    public AutoEZ() {
        super(Categories.Chat, Items.LIGHTNING_ROD, "auto-ez", "Announces EASY when you kill or pop someone.");
    }

    // Kills

    @EventHandler
    public void onPacketReadMessage(PacketEvent.Receive event) {
        if (!kills.get()) return;
        if (killMessages.get().isEmpty() && killMode.get() == Mode.Custom) return;
        if (event.packet instanceof GameMessageS2CPacket) {
            String msg = ((GameMessageS2CPacket) event.packet).getMessage().getString();
            for (PlayerEntity player : mc.world.getPlayers()) {
                if (player == mc.player)
                    continue;
                if (player.getName().getString().equals(mc.getSession().getUsername())) return;
                if (msg.contains(player.getName().getString())) {
                    if (msg.contains("by " + mc.getSession().getUsername()) || msg.contains("whilst fighting " + mc.getSession().getUsername()) || msg.contains(mc.getSession().getUsername() + " sniped") || msg.contains(mc.getSession().getUsername() + " annaly fucked") || msg.contains(mc.getSession().getUsername() + " destroyed") || msg.contains(mc.getSession().getUsername() + " killed") || msg.contains(mc.getSession().getUsername() + " fucked") || msg.contains(mc.getSession().getUsername() + " separated") || msg.contains(mc.getSession().getUsername() + " punched") || msg.contains(mc.getSession().getUsername() + " shoved")) {
                        if (msg.contains("end crystal") || msg.contains("end-crystal")) {
                            if (Modules.get().isActive(CrystalAura.class)) {
                                if (mc.player.distanceTo(player) < Modules.get().get(CrystalAura.class).targetRange.get()) {
                                    if (killIgnoreFriends.get() && Friends.get().isFriend(player)) return;
                                    if (EntityUtils.getGameMode(player).isCreative()) return;
                                    String message = getMessageStyle();
                                    mc.player.sendChatMessage(Placeholders.apply(message).replace("%killed_player%", player.getName().getString()));
                                }
                            } else if (Modules.get().isActive(PistonAura.class)) {
                                if (mc.player.distanceTo(player) < Modules.get().get(PistonAura.class).targetRange.get()) {
                                    if (killIgnoreFriends.get() && Friends.get().isFriend(player)) return;
                                    if (EntityUtils.getGameMode(player).isCreative()) return;
                                    String message = getMessageStyle();
                                    mc.player.sendChatMessage(Placeholders.apply(message).replace("%killed_player%", player.getName().getString()));
                                }
                            } else if (Modules.get().isActive(CEVBreaker.class)) {
                                if (mc.player.distanceTo(player) < Modules.get().get(CEVBreaker.class).targetRange.get()) {
                                    if (killIgnoreFriends.get() && Friends.get().isFriend(player)) return;
                                    if (EntityUtils.getGameMode(player).isCreative()) return;
                                    String message = getMessageStyle();
                                    mc.player.sendChatMessage(Placeholders.apply(message).replace("%killed_player%", player.getName().getString()));
                                }
                            } else {
                                if (mc.player.distanceTo(player) < 7) {
                                    if (killIgnoreFriends.get() && Friends.get().isFriend(player)) return;
                                    if (EntityUtils.getGameMode(player).isCreative()) return;
                                    String message = getMessageStyle();
                                    mc.player.sendChatMessage(Placeholders.apply(message).replace("%killed_player%", player.getName().getString()));
                                }
                            }
                        } else {
                            if (Modules.get().isActive(KillAura.class)) {
                                if (mc.player.distanceTo(player) < Modules.get().get(KillAura.class).targetRange.get()) {
                                    if (killIgnoreFriends.get() && Friends.get().isFriend(player)) return;
                                    if (EntityUtils.getGameMode(player).isCreative()) return;
                                    String message = getMessageStyle();
                                    mc.player.sendChatMessage(Placeholders.apply(message).replace("%killed_player%", player.getName().getString()));
                                }
                            } else if (mc.player.distanceTo(player) < 8) {
                                if (killIgnoreFriends.get() && Friends.get().isFriend(player)) return;
                                if (EntityUtils.getGameMode(player).isCreative()) return;
                                String message = getMessageStyle();
                                mc.player.sendChatMessage(Placeholders.apply(message).replace("%killed_player%", player.getName().getString()));
                            }
                        }
                    } else {
                        if ((msg.contains("bed") || msg.contains("[Intentional Game Design]")) && (Modules.get().isActive(BedAura.class))) {
                            if ((mc.player.distanceTo(player) < Modules.get().get(BedAura.class).targetRange.get())) {
                                if (killIgnoreFriends.get() && Friends.get().isFriend(player)) return;
                                if (EntityUtils.getGameMode(player).isCreative()) return;
                                String message = getMessageStyle();
                                mc.player.sendChatMessage(Placeholders.apply(message).replace("%killed_player%", player.getName().getString()));
                            }
                        } else if ((msg.contains("anchor") || msg.contains("[Intentional Game Design]")) && Modules.get().isActive(AnchorAura.class)) {
                            if (mc.player.distanceTo(player) < Modules.get().get(AnchorAura.class).targetRange.get()) {
                                if (killIgnoreFriends.get() && Friends.get().isFriend(player)) return;
                                if (EntityUtils.getGameMode(player).isCreative()) return;
                                String message = getMessageStyle();
                                mc.player.sendChatMessage(Placeholders.apply(message).replace("%killed_player%", player.getName().getString()));
                            }
                        }
                    }
                }
            }
        }
    }

    public String getMessageStyle() {
        return switch (killMode.get()) {
            case KultKlient -> switch (killMessageStyle.get()) {
                case EZ -> getMessage().get(random.nextInt(getMessage().size()));
                case GG -> getGGMessage().get(random.nextInt(getGGMessage().size()));
            };
            case Custom -> killMessages.get().get(random.nextInt(killMessages.get().size()));
        };
    }

    private static List<String> getMessage() {
        return Arrays.asList(
            "%killed_player% just got raped by KultKlient Legacy!",
            "%killed_player% just got ended by KultKlient Legacy!",
            "haha %killed_player% is a noob! KultKlient Legacy on top!",
            "I just EZZz'd %killed_player% using KultKlient Legacy!",
            "I just fucked %killed_player% using KultKlient Legacy!",
            "Take the L nerd %killed_player%! You just got ended by KultKlient Legacy!",
            "I just nae nae'd %killed_player% using KultKlient Legacy!",
            "I am too good for %killed_player%! KultKlient Legacy on top!"
        );
    }

    private static List<String> getGGMessage() {
        return Arrays.asList(
            "GG %killed_player%! KultKlient Legacy is so op!",
            "Nice fight %killed_player%! I really enjoyed it!",
            "Close fight %killed_player%, but i won!",
            "Good fight, %killed_player%! KultKlient Legacy on top!"
        );
    }

    // Totem Pops

    @EventHandler
    private void onReceivePacket(PacketEvent.Receive event) {
        if (!totems.get()) return;
        if (totemMessages.get().isEmpty() && totemMode.get() == Mode.Custom) return;
        if (!(event.packet instanceof EntityStatusS2CPacket)) return;

        EntityStatusS2CPacket packet = (EntityStatusS2CPacket) event.packet;
        if (packet.getStatus() != 35) return;

        Entity entity = packet.getEntity(mc.world);

        if (!(entity instanceof PlayerEntity)) return;

        if (entity == mc.player) return;
        if (mc.player.distanceTo(entity) > 8) return;
        if (Friends.get().isFriend((PlayerEntity) entity) && totemIgnoreFriends.get()) return;

        if (EntityUtils.getGameMode(mc.player).isCreative()) return;
        if (canSendPop) {
            String message = getTotemMessageStyle();
            mc.player.sendChatMessage(Placeholders.apply(message).replace("%popped_player%", entity.getName().getString()));
            canSendPop = false;
        }
    }

    @Override
    public void onActivate() {
        canSendPop = true;
        ticks = 0;
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (ticks > delay.get()) {
            canSendPop = true;
            ticks = 0;
        }

        if (!canSendPop) ticks++;
    }

    public String getTotemMessageStyle() {
        return switch (totemMode.get()) {
            case KultKlient -> getTotemMessage().get(random.nextInt(getTotemMessage().size()));
            case Custom -> totemMessages.get().get(random.nextInt(totemMessages.get().size()));
        };
    }

    private static List<String> getTotemMessage() {
        return Arrays.asList(
            "%popped_player% just got popped by KultKlient Legacy!",
            "Keep popping %popped_player%! KultKlient Legacy owns you!",
            "%popped_player%'s totem just got ended by KultKlient Legacy!",
            "%popped_player% just lost 1 totem thanks to KultKlient Legacy!",
            "I just easily popped %popped_player% using KultKlient Legacy!"
        );
    }

    public enum Mode {
        KultKlient,
        Custom
    }

    public enum MessageStyle {
        EZ,
        GG
    }
}
