package kult.klient.systems.modules.world;

import kult.klient.KultKlient;
import kult.klient.eventbus.EventHandler;
import kult.klient.events.render.Render3DEvent;
import kult.klient.events.world.TickEvent;
import kult.klient.renderer.ShapeMode;
import kult.klient.settings.*;
import kult.klient.systems.modules.Categories;
import kult.klient.systems.modules.Module;
import kult.klient.utils.render.color.SettingColor;
import kult.klient.utils.world.BlockUtils;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;

public class AirPlace extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    // General

    private final Setting<Boolean> render = sgGeneral.add(new BoolSetting.Builder()
        .name("render")
        .description("Renders a block overlay where the obsidian will be placed.")
        .defaultValue(true)
        .build()
    );

    private final Setting<ShapeMode> shapeMode = sgGeneral.add(new EnumSetting.Builder<ShapeMode>()
        .name("shape-mode")
        .description("Determines how the shapes are rendered.")
        .defaultValue(ShapeMode.Both)
        .build()
    );

    private final Setting<SettingColor> sideColor = sgGeneral.add(new ColorSetting.Builder()
        .name("side-color")
        .description("The color of the sides of the blocks being rendered.")
        .defaultValue(new SettingColor(KultKlient.INSTANCE.KULTKLIENT_COLOR.r, KultKlient.INSTANCE.KULTKLIENT_COLOR.g, KultKlient.INSTANCE.KULTKLIENT_COLOR.b, 75))
        .build()
    );

    private final Setting<SettingColor> lineColor = sgGeneral.add(new ColorSetting.Builder()
        .name("line-color")
        .description("The color of the lines of the blocks being rendered.")
        .defaultValue(new SettingColor(KultKlient.INSTANCE.KULTKLIENT_COLOR.r, KultKlient.INSTANCE.KULTKLIENT_COLOR.g, KultKlient.INSTANCE.KULTKLIENT_COLOR.b, 255))
        .build()
    );

    public AirPlace() {
        super(Categories.World, Items.BARRIER, "air-place", "Places a block where your crosshair is pointing at.");
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (!(mc.crosshairTarget instanceof BlockHitResult) || !(mc.player.getMainHandStack().getItem() instanceof BlockItem)) return;
        if (mc.options.keyUse.isPressed()) BlockUtils.place(((BlockHitResult) mc.crosshairTarget).getBlockPos(), Hand.MAIN_HAND, mc.player.getInventory().selectedSlot, false, 0, true, true, false);
    }

    @EventHandler
    private void onRender3D(Render3DEvent event) {
        if (!(mc.crosshairTarget instanceof BlockHitResult) || !mc.world.getBlockState(((BlockHitResult) mc.crosshairTarget).getBlockPos()).getMaterial().isReplaceable() || !(mc.player.getMainHandStack().getItem() instanceof BlockItem) || !render.get()) return;
        event.renderer.box(((BlockHitResult) mc.crosshairTarget).getBlockPos(), sideColor.get(), lineColor.get(), shapeMode.get(), 0);
    }
}
