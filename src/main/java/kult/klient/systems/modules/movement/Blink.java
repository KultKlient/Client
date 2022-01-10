package kult.klient.systems.modules.movement;

import kult.klient.KultKlient;
import kult.klient.eventbus.EventHandler;
import kult.klient.events.packets.PacketEvent;
import kult.klient.events.render.Render3DEvent;
import kult.klient.events.world.TickEvent;
import kult.klient.renderer.ShapeMode;
import kult.klient.settings.*;
import kult.klient.systems.modules.Categories;
import kult.klient.systems.modules.Module;
import kult.klient.utils.Utils;
import kult.klient.utils.entity.fakeplayer.FakePlayerEntity;
import kult.klient.utils.misc.Pool;
import kult.klient.utils.render.WireframeEntityRenderer;
import kult.klient.utils.render.color.SettingColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.world.dimension.DimensionType;

import java.util.*;

public class Blink extends Module {
    private final List<PlayerMoveC2SPacket> packets = new ArrayList<>();
    private final List<BlinkPlayer> blinks = new ArrayList<>();

    private final Pool<Section> sectionPool = new Pool<>(Section::new);
    private final Queue<Section> sections = new ArrayDeque<>();

    private DimensionType lastDimension;

    private Section section;

    private int timer = 0;

    private final SettingGroup sgGhost = settings.createGroup("Ghost");
    private final SettingGroup sgBreadcrumbs = settings.createGroup("Breadcrumbs");

    // Ghost

    private final Setting<Boolean> ghost = sgGhost.add(new BoolSetting.Builder()
        .name("ghost")
        .description("Renders a ghost in the place you really are in.")
        .defaultValue(true)
        .build()
    );

    private final Setting<ShapeMode> shapeMode = sgGhost.add(new EnumSetting.Builder<ShapeMode>()
        .name("shape-mode")
        .description("Determines how the shapes are rendered.")
        .defaultValue(ShapeMode.Both)
        .build()
    );

    private final Setting<SettingColor> sideColor = sgGhost.add(new ColorSetting.Builder()
        .name("side-color")
        .description("The side color.")
        .defaultValue(new SettingColor(KultKlient.INSTANCE.KULTKLIENT_COLOR.r, KultKlient.INSTANCE.KULTKLIENT_COLOR.g, KultKlient.INSTANCE.KULTKLIENT_COLOR.b, 75))
        .build()
    );

    private final Setting<SettingColor> lineColor = sgGhost.add(new ColorSetting.Builder()
        .name("line-color")
        .description("The line color.")
        .defaultValue(new SettingColor(KultKlient.INSTANCE.KULTKLIENT_COLOR.r, KultKlient.INSTANCE.KULTKLIENT_COLOR.g, KultKlient.INSTANCE.KULTKLIENT_COLOR.b))
        .build()
    );

    // Breadcrumbs

    private final Setting<Boolean> breadcrumbs = sgBreadcrumbs.add(new BoolSetting.Builder()
        .name("breadcrumbs")
        .description("Displays a trail behind where you have walked while using blink.")
        .defaultValue(true)
        .build()
    );

    private final Setting<SettingColor> color = sgBreadcrumbs.add(new ColorSetting.Builder()
        .name("color")
        .description("The color of the breadcrumbs trail.")
        .defaultValue(new SettingColor(KultKlient.INSTANCE.KULTKLIENT_COLOR.r, KultKlient.INSTANCE.KULTKLIENT_COLOR.g, KultKlient.INSTANCE.KULTKLIENT_COLOR.b))
        .build()
    );

    private final Setting<Integer> maxSections = sgBreadcrumbs.add(new IntSetting.Builder()
        .name("max-sections")
        .description("The maximum number of sections.")
        .defaultValue(1000)
        .min(1)
        .sliderRange(1, 5000)
        .build()
    );

    private final Setting<Double> sectionLength = sgBreadcrumbs.add(new DoubleSetting.Builder()
        .name("section-length")
        .description("The section length in blocks.")
        .defaultValue(0.5)
        .min(0)
        .sliderRange(0, 1)
        .build()
    );

    public Blink() {
        super(Categories.Movement, Items.TINTED_GLASS, "blink", "Allows you to essentially teleport while suspending motion updates.");
    }

    @Override
    public void onActivate() {
        synchronized (blinks) {
            blinks.add(new BlinkPlayer(mc.player));
        }

        section = sectionPool.get();
        section.set1();

        lastDimension = mc.world.getDimension();
    }

    @Override
    public void onDeactivate() {
        synchronized (packets) {
            packets.forEach(p -> mc.player.networkHandler.sendPacket(p));
            packets.clear();
            timer = 0;
        }

        synchronized (blinks) {
            blinks.clear();
        }

        for (Section section : sections) sectionPool.free(section);
        sections.clear();
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        timer++;

        if (lastDimension != mc.world.getDimension()) {
            for (Section sec : sections) sectionPool.free(sec);
            sections.clear();
        }

        if (isFarEnough(section.x1, section.y1, section.z1)) {
            section.set2();

            if (sections.size() >= maxSections.get()) {
                Section section = sections.poll();
                if (section != null) sectionPool.free(section);
            }

            sections.add(section);
            section = sectionPool.get();
            section.set1();
        }

        lastDimension = mc.world.getDimension();
    }

    @EventHandler
    private void onSendPacket(PacketEvent.Send event) {
        if (!(event.packet instanceof PlayerMoveC2SPacket)) return;
        event.cancel();

        synchronized (packets) {
            PlayerMoveC2SPacket p = (PlayerMoveC2SPacket) event.packet;
            PlayerMoveC2SPacket prev = packets.size() == 0 ? null : packets.get(packets.size() - 1);

            if (prev != null && p.isOnGround() == prev.isOnGround() && p.getYaw(-1) == prev.getYaw(-1) && p.getPitch(-1) == prev.getPitch(-1) && p.getX(-1) == prev.getX(-1) && p.getY(-1) == prev.getY(-1) && p.getZ(-1) == prev.getZ(-1)) return;

            packets.add(p);
        }
    }

    @Override
    public String getInfoString() {
        return String.format("%.1f", timer / 20f);
    }

    @EventHandler
    private void onRender3D(Render3DEvent event) {
        if (ghost.get()) {
            synchronized (blinks) {
                blinks.forEach(blinkPlayer -> blinkPlayer.render(event));
            }
        }

        if (breadcrumbs.get()) {
            int iLast = -1;

            for (Section section : sections) {
                if (iLast == -1) iLast = event.renderer.lines.vec3(section.x1, section.y1, section.z1).color(color.get()).next();

                int i = event.renderer.lines.vec3(section.x2, section.y2, section.z2).color(color.get()).next();
                event.renderer.lines.line(iLast, i);
                iLast = i;
            }
        }
    }

    private boolean isFarEnough(double x, double y, double z) {
        return Math.abs(mc.player.getX() - x) >= sectionLength.get() || Math.abs(mc.player.getY() - y) >= sectionLength.get() || Math.abs(mc.player.getZ() - z) >= sectionLength.get();
    }

    private class Section {
        public float x1, y1, z1;
        public float x2, y2, z2;

        public void set1() {
            x1 = (float) mc.player.getX();
            y1 = (float) mc.player.getY();
            z1 = (float) mc.player.getZ();
        }

        public void set2() {
            x2 = (float) mc.player.getX();
            y2 = (float) mc.player.getY();
            z2 = (float) mc.player.getZ();
        }

        public void render(Render3DEvent event) {
            event.renderer.line(x1, y1, z1, x2, y2, z2, color.get());
        }
    }

    private class BlinkPlayer extends FakePlayerEntity {
        public BlinkPlayer(PlayerEntity player) {
            super(player, "blink", Utils.getPlayerHealth(), true);
        }

        public void render(Render3DEvent event) {
            WireframeEntityRenderer.render(event, this, 1, sideColor.get(), lineColor.get(), shapeMode.get());
        }
    }
}
