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

public class ObsidianHud extends HudElement {
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

    public ObsidianHud(HUD hud) {
        super(hud, "obsidian", "Displays the amount of obsidian in your inventory.", true);
    }

    @Override
    public void update(HudRenderer renderer) {
        box.setSize(16 * scale.get(), 16 * scale.get());
    }

    @Override
    public void render(HudRenderer renderer) {
        double x = box.getX();
        double y = box.getY();

        if (isInEditor()) RenderUtils.drawItem(Items.OBSIDIAN.getDefaultStack(), (int) x, (int) y, scale.get(), true);
        else if (InvUtils.find(Items.OBSIDIAN).getCount() > 0) RenderUtils.drawItem(new ItemStack(Items.OBSIDIAN, InvUtils.find(Items.OBSIDIAN).getCount()), (int) x, (int) y, scale.get(), true);
    }
}
