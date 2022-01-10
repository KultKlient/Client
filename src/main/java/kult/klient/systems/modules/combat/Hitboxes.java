package kult.klient.systems.modules.combat;

import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import kult.klient.settings.DoubleSetting;
import kult.klient.settings.EntityTypeListSetting;
import kult.klient.settings.Setting;
import kult.klient.settings.SettingGroup;
import kult.klient.systems.modules.Categories;
import kult.klient.systems.modules.Module;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Items;

public class Hitboxes extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    // General

    private final Setting<Object2BooleanMap<EntityType<?>>> entities = sgGeneral.add(new EntityTypeListSetting.Builder()
        .name("entities")
        .description("Which entities to target.")
        .defaultValue(EntityType.PLAYER)
        .build()
    );

    private final Setting<Double> value = sgGeneral.add(new DoubleSetting.Builder()
        .name("expand")
        .description("How much to expand the hitbox of the entity.")
        .defaultValue(0.5)
        .sliderRange(0, 1)
        .build()
    );

    public Hitboxes() {
        super(Categories.Combat, Items.GLASS, "hitboxes", "Expands an entity's hitboxes.");
    }

    public double getEntityValue(Entity entity) {
        if (!isActive()) return 0;
        if (entities.get().getBoolean(entity.getType())) return value.get();
        return 0;
    }
}
