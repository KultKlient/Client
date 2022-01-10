package kult.klient.systems.modules.movement;

import kult.klient.settings.BlockListSetting;
import kult.klient.settings.DoubleSetting;
import kult.klient.settings.Setting;
import kult.klient.settings.SettingGroup;
import kult.klient.systems.modules.Categories;
import kult.klient.systems.modules.Module;
import net.minecraft.block.Block;
import net.minecraft.item.Items;

import java.util.List;

public class Slippy extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    public final Setting<Double> slippness = sgGeneral.add(new DoubleSetting.Builder()
        .name("slippness")
        .description("Decide how slippery blocks should be")
        .defaultValue(1.02)
        .range(0.0, 1.10)
        .sliderRange(0, 1.10)
        .build()
    );

    public final Setting<List<Block>> blocks = sgGeneral.add(new BlockListSetting.Builder()
        .name("ignored-blocks")
        .description("Decide which blocks not to slip on")
        .build()
    );

    public Slippy() {
        super(Categories.Movement, Items.BLUE_ICE, "slippy", "Makes blocks slippery like ice.");
    }
}
