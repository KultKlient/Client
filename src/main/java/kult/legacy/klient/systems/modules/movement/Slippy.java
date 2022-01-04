package kult.legacy.klient.systems.modules.movement;

import kult.legacy.klient.settings.BlockListSetting;
import kult.legacy.klient.settings.DoubleSetting;
import kult.legacy.klient.settings.Setting;
import kult.legacy.klient.settings.SettingGroup;
import kult.legacy.klient.systems.modules.Categories;
import kult.legacy.klient.systems.modules.Module;
import net.minecraft.block.Block;
import net.minecraft.item.Items;

import java.util.List;

public class Slippy extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    public final Setting<Double> slippness = sgGeneral.add(new DoubleSetting.Builder()
        .name("slippness")
        .description("Decide how slippery blocks should be")
        .min(0.0)
        .max(1.10)
        .sliderMax(1.10)
        .defaultValue(1.02)
        .build()
    );

    public final Setting<List<Block>> blocks = sgGeneral.add(new BlockListSetting.Builder()
        .name("ignored blocks")
        .description("Decide which blocks not to slip on")
        .build()
    );

    public Slippy() {
        super(Categories.Movement, Items.BLUE_ICE, "slippy", "Makes blocks slippery like ice.");
    }
}
