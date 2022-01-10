package kult.klient.systems.hud.modules;

import kult.klient.settings.DoubleSetting;
import kult.klient.settings.Setting;
import kult.klient.settings.SettingGroup;
import kult.klient.utils.player.InvUtils;
import kult.klient.utils.render.RenderUtils;
import kult.klient.systems.hud.HUD;
import kult.klient.systems.hud.HudElement;
import kult.klient.systems.hud.HudRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class XPBottleHud extends HudElement {
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

    public XPBottleHud(HUD hud) {
        super(hud, "xp-bottle", "Displays the amount of xp bottles in your inventory.", true);
    }

    @Override
    public void update(HudRenderer renderer) {
        box.setSize(16 * scale.get(), 16 * scale.get());
    }

    @Override
    public void render(HudRenderer renderer) {
        double x = box.getX();
        double y = box.getY();

        if (isInEditor()) RenderUtils.drawItem(Items.EXPERIENCE_BOTTLE.getDefaultStack(), (int) x, (int) y, scale.get(), true);
        else if (InvUtils.find(Items.EXPERIENCE_BOTTLE).getCount() > 0) RenderUtils.drawItem(new ItemStack(Items.EXPERIENCE_BOTTLE, InvUtils.find(Items.EXPERIENCE_BOTTLE).getCount()), (int) x, (int) y, scale.get(), true);
    }
}
