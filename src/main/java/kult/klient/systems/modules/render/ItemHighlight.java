package kult.klient.systems.modules.render;

import kult.klient.KultKlient;
import kult.klient.settings.ColorSetting;
import kult.klient.settings.ItemListSetting;
import kult.klient.settings.Setting;
import kult.klient.settings.SettingGroup;
import kult.klient.systems.modules.Categories;
import kult.klient.systems.modules.Module;
import kult.klient.utils.render.color.SettingColor;
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
        .defaultValue(new SettingColor(KultKlient.INSTANCE.KULTKLIENT_COLOR.r, KultKlient.INSTANCE.KULTKLIENT_COLOR.g, KultKlient.INSTANCE.KULTKLIENT_COLOR.b, 50))
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
