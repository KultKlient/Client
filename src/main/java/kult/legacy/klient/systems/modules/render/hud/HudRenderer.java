package kult.legacy.klient.systems.modules.render.hud;

import kult.legacy.klient.gui.GuiThemes;
import kult.legacy.klient.renderer.text.TextRenderer;
import kult.legacy.klient.utils.render.color.Color;

import java.util.ArrayList;
import java.util.List;

public class HudRenderer {
    public double delta;
    private final List<Runnable> postTasks = new ArrayList<>();

    public void begin(double scale, double frameDelta, boolean scaleOnly) {
        TextRenderer.get().begin(scale, scaleOnly, false);

        this.delta = frameDelta;
    }

    public void end() {
        TextRenderer.get().end();

        for (Runnable runnable : postTasks) {
            runnable.run();
        }

        postTasks.clear();
    }

    public void text(String text, double x, double y, Color color) {
        TextRenderer.get().render(text, x, y, color, true);
    }

    public double textWidth(String text) {
        return TextRenderer.get().getWidth(text);
    }

    public double textHeight() {
        return TextRenderer.get().getHeight();
    }

    public void addPostTask(Runnable runnable) {
        postTasks.add(runnable);
    }

    public int roundAmount() {
        return GuiThemes.get().roundAmount();
    }
}
