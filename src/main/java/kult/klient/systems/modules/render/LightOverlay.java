package kult.klient.systems.modules.render;

import kult.klient.eventbus.EventHandler;
import kult.klient.events.render.Render3DEvent;
import kult.klient.events.world.TickEvent;
import kult.klient.renderer.DrawMode;
import kult.klient.renderer.Mesh;
import kult.klient.renderer.ShaderMesh;
import kult.klient.renderer.Shaders;
import kult.klient.settings.*;
import kult.klient.systems.modules.Categories;
import kult.klient.systems.modules.Module;
import kult.klient.utils.misc.Pool;
import kult.klient.utils.render.color.Color;
import kult.klient.utils.render.color.SettingColor;
import kult.klient.utils.world.BlockIterator;
import kult.klient.utils.world.BlockUtils;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class LightOverlay extends Module {
    private final Pool<Cross> crossPool = new Pool<>(Cross::new);
    private final List<Cross> crosses = new ArrayList<>();

    private final BlockPos.Mutable bp = new BlockPos.Mutable();

    private final Mesh mesh = new ShaderMesh(Shaders.POS_COLOR, DrawMode.Lines, Mesh.Attrib.Vec3, Mesh.Attrib.Color);

    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final SettingGroup sgColors = settings.createGroup("Colors");

    // General

    private final Setting<Integer> horizontalRange = sgGeneral.add(new IntSetting.Builder()
        .name("horizontal-range")
        .description("Horizontal range in blocks.")
        .defaultValue(8)
        .min(0)
        .build()
    );

    private final Setting<Integer> verticalRange = sgGeneral.add(new IntSetting.Builder()
        .name("vertical-range")
        .description("Vertical range in blocks.")
        .defaultValue(4)
        .min(0)
        .build()
    );

    private final Setting<Boolean> seeThroughBlocks = sgGeneral.add(new BoolSetting.Builder()
        .name("see-through-blocks")
        .description("Allows you to see the lines through blocks.")
        .defaultValue(false)
        .build()
    );

    private final Setting<Boolean> newMobSpawnLightLevel = sgGeneral.add(new BoolSetting.Builder()
        .name("new-mob-spawn-light-level")
        .description("Use the new (1.18+) mob spawn behavior.")
        .defaultValue(true)
        .build()
    );

    // Colors

    private final Setting<SettingColor> color = sgColors.add(new ColorSetting.Builder()
        .name("color")
        .description("Color of places where mobs can currently spawn.")
        .defaultValue(new SettingColor(225, 25, 25))
        .build()
    );

    private final Setting<SettingColor> potentialColor = sgColors.add(new ColorSetting.Builder()
        .name("potential-color")
        .description("Color of places where mobs can potentially spawn (eg at night).")
        .defaultValue(new SettingColor(225, 225, 25))
        .build()
    );

    public LightOverlay() {
        super(Categories.Render, Items.YELLOW_STAINED_GLASS, "light-overlay", "Shows blocks where mobs can spawn.");
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        for (Cross cross : crosses) crossPool.free(cross);
        crosses.clear();

        BlockIterator.register(horizontalRange.get(), verticalRange.get(), (blockPos, blockState) -> {
            switch (BlockUtils.isValidMobSpawn(blockPos, newMobSpawnLightLevel.get())) {
                case Never:
                    break;
                case Potential:
                    crosses.add(crossPool.get().set(blockPos, true));
                    break;
                case Always:
                    crosses.add((crossPool.get().set(blockPos, false)));
                    break;
            }
        });
    }

    @EventHandler
    private void onRender3D(Render3DEvent event) {
        if (crosses.isEmpty()) return;

        mesh.depthTest = !seeThroughBlocks.get();
        mesh.begin();

        for (Cross cross : crosses) cross.render();

        mesh.end();
        mesh.render(event.matrices);
    }

    private void line(double x1, double y1, double z1, double x2, double y2, double z2, Color color) {
        mesh.line(
            mesh.vec3(x1, y1, z1).color(color).next(),
            mesh.vec3(x2, y2, z2).color(color).next()
        );
    }

    private class Cross {
        private double x, y, z;
        private boolean potential;

        public Cross set(BlockPos blockPos, boolean potential) {
            x = blockPos.getX();
            y = blockPos.getY() + 0.0075;
            z = blockPos.getZ();

            this.potential = potential;

            return this;
        }

        public void render() {
            Color c = potential ? potentialColor.get() : color.get();

            line(x, y, z, x + 1, y, z + 1, c);
            line(x + 1, y, z, x, y, z + 1, c);
        }
    }
}
