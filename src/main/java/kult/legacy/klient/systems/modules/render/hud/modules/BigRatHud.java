package kult.legacy.klient.systems.modules.render.hud.modules;

import kult.legacy.klient.renderer.GL;
import kult.legacy.klient.renderer.Renderer2D;
import kult.legacy.klient.settings.*;
import kult.legacy.klient.systems.modules.render.hud.HUD;
import kult.legacy.klient.systems.modules.render.hud.HudElement;
import kult.legacy.klient.systems.modules.render.hud.HudRenderer;
import kult.legacy.klient.utils.render.color.Color;
import net.minecraft.util.Identifier;

public class BigRatHud extends HudElement {
    private static final Identifier BIG_RAT = new Identifier("kultklientlegacy", "textures/big-rat.png");
    private final Color TEXTURE_COLOR = new Color(255, 255, 255, 255);

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    // General

    private final Setting<Double> scale = sgGeneral.add(new DoubleSetting.Builder()
        .name("scale")
        .description("The scale of big rat.")
        .defaultValue(0.25)
        .min(0.1)
        .sliderRange(0.1, 2)
        .build()
    );

    public BigRatHud(HUD hud) {
        super(hud, "big-rat", "Displays a BIG RAT.", false);
    }

    @Override
    public void update(HudRenderer renderer) {
        box.setSize(900 * scale.get(), 600 * scale.get());
    }

    @Override
    public void render(HudRenderer renderer) {
        GL.bindTexture(BIG_RAT);
        Renderer2D.TEXTURE.begin();
        Renderer2D.TEXTURE.texQuad(box.getX(), box.getY(), box.width, box.height, TEXTURE_COLOR);
        Renderer2D.TEXTURE.render(null);
    }
}
