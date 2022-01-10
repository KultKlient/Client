package kult.klient.systems.modules.render;

import kult.klient.eventbus.EventHandler;
import kult.klient.events.world.TickEvent;
import kult.klient.settings.BoolSetting;
import kult.klient.settings.ParticleTypeListSetting;
import kult.klient.settings.Setting;
import kult.klient.settings.SettingGroup;
import kult.klient.systems.modules.Categories;
import kult.klient.systems.modules.Module;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.ParticleTypes;

import java.util.List;

public class Trail extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    // General

    private final Setting<List<ParticleType<?>>> particles = sgGeneral.add(new ParticleTypeListSetting.Builder()
        .name("particles")
        .description("Particles to draw.")
        .defaultValue(ParticleTypes.DRIPPING_OBSIDIAN_TEAR, ParticleTypes.CAMPFIRE_COSY_SMOKE)
        .build()
    );

    private final Setting<Boolean> pause = sgGeneral.add(new BoolSetting.Builder()
        .name("pause-when-stationary")
        .description("Whether or not to add particles when you are not moving.")
        .defaultValue(true)
        .build()
    );

    public Trail() {
        super(Categories.Render, Items.BLUE_STAINED_GLASS, "trail", "Renders a customizable trail behind your player.");
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (pause.get() && mc.player.getVelocity().x == 0 && mc.player.getVelocity().y == 0 && mc.player.getVelocity().z == 0) return;
        for (ParticleType<?> particleType : particles.get()) {
            mc.world.addParticle((ParticleEffect) particleType, mc.player.getX(), mc.player.getY(), mc.player.getZ(), 0, 0, 0);
        }
    }
}
