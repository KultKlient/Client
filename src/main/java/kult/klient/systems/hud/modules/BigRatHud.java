package kult.klient.systems.hud.modules;

import kult.klient.renderer.GL;
import kult.klient.renderer.Renderer2D;
import kult.klient.settings.DoubleSetting;
import kult.klient.settings.Setting;
import kult.klient.settings.SettingGroup;
import kult.klient.utils.render.color.Color;
import kult.klient.systems.hud.HUD;
import kult.klient.systems.hud.HudElement;
import kult.klient.systems.hud.HudRenderer;
import net.minecraft.util.Identifier;

public class BigRatHud extends HudElement {
    private static final Identifier BIG_RAT = new Identifier("kultklient", "textures/big-rat.png");
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
