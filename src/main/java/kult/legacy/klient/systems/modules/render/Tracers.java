package kult.legacy.klient.systems.modules.render;

import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import kult.legacy.klient.KultKlientLegacy;
import kult.legacy.klient.events.render.Render3DEvent;
import kult.legacy.klient.systems.friends.Friends;
import kult.legacy.klient.systems.modules.Categories;
import kult.legacy.klient.systems.modules.Module;
import kult.legacy.klient.systems.modules.Modules;
import kult.legacy.klient.utils.entity.EntityUtils;
import kult.legacy.klient.utils.entity.Target;
import kult.legacy.klient.utils.player.PlayerUtils;
import kult.legacy.klient.utils.render.RenderUtils;
import kult.legacy.klient.utils.render.color.Color;
import kult.legacy.klient.utils.render.color.SettingColor;
import kult.legacy.klient.eventbus.EventHandler;
import kult.legacy.klient.settings.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;

public class Tracers extends Module {
    private final Color distanceColor = new Color(255, 255, 255);

    private int count;

    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final SettingGroup sgAppearance = settings.createGroup("Appearance");
    private final SettingGroup sgColors = settings.createGroup("Colors");

    // General

    private final Setting<Object2BooleanMap<EntityType<?>>> entities = sgGeneral.add(new EntityTypeListSetting.Builder()
        .name("entities")
        .description("Select specific entities.")
        .defaultValue(EntityType.PLAYER)
        .build()
    );

    public final Setting<Boolean> showInvisible = sgGeneral.add(new BoolSetting.Builder()
        .name("show-invisible")
        .description("Shows invisible entities.")
        .defaultValue(true)
        .build()
    );

    private final Setting<Boolean> ignoreFriends = sgGeneral.add(new BoolSetting.Builder()
        .name("ignore-friends")
        .description("Stops tracer rendering for friends.")
        .defaultValue(false)
        .build()
    );

    // Appearance

    private final Setting<Target> target = sgAppearance.add(new EnumSetting.Builder<Target>()
        .name("target")
        .description("What part of the entity to target.")
        .defaultValue(Target.Body)
        .build()
    );

    private final Setting<Boolean> hideHud = sgAppearance.add(new BoolSetting.Builder()
        .name("auto-hide")
        .description("Hide tracers when game HUD is hidden.")
        .defaultValue(true)
        .build()
    );

    private final Setting<Boolean> stem = sgAppearance.add(new BoolSetting.Builder()
        .name("stem")
        .description("Draw a line through the center of the tracer target.")
        .defaultValue(true)
        .build()
    );

    private final Setting<Integer> maxDist = sgAppearance.add(new IntSetting.Builder()
        .name("max-distance")
        .description("Maximum distance for tracers to show.")
        .defaultValue(256)
        .min(0)
        .sliderMax(256)
        .build()
    );

    // Colors

    public final Setting<Boolean> distance = sgColors.add(new BoolSetting.Builder()
        .name("distance-based")
        .description("Changes the color depending on distance.")
        .defaultValue(false)
        .build()
    );

    private final Setting<SettingColor> playersColor = sgColors.add(new ColorSetting.Builder()
        .name("players")
        .description("The other player's color.")
        .defaultValue(new SettingColor(KultKlientLegacy.INSTANCE.KULTKLIENT_COLOR.r, KultKlientLegacy.INSTANCE.KULTKLIENT_COLOR.g, KultKlientLegacy.INSTANCE.KULTKLIENT_COLOR.b))
        .visible(() -> !distance.get())
        .build()
    );


    public final Setting<SettingColor> selfColor = sgColors.add(new ColorSetting.Builder()
        .name("self")
        .description("Your own color.")
        .defaultValue(new SettingColor(0, 165, 255))
        .visible(() -> !distance.get())
        .build()
    );

    private final Setting<SettingColor> animalsColor = sgColors.add(new ColorSetting.Builder()
        .name("animals")
        .description("The animal's color.")
        .defaultValue(new SettingColor(25, 255, 25))
        .visible(() -> !distance.get())
        .build()
    );

    private final Setting<SettingColor> waterAnimalsColor = sgColors.add(new ColorSetting.Builder()
        .name("water-animals")
        .description("The water animal's color.")
        .defaultValue(new SettingColor(25, 25, 255))
        .visible(() -> !distance.get())
        .build()
    );

    private final Setting<SettingColor> monstersColor = sgColors.add(new ColorSetting.Builder()
        .name("monsters")
        .description("The monster's color.")
        .defaultValue(new SettingColor(255, 25, 25))
        .visible(() -> !distance.get())
        .build()
    );

    private final Setting<SettingColor> ambientColor = sgColors.add(new ColorSetting.Builder()
        .name("ambient")
        .description("The ambient's color.")
        .defaultValue(new SettingColor(25, 25, 25))
        .visible(() -> !distance.get())
        .build()
    );

    private final Setting<SettingColor> miscColor = sgColors.add(new ColorSetting.Builder()
        .name("misc")
        .description("The misc color.")
        .defaultValue(new SettingColor(175, 175, 175))
        .visible(() -> !distance.get())
        .build()
    );

    public Tracers() {
        super(Categories.Render, Items.WHITE_STAINED_GLASS, "tracers", "Displays tracer lines to specified entities.");
    }

    @EventHandler
    private void onRender3D(Render3DEvent event) {
        if (hideHud.get() && mc.options.hudHidden) return;

        count = 0;

        for (Entity entity : mc.world.getEntities()) {
            if (mc.player.distanceTo(entity) > maxDist.get() || (!Modules.get().isActive(Freecam.class) && entity == mc.player) || !entities.get().getBoolean(entity.getType()) || (!showInvisible.get() && entity.isInvisible()) | !EntityUtils.isInRenderDistance(entity)) continue;
            if (ignoreFriends.get() && entity instanceof PlayerEntity player) {
                if (Friends.get().isFriend(player)) return;
            }

            Color color;
            if (distance.get()) color = getColorFromDistance(entity);
            else if (entity instanceof PlayerEntity) {
                if (entity.equals(mc.getCameraEntity())) color = PlayerUtils.getPlayerColor(((PlayerEntity) entity), selfColor.get());
                else color = PlayerUtils.getPlayerColor(((PlayerEntity) entity), playersColor.get());
            } else {
                color = switch (entity.getType().getSpawnGroup()) {
                    case CREATURE -> animalsColor.get();
                    case WATER_AMBIENT, WATER_CREATURE, UNDERGROUND_WATER_CREATURE -> waterAnimalsColor.get();
                    case MONSTER -> monstersColor.get();
                    case AMBIENT -> ambientColor.get();
                    default -> miscColor.get();
                };
            }

            double x = entity.prevX + (entity.getX() - entity.prevX) * event.tickDelta;
            double y = entity.prevY + (entity.getY() - entity.prevY) * event.tickDelta;
            double z = entity.prevZ + (entity.getZ() - entity.prevZ) * event.tickDelta;

            double height = entity.getBoundingBox().maxY - entity.getBoundingBox().minY;
            if (target.get() == Target.Head) y += height;
            else if (target.get() == Target.Body) y += height / 2;

            event.renderer.line(RenderUtils.center.x, RenderUtils.center.y, RenderUtils.center.z, x, y, z, color);
            if (stem.get()) event.renderer.line(x, entity.getY(), z, x, entity.getY() + height, z, color);

            count++;
        }
    }

    private Color getColorFromDistance(Entity entity) {
        // Credit to Icy from Stackoverflow
        double distance = mc.gameRenderer.getCamera().getPos().distanceTo(entity.getPos());
        double percent = distance / 60;

        if (percent < 0 || percent > 1) {
            distanceColor.set(0, 255, 0, 255);
            return distanceColor;
        }

        int r, g;

        if (percent < 0.5) {
            r = 255;
            g = (int) (255 * percent / 0.5);  // Closer to 0.5, closer to yellow (255,255,0)
        } else {
            g = 255;
            r = 255 - (int) (255 * (percent - 0.5) / 0.5); // Closer to 1.0, closer to green (0,255,0)
        }

        distanceColor.set(r, g, 0, 255);
        return distanceColor;
    }

    @Override
    public String getInfoString() {
        return Integer.toString(count);
    }
}
