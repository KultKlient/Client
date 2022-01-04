package kult.legacy.klient.gui.themes.kultklient;

import kult.legacy.klient.KultKlientLegacy;
import kult.legacy.klient.gui.DefaultSettingsWidgetFactory;
import kult.legacy.klient.gui.GuiTheme;
import kult.legacy.klient.gui.WidgetScreen;
import kult.legacy.klient.gui.renderer.packer.GuiTexture;
import kult.legacy.klient.gui.themes.kultklient.widgets.input.WKultKlientDropdown;
import kult.legacy.klient.gui.themes.kultklient.widgets.input.WKultKlientSlider;
import kult.legacy.klient.gui.themes.kultklient.widgets.input.WKultKlientTextBox;
import kult.legacy.klient.gui.utils.AlignmentX;
import kult.legacy.klient.gui.utils.CharFilter;
import kult.legacy.klient.gui.widgets.containers.WSection;
import kult.legacy.klient.gui.widgets.containers.WView;
import kult.legacy.klient.gui.widgets.containers.WWindow;
import kult.legacy.klient.gui.widgets.input.WDropdown;
import kult.legacy.klient.gui.widgets.input.WSlider;
import kult.legacy.klient.gui.widgets.input.WTextBox;
import kult.legacy.klient.renderer.text.TextRenderer;
import kult.legacy.klient.systems.accounts.Account;
import kult.legacy.klient.systems.modules.Module;
import kult.legacy.klient.utils.render.color.Color;
import kult.legacy.klient.utils.render.color.SettingColor;
import kult.legacy.klient.gui.themes.kultklient.widgets.*;
import kult.legacy.klient.gui.themes.kultklient.widgets.pressable.*;
import kult.legacy.klient.gui.widgets.*;
import kult.legacy.klient.gui.widgets.pressable.*;
import kult.legacy.klient.settings.*;

import static kult.legacy.klient.KultKlientLegacy.mc;

public class KultKlientGuiTheme extends GuiTheme {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final SettingGroup sgColors = settings.createGroup("Colors");
    private final SettingGroup sgTextColors = settings.createGroup("Text");
    private final SettingGroup sgBackgroundColors = settings.createGroup("Background");
    private final SettingGroup sgOutline = settings.createGroup("Outline");
    private final SettingGroup sgSeparator = settings.createGroup("Separator");
    private final SettingGroup sgScrollbar = settings.createGroup("Scrollbar");
    private final SettingGroup sgSlider = settings.createGroup("Slider");

    // General

    public final Setting<Double> scale = sgGeneral.add(new DoubleSetting.Builder()
        .name("scale")
        .description("Scale of the GUI.")
        .defaultValue(1)
        .min(0.75)
        .sliderRange(0.75, 4)
        .onSliderRelease()
        .onChanged(aDouble -> {
            if (mc.currentScreen instanceof WidgetScreen) ((WidgetScreen) mc.currentScreen).invalidate();
        })
        .build()
    );

    public final Setting<AlignmentX> moduleAlignment = sgGeneral.add(new EnumSetting.Builder<AlignmentX>()
        .name("module-alignment")
        .description("How module titles are aligned.")
        .defaultValue(AlignmentX.Center)
        .build()
    );

    public final Setting<Boolean> categoryIcons = sgGeneral.add(new BoolSetting.Builder()
        .name("category-icons")
        .description("Adds item icons to module categories.")
        .defaultValue(true)
        .build()
    );

    public final Setting<Boolean> hideHUD = sgGeneral.add(new BoolSetting.Builder()
        .name("hide-hud")
        .description("Hide HUD when in GUI.")
        .defaultValue(false)
        .onChanged(v -> {
            if (mc.currentScreen instanceof WidgetScreen) mc.options.hudHidden = v;
        })
        .build()
    );

    public final Setting<Integer> round = sgGeneral.add(new IntSetting.Builder()
        .name("round")
        .description("How much windows should be rounded. (0 to disable)")
        .defaultValue(10)
        .range(0, 20)
        .sliderRange(0, 20)
        .build()
    );

    // Colors

    public final Setting<SettingColor> mainColor = color("main", "Main color of the GUI.", new SettingColor(KultKlientLegacy.INSTANCE.MATHAX_COLOR.r, KultKlientLegacy.INSTANCE.MATHAX_COLOR.g, KultKlientLegacy.INSTANCE.MATHAX_COLOR.b));
    public final Setting<SettingColor> checkboxColor = color("checkbox", "Color of checkbox.", new SettingColor(KultKlientLegacy.INSTANCE.MATHAX_COLOR.r, KultKlientLegacy.INSTANCE.MATHAX_COLOR.g, KultKlientLegacy.INSTANCE.MATHAX_COLOR.b));
    public final Setting<SettingColor> plusColor = color("plus", "Color of plus button.", new SettingColor(255, 255, 255));
    public final Setting<SettingColor> minusColor = color("minus", "Color of minus button.", new SettingColor(255, 255, 255));

    // Text

    public final Setting<SettingColor> textColor = color(sgTextColors, "text", "Color of text.", new SettingColor(255, 255, 255));
    public final Setting<SettingColor> textSecondaryColor = color(sgTextColors, "text-secondary-text", "Color of secondary text.", new SettingColor(150, 150, 150));
    public final Setting<SettingColor> textHighlightColor = color(sgTextColors, "text-highlight", "Color of text highlighting.", new SettingColor(KultKlientLegacy.INSTANCE.MATHAX_COLOR.r, KultKlientLegacy.INSTANCE.MATHAX_COLOR.g, KultKlientLegacy.INSTANCE.MATHAX_COLOR.b));
    public final Setting<SettingColor> titleTextColor = color(sgTextColors, "title-text", "Color of title text.", new SettingColor(255, 255, 255));
    public final Setting<SettingColor> loggedInColor = color(sgTextColors, "logged-in-text", "Color of logged in account name in Account Manager.", new SettingColor(45, 225, 45));

    // Background

    public final ThreeStateColorSetting backgroundColor = new ThreeStateColorSetting(
            sgBackgroundColors,
            "background",
            new SettingColor(KultKlientLegacy.INSTANCE.MATHAX_BACKGROUND_COLOR.r, KultKlientLegacy.INSTANCE.MATHAX_BACKGROUND_COLOR.g, KultKlientLegacy.INSTANCE.MATHAX_BACKGROUND_COLOR.b, 100),
            new SettingColor(KultKlientLegacy.INSTANCE.MATHAX_BACKGROUND_COLOR.r, KultKlientLegacy.INSTANCE.MATHAX_BACKGROUND_COLOR.g, KultKlientLegacy.INSTANCE.MATHAX_BACKGROUND_COLOR.b, 135),
            new SettingColor(KultKlientLegacy.INSTANCE.MATHAX_BACKGROUND_COLOR.r, KultKlientLegacy.INSTANCE.MATHAX_BACKGROUND_COLOR.g, KultKlientLegacy.INSTANCE.MATHAX_BACKGROUND_COLOR.b, 175)
    );

    public final Setting<SettingColor> moduleBackground = color(sgBackgroundColors, "module-background", "Color of module background when active.", new SettingColor(KultKlientLegacy.INSTANCE.MATHAX_BACKGROUND_COLOR.r, KultKlientLegacy.INSTANCE.MATHAX_BACKGROUND_COLOR.g, KultKlientLegacy.INSTANCE.MATHAX_BACKGROUND_COLOR.b, 200));

    // Outline

    public final ThreeStateColorSetting outlineColor = new ThreeStateColorSetting(
            sgOutline,
            "outline",
            new SettingColor(125, 125, 125),
            new SettingColor(150, 150, 150),
            new SettingColor(175, 175, 175)
    );

    // Separator

    public final Setting<SettingColor> separatorText = color(sgSeparator, "separator-text", "Color of separator text", new SettingColor(255, 255, 255));
    public final Setting<SettingColor> separatorCenter = color(sgSeparator, "separator-center", "Center color of separators.", new SettingColor(255, 255, 255));
    public final Setting<SettingColor> separatorEdges = color(sgSeparator, "separator-edges", "Color of separator edges.", new SettingColor(225, 225, 225, 150));

    // Scrollbar

    public final ThreeStateColorSetting scrollbarColor = new ThreeStateColorSetting(
        sgScrollbar,
        "Scrollbar",
        new SettingColor(KultKlientLegacy.INSTANCE.MATHAX_BACKGROUND_COLOR.r, KultKlientLegacy.INSTANCE.MATHAX_BACKGROUND_COLOR.g, KultKlientLegacy.INSTANCE.MATHAX_BACKGROUND_COLOR.b, 100),
        new SettingColor(KultKlientLegacy.INSTANCE.MATHAX_BACKGROUND_COLOR.r, KultKlientLegacy.INSTANCE.MATHAX_BACKGROUND_COLOR.g, KultKlientLegacy.INSTANCE.MATHAX_BACKGROUND_COLOR.b, 150),
        new SettingColor(KultKlientLegacy.INSTANCE.MATHAX_BACKGROUND_COLOR.r, KultKlientLegacy.INSTANCE.MATHAX_BACKGROUND_COLOR.g, KultKlientLegacy.INSTANCE.MATHAX_BACKGROUND_COLOR.b, 150)
    );

    // Slider

    public final ThreeStateColorSetting sliderHandle = new ThreeStateColorSetting(
        sgSlider,
        "slider-handle",
        new SettingColor(KultKlientLegacy.INSTANCE.MATHAX_COLOR.r, KultKlientLegacy.INSTANCE.MATHAX_COLOR.g, KultKlientLegacy.INSTANCE.MATHAX_COLOR.b),
        new SettingColor(255, 100, 125),
        new SettingColor(255, 100, 125)
    );

    public final Setting<SettingColor> sliderLeft = color(sgSlider, "slider-left", "Color of slider left part.", new SettingColor(KultKlientLegacy.INSTANCE.MATHAX_COLOR.r, KultKlientLegacy.INSTANCE.MATHAX_COLOR.g, KultKlientLegacy.INSTANCE.MATHAX_COLOR.b));
    public final Setting<SettingColor> sliderRight = color(sgSlider, "slider-right", "Color of slider right part.", new SettingColor(KultKlientLegacy.INSTANCE.MATHAX_BACKGROUND_COLOR.r, KultKlientLegacy.INSTANCE.MATHAX_BACKGROUND_COLOR.g, KultKlientLegacy.INSTANCE.MATHAX_BACKGROUND_COLOR.b));

    public KultKlientGuiTheme() {
        super("KultKlient");

        settingsFactory = new DefaultSettingsWidgetFactory(this);
    }

    private Setting<SettingColor> color(SettingGroup group, String name, String description, SettingColor color) {
        return group.add(new ColorSetting.Builder()
                .name(name + "-color")
                .description(description)
                .defaultValue(color)
                .build());
    }

    private Setting<SettingColor> color(String name, String description, SettingColor color) {
        return color(sgColors, name, description, color);
    }

    // Widgets

    @Override
    public WWindow window(String title) {
        return w(new WKultKlientWindow(title));
    }

    @Override
    public WLabel label(String text, boolean title, double maxWidth) {
        if (maxWidth == 0) return w(new WKultKlientLabel(text, title));
        return w(new WKultKlientMultiLabel(text, title, maxWidth));
    }

    @Override
    public WHorizontalSeparator horizontalSeparator(String text) {
        return w(new WKultKlientHorizontalSeparator(text));
    }

    @Override
    public WVerticalSeparator verticalSeparator() {
        return w(new WKultKlientVerticalSeparator());
    }

    @Override
    protected WButton button(String text, GuiTexture texture) {
        return w(new WKultKlientButton(text, texture));
    }

    @Override
    public WMinus minus() {
        return w(new WKultKlientMinus());
    }

    @Override
    public WPlus plus() {
        return w(new WKultKlientPlus());
    }

    @Override
    public WCheckbox checkbox(boolean checked) {
        return w(new WKultKlientCheckbox(checked));
    }

    @Override
    public WSlider slider(double value, double min, double max) {
        return w(new WKultKlientSlider(value, min, max));
    }

    @Override
    public WTextBox textBox(String text, CharFilter filter) {
        return w(new WKultKlientTextBox(text, filter));
    }

    @Override
    public <T> WDropdown<T> dropdown(T[] values, T value) {
        return w(new WKultKlientDropdown<>(values, value));
    }

    @Override
    public WTriangle triangle() {
        return w(new WKultKlientTriangle());
    }

    @Override
    public WTooltip tooltip(String text) {
        return w(new WKultKlientTooltip(text));
    }

    @Override
    public WView view() {
        return w(new WKultKlientView());
    }

    @Override
    public WSection section(String title, boolean expanded, WWidget headerWidget) {
        return w(new WKultKlientSection(title, expanded, headerWidget));
    }

    @Override
    public WAccount account(WidgetScreen screen, Account<?> account) {
        return w(new WKultKlientAccount(screen, account));
    }

    @Override
    public WWidget module(Module module) {
        return w(new WKultKlientModule(module));
    }

    @Override
    public WQuad quad(Color color) {
        return w(new WKultKlientQuad(color));
    }

    @Override
    public WTopBar topBar() {
        return w(new WKultKlientTopBar());
    }

    // Colors

    @Override
    public Color textColor() {
        return textColor.get();
    }

    @Override
    public Color textSecondaryColor() {
        return textSecondaryColor.get();
    }

    // Other

    @Override
    public TextRenderer textRenderer() {
        return TextRenderer.get();
    }

    @Override
    public double scale(double value) {
        return value * scale.get();
    }

    @Override
    public void resetScale() {
        scale.set(scale.getDefaultValue());
    }

    @Override
    public boolean categoryIcons() {
        return categoryIcons.get();
    }

    @Override
    public boolean hideHUD() {
        return hideHUD.get();
    }

    @Override
    public int roundAmount() {
        return round.get();
    }

    public class ThreeStateColorSetting {
        private final Setting<SettingColor> normal, hovered, pressed;

        public ThreeStateColorSetting(SettingGroup group, String name, SettingColor c1, SettingColor c2, SettingColor c3) {
            normal = color(group, name, "Color of " + name + ".", c1);
            hovered = color(group, "hovered-" + name, "Color of " + name + " when hovered.", c2);
            pressed = color(group, "pressed-" + name, "Color of " + name + " when pressed.", c3);
        }

        public SettingColor get() {
            return normal.get();
        }

        public SettingColor get(boolean pressed, boolean hovered, boolean bypassDisableHoverColor) {
            if (pressed) return this.pressed.get();
            return (hovered && (bypassDisableHoverColor || !disableHoverColor)) ? this.hovered.get() : this.normal.get();
        }

        public SettingColor get(boolean pressed, boolean hovered) {
            return get(pressed, hovered, false);
        }
    }
}
