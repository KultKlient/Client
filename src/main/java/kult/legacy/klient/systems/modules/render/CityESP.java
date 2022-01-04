package kult.legacy.klient.systems.modules.render;

import kult.legacy.klient.KultKlientLegacy;
import kult.legacy.klient.events.render.Render3DEvent;
import kult.legacy.klient.events.world.TickEvent;
import kult.legacy.klient.settings.ColorSetting;
import kult.legacy.klient.settings.EnumSetting;
import kult.legacy.klient.settings.Setting;
import kult.legacy.klient.settings.SettingGroup;
import kult.legacy.klient.renderer.ShapeMode;
import kult.legacy.klient.systems.modules.Categories;
import kult.legacy.klient.systems.modules.Module;
import kult.legacy.klient.utils.entity.EntityUtils;
import kult.legacy.klient.utils.entity.SortPriority;
import kult.legacy.klient.utils.entity.TargetUtils;
import kult.legacy.klient.utils.render.color.SettingColor;
import kult.legacy.klient.eventbus.EventHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;

public class CityESP extends Module {
    private BlockPos target;

    private final SettingGroup sgRender = settings.createGroup("Render");

    // Render

    private final Setting<ShapeMode> shapeMode = sgRender.add(new EnumSetting.Builder<ShapeMode>()
        .name("shape-mode")
        .description("Determines how the shapes are rendered.")
        .defaultValue(ShapeMode.Both)
        .build()
    );

    private final Setting<SettingColor> sideColor = sgRender.add(new ColorSetting.Builder()
        .name("side-color")
        .description("The side color of the rendering.")
        .defaultValue(new SettingColor(KultKlientLegacy.INSTANCE.MATHAX_COLOR.r, KultKlientLegacy.INSTANCE.MATHAX_COLOR.g, KultKlientLegacy.INSTANCE.MATHAX_COLOR.b, 75))
        .build()
    );

    private final Setting<SettingColor> lineColor = sgRender.add(new ColorSetting.Builder()
        .name("line-color")
        .description("The line color of the rendering.")
        .defaultValue(new SettingColor(KultKlientLegacy.INSTANCE.MATHAX_COLOR.r, KultKlientLegacy.INSTANCE.MATHAX_COLOR.g, KultKlientLegacy.INSTANCE.MATHAX_COLOR.b, 255))
        .build()
    );

    public CityESP() {
        super(Categories.Render, Items.RED_STAINED_GLASS, "city-esp", "Displays blocks that can be broken in order to city another player.");
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        PlayerEntity targetEntity = TargetUtils.getPlayerTarget(mc.interactionManager.getReachDistance() + 2, SortPriority.Lowest_Distance);

        if (TargetUtils.isBadTarget(targetEntity, mc.interactionManager.getReachDistance() + 2)) {
            target = null;
        } else {
            target = EntityUtils.getCityBlock(targetEntity);
        }
    }

    @EventHandler
    private void onRender3D(Render3DEvent event) {
        if (target == null) return;

        event.renderer.box(target, sideColor.get(), lineColor.get(), shapeMode.get(), 0);
    }
}
