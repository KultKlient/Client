package kult.legacy.klient.systems.modules.render.hud.modules;

import kult.legacy.klient.settings.DoubleSetting;
import kult.legacy.klient.settings.Setting;
import kult.legacy.klient.settings.SettingGroup;
import kult.legacy.klient.systems.modules.render.hud.HUD;
import kult.legacy.klient.systems.modules.render.hud.HudElement;
import kult.legacy.klient.systems.modules.render.hud.HudRenderer;
import kult.legacy.klient.utils.player.InvUtils;
import kult.legacy.klient.utils.render.RenderUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class CrystalHud extends HudElement {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    // General

    private final Setting<Double> scale = sgGeneral.add(new DoubleSetting.Builder()
        .name("scale")
        .description("The scale.")
        .defaultValue(2)
        .min(1)
        .sliderRange(1, 5)
        .build()
    );

    public CrystalHud(HUD hud) {
        super(hud, "crystal", "Displays the amount of crystals in your inventory.", true);
    }

    @Override
    public void update(HudRenderer renderer) {
        box.setSize(16 * scale.get(), 16 * scale.get());
    }

    @Override
    public void render(HudRenderer renderer) {
        double x = box.getX();
        double y = box.getY();

        if (isInEditor()) RenderUtils.drawItem(Items.END_CRYSTAL.getDefaultStack(), (int) x, (int) y, scale.get(), true);
        else if (InvUtils.find(Items.END_CRYSTAL).getCount() > 0) RenderUtils.drawItem(new ItemStack(Items.END_CRYSTAL, InvUtils.find(Items.END_CRYSTAL).getCount()), (int) x, (int) y, scale.get(), true);
    }
}
