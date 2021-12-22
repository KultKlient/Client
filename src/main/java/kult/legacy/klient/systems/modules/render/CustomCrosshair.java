package kultklient.legacy.client.systems.modules.render;

import kultklient.legacy.client.eventbus.EventHandler;
import kultklient.legacy.client.events.render.Render2DEvent;
import kultklient.legacy.client.settings.SettingGroup;
import kultklient.legacy.client.systems.modules.Categories;
import kultklient.legacy.client.systems.modules.Module;
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
