package kult.legacy.klient.systems.modules.render;

import kult.legacy.klient.KultKlientLegacy;
import kult.legacy.klient.settings.ColorSetting;
import kult.legacy.klient.settings.Setting;
import kult.legacy.klient.settings.SettingGroup;
import kult.legacy.klient.systems.modules.Categories;
import kult.legacy.klient.systems.modules.Module;
import kult.legacy.klient.utils.render.color.SettingColor;
import net.minecraft.item.Items;
import net.minecraft.util.math.Vec3d;

public class Confetti extends Module {
    private final SettingGroup sgGeneral = settings.createGroup("Colors");

    // Colors

    private final Setting<SettingColor> colorOne = sgGeneral.add(new ColorSetting.Builder()
        .name("first")
        .description("The first confetti color to change.")
        .defaultValue(new SettingColor(KultKlientLegacy.INSTANCE.KULTKLIENT_COLOR.r, KultKlientLegacy.INSTANCE.KULTKLIENT_COLOR.g, KultKlientLegacy.INSTANCE.KULTKLIENT_COLOR.b))
        .build()
    );

    private final Setting<SettingColor> colorTwo = sgGeneral.add(new ColorSetting.Builder()
        .name("second")
        .description("The second confetti color to change.")
        .defaultValue(new SettingColor(KultKlientLegacy.INSTANCE.KULTKLIENT_BACKGROUND_COLOR.r, KultKlientLegacy.INSTANCE.KULTKLIENT_BACKGROUND_COLOR.g, KultKlientLegacy.INSTANCE.KULTKLIENT_BACKGROUND_COLOR.b))
        .build()
    );

    public Confetti() {
        super(Categories.Render, Items.TOTEM_OF_UNDYING, "Confetti", "Changes the color of the totem pop particles.");
    }

    public Vec3d getColorOne() {
        return getDoubleVectorColor(colorOne);
    }

    public Vec3d getColorTwo() {
        return getDoubleVectorColor(colorTwo);
    }

    public Vec3d getDoubleVectorColor(Setting<SettingColor> colorSetting) {
        return new Vec3d(colorSetting.get().r / 255.0, colorSetting.get().g / 255.0, colorSetting.get().b / 255.0);
    }
}
