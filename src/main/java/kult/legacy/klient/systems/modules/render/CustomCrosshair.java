package kult.legacy.klient.systems.modules.render;

import kult.legacy.klient.eventbus.EventHandler;
import kult.legacy.klient.events.render.Render2DEvent;
import kult.legacy.klient.settings.SettingGroup;
import kult.legacy.klient.systems.modules.Categories;
import kult.legacy.klient.systems.modules.Module;
import net.minecraft.item.Items;

public class CustomCrosshair extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    // General

    public CustomCrosshair() {
        super(Categories.Render, Items.COMPASS, "custom-crosshair", "Renders a customizable crosshair instead of the Minecraft one.");
    }

    @EventHandler
    public void onRender2D(Render2DEvent event) {
        //TODO: Make.
    }
}
