package kult.legacy.klient.systems.modules.render;

import kult.legacy.klient.KultKlientLegacy;
import kult.legacy.klient.settings.ColorSetting;
import kult.legacy.klient.settings.ItemListSetting;
import kult.legacy.klient.settings.Setting;
import kult.legacy.klient.settings.SettingGroup;
import kult.legacy.klient.systems.modules.Categories;
import kult.legacy.klient.systems.modules.Module;
import kult.legacy.klient.utils.render.color.SettingColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.List;

public class ItemHighlight extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<List<Item>> items = sgGeneral.add(new ItemListSetting.Builder()
        .name("items")
        .description("Items to highlight.")
        .build()
    );

    private final Setting<SettingColor> color = sgGeneral.add(new ColorSetting.Builder()
        .name("color")
        .description("The color to highlight the items with.")
        .defaultValue(new SettingColor(KultKlientLegacy.INSTANCE.KULTKLIENT_COLOR.r, KultKlientLegacy.INSTANCE.KULTKLIENT_COLOR.g, KultKlientLegacy.INSTANCE.KULTKLIENT_COLOR.b, 50))
        .build()
    );

    public ItemHighlight() {
        super(Categories.Render, Items.PURPLE_STAINED_GLASS_PANE, "item-highlight", "Highlights selected items when in GUIs.");
    }

    public int getColor(ItemStack stack) {
        if (items.get().contains(stack.getItem()) && isActive()) return color.get().getPacked();
        return -1;
    }
}
