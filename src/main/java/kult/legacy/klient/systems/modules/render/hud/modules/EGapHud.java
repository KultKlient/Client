package kultklient.legacy.client.systems.modules.render.hud.modules;

import kultklient.legacy.client.settings.DoubleSetting;
import kultklient.legacy.client.settings.Setting;
import kultklient.legacy.client.settings.SettingGroup;
import kultklient.legacy.client.systems.modules.render.hud.HUD;
import kultklient.legacy.client.systems.modules.render.hud.HudElement;
import kultklient.legacy.client.systems.modules.render.hud.HudRenderer;
import kultklient.legacy.client.utils.player.InvUtils;
import kultklient.legacy.client.utils.render.RenderUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class EGapHud extends HudElement {
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

    public EGapHud(HUD hud) {
        super(hud, "e-gap", "Displays the amount of e-gaps in your inventory.", true);
    }

    @Override
    public void update(HudRenderer renderer) {
        box.setSize(16 * scale.get(), 16 * scale.get());
    }

    @Override
    public void render(HudRenderer renderer) {
        double x = box.getX();
        double y = box.getY();

        if (isInEditor()) RenderUtils.drawItem(Items.ENCHANTED_GOLDEN_APPLE.getDefaultStack(), (int) x, (int) y, scale.get(), true);
        else if (InvUtils.find(Items.ENCHANTED_GOLDEN_APPLE).getCount() > 0) RenderUtils.drawItem(new ItemStack(Items.ENCHANTED_GOLDEN_APPLE, InvUtils.find(Items.ENCHANTED_GOLDEN_APPLE).getCount()), (int) x, (int) y, scale.get(), true);
    }
}
