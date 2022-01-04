package kult.legacy.klient.systems.modules.movement;

import kult.legacy.klient.events.entity.player.CobwebEntityCollisionEvent;
import kult.legacy.klient.events.world.TickEvent;
import kult.legacy.klient.systems.modules.Categories;
import kult.legacy.klient.systems.modules.Module;
import kult.legacy.klient.systems.modules.Modules;
import kult.legacy.klient.systems.modules.world.Timer;
import kult.legacy.klient.eventbus.EventHandler;
import kult.legacy.klient.settings.*;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;

public class NoSlow extends Module {
    private boolean usedTimer;

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> items = sgGeneral.add(new BoolSetting.Builder()
        .name("items")
        .description("Whether or not using items will slow you.")
        .defaultValue(true)
        .build()
    );

    public final Setting<WebMode> web = sgGeneral.add(new EnumSetting.Builder<WebMode>()
        .name("web")
        .description("Whether or not cobwebs will not slow you down.")
        .defaultValue(WebMode.Vanilla)
        .build()
    );

    public final Setting<Integer> webTimer = sgGeneral.add(new IntSetting.Builder()
        .name("web-timer")
        .description("The timer value for WebMode Timer.")
        .defaultValue(10)
        .min(1)
        .sliderMin(1)
        .visible(() -> web.get() == WebMode.Timer)
        .build()
    );

    private final Setting<Boolean> soulSand = sgGeneral.add(new BoolSetting.Builder()
        .name("soul-sand")
        .description("Whether or not Soul Sand will not slow you down.")
        .defaultValue(true)
        .build()
    );

    private final Setting<Boolean> slimeBlock = sgGeneral.add(new BoolSetting.Builder()
        .name("slime-block")
        .description("Whether or not slime blocks will not slow you down.")
        .defaultValue(true)
        .build()
    );

    private final Setting<Boolean> airStrict = sgGeneral.add(new BoolSetting.Builder()
        .name("air-strict")
        .description("Will attempt to bypass anti-cheats like 2b2t's. Only works while in air.")
        .defaultValue(false)
        .build()
    );

    private final Setting<Boolean> sneaking = sgGeneral.add(new BoolSetting.Builder()
        .name("sneaking")
        .description("Whether or not sneaking will not slow you down.")
        .defaultValue(false)
        .build()
    );

    public NoSlow() {
        super(Categories.Movement, Items.DIAMOND_BOOTS, "no-slow", "Allows you to move normally when using objects that will slow you.");
    }

    public boolean airStrict() {
        return isActive() && airStrict.get() && mc.player.isUsingItem();
    }

    public boolean items() {
        return isActive() && items.get();
    }

    public boolean soulSand() {
        return isActive() && soulSand.get();
    }

    public boolean slimeBlock() {
        return isActive() && slimeBlock.get();
    }

    public boolean sneaking() {
        return isActive() && sneaking.get();
    }

    @EventHandler
    private void onWebEntityCollision(CobwebEntityCollisionEvent event) {
        if (web.get() != WebMode.None) {
            switch (web.get()) {
                case Vanilla -> event.cancel();
                case Timer -> {
                    if (!mc.player.isOnGround()) {
                        if (event.state.getBlock() == Blocks.COBWEB) {
                            Modules.get().get(Timer.class).setOverride(webTimer.get());
                            usedTimer = true;
                        }
                    } else {
                        Modules.get().get(Timer.class).setOverride(Timer.OFF);
                        usedTimer = false;
                    }
                }
            }
        }
    }

    @EventHandler
    private void onPreTick(TickEvent.Pre event) {
        if (usedTimer && mc.world.getBlockState(mc.player.getBlockPos()).getBlock() != Blocks.COBWEB) {
            Modules.get().get(Timer.class).setOverride(Timer.OFF);
            usedTimer = false;
        }
    }

    public enum WebMode {
        Vanilla,
        Timer,
        None
    }
}
