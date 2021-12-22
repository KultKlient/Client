package kultklient.legacy.client.systems.modules.misc;

import kultklient.legacy.client.eventbus.EventHandler;
import kultklient.legacy.client.events.world.TickEvent;
import kultklient.legacy.client.settings.IntSetting;
import kultklient.legacy.client.settings.Setting;
import kultklient.legacy.client.settings.SettingGroup;
import kultklient.legacy.client.systems.modules.Categories;
import kultklient.legacy.client.systems.modules.Module;
import kultklient.legacy.client.systems.modules.Modules;
import kultklient.legacy.client.systems.modules.player.EXPThrower;
import kultklient.legacy.client.utils.player.Rotations;
import net.minecraft.item.BowItem;
import net.minecraft.item.EnderPearlItem;
import net.minecraft.item.ExperienceBottleItem;
import net.minecraft.item.Items;

public class SpinBot extends Module {
    private short count;

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    // General

    private final Setting<Integer> speed = sgGeneral.add(new IntSetting.Builder()
        .name("spin-speed")
        .description("The speed at which you spin.")
        .defaultValue(25)
        .min(0)
        .sliderMax(100)
        .build()
    );

    public SpinBot() {
        super(Categories.Misc, Items.GUNPOWDER, "spin-bot", "Makes you spin like Spin bot in CS:GO.");
        count = 0;
    }

    @EventHandler
    public void onTick(TickEvent.Post post) {
        if (Modules.get().isActive(EXPThrower.class) || mc.player.getMainHandStack().getItem() instanceof ExperienceBottleItem || mc.player.getMainHandStack().getItem() instanceof ExperienceBottleItem || mc.player.getMainHandStack().getItem() instanceof EnderPearlItem || mc.player.getMainHandStack().getItem() instanceof EnderPearlItem || mc.player.getMainHandStack().getItem() instanceof ExperienceBottleItem || mc.player.getMainHandStack().getItem() instanceof ExperienceBottleItem || mc.player.getMainHandStack().getItem() instanceof BowItem || mc.player.getMainHandStack().getItem() instanceof BowItem || mc.player.getInventory().getArmorStack(2).getItem() == Items.ELYTRA) {
            return;
        }
        count = (short)(count + speed.get());
        if (count > 180) {
            count = (short)-180;
        }
        Rotations.rotate(count, 0.0);
    }
}
