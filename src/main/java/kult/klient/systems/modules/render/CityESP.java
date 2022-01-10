package kult.klient.systems.modules.render;

import kult.klient.KultKlient;
import kult.klient.eventbus.EventHandler;
import kult.klient.events.render.Render3DEvent;
import kult.klient.events.world.TickEvent;
import kult.klient.renderer.ShapeMode;
import kult.klient.settings.ColorSetting;
import kult.klient.settings.EnumSetting;
import kult.klient.settings.Setting;
import kult.klient.settings.SettingGroup;
import kult.klient.systems.modules.Categories;
import kult.klient.systems.modules.Module;
import kult.klient.utils.entity.EntityUtils;
import kult.klient.utils.entity.SortPriority;
import kult.klient.utils.entity.TargetUtils;
import kult.klient.utils.render.color.SettingColor;
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
        .defaultValue(new SettingColor(KultKlient.INSTANCE.KULTKLIENT_COLOR.r, KultKlient.INSTANCE.KULTKLIENT_COLOR.g, KultKlient.INSTANCE.KULTKLIENT_COLOR.b, 75))
        .build()
    );

    private final Setting<SettingColor> lineColor = sgRender.add(new ColorSetting.Builder()
        .name("line-color")
        .description("The line color of the rendering.")
        .defaultValue(new SettingColor(KultKlient.INSTANCE.KULTKLIENT_COLOR.r, KultKlient.INSTANCE.KULTKLIENT_COLOR.g, KultKlient.INSTANCE.KULTKLIENT_COLOR.b, 255))
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
