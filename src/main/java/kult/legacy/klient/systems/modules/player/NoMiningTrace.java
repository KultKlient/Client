package kult.legacy.klient.systems.modules.player;

import kult.legacy.klient.settings.BoolSetting;
import kult.legacy.klient.settings.Setting;
import kult.legacy.klient.settings.SettingGroup;
import kult.legacy.klient.systems.modules.Categories;
import kult.legacy.klient.systems.modules.Module;
import net.minecraft.item.Items;
import net.minecraft.item.PickaxeItem;

public class NoMiningTrace extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> onlyWhenHoldingPickaxe = sgGeneral.add(new BoolSetting.Builder()
        .name("only-when-holding-a-pickaxe")
        .description("Whether or not to work only when holding a pickaxe.")
        .defaultValue(true)
        .build()
    );

    public NoMiningTrace() {
        super(Categories.Player, Items.DIAMOND_PICKAXE, "no-mining-trace", "Allows you to mine blocks through entities.");
    }

    public boolean canWork() {
        if (!isActive()) return false;

        if (onlyWhenHoldingPickaxe.get()) {
            return mc.player.getMainHandStack().getItem() instanceof PickaxeItem || mc.player.getOffHandStack().getItem() instanceof PickaxeItem;
        }

        return true;
    }
}
