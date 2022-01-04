package kult.legacy.klient.gui.tabs.builtin;

import kult.legacy.klient.gui.GuiTheme;
import kult.legacy.klient.gui.tabs.Tab;
import kult.legacy.klient.gui.tabs.TabScreen;
import kult.legacy.klient.gui.tabs.WindowTabScreen;
import kult.legacy.klient.gui.widgets.containers.WHorizontalList;
import kult.legacy.klient.gui.widgets.containers.WSection;
import kult.legacy.klient.gui.widgets.containers.WTable;
import kult.legacy.klient.gui.widgets.input.WTextBox;
import kult.legacy.klient.gui.widgets.pressable.WMinus;
import kult.legacy.klient.gui.widgets.pressable.WPlus;
import kult.legacy.klient.settings.ColorSetting;
import kult.legacy.klient.settings.SettingGroup;
import kult.legacy.klient.settings.Settings;
import kult.legacy.klient.systems.enemies.Enemy;
import kult.legacy.klient.systems.enemies.Enemies;
import kult.legacy.klient.utils.render.color.SettingColor;
import net.minecraft.client.gui.screen.Screen;

public class EnemiesTab extends Tab {
    public EnemiesTab() {
        super("Enemies");
    }

    @Override
    public TabScreen createScreen(GuiTheme theme) {
        return new EnemiesScreen(theme, this);
    }

    @Override
    public boolean isScreen(Screen screen) {
        return screen instanceof EnemiesScreen;
    }

    public static class EnemiesScreen extends WindowTabScreen {
        private final Settings settings = new Settings();

        public EnemiesScreen(GuiTheme theme, Tab tab) {
            super(theme, tab);

            SettingGroup sgGeneral = settings.getDefaultGroup();

            sgGeneral.add(new ColorSetting.Builder()
                .name("color")
                .description("The color used to show enemies.")
                .defaultValue(new SettingColor(0, 255, 180))
                .onChanged(Enemies.get().color::set)
                .onModuleActivated(colorSetting -> colorSetting.set(Enemies.get().color))
                .build()
            );

            settings.onActivated();
        }

        @Override
        public void initWidgets() {
            // Settings
            add(theme.settings(settings)).expandX();

            // Enemies
            WSection enemies = add(theme.section("Enemies")).expandX().widget();
            WTable table = enemies.add(theme.table()).expandX().widget();

            initTable(table);

            // New
            WHorizontalList list = enemies.add(theme.horizontalList()).expandX().widget();

            WTextBox nameW = list.add(theme.textBox("")).minWidth(400).expandX().widget();
            nameW.setFocused(true);

            WPlus add = list.add(theme.plus()).widget();
            add.action = () -> {
                String name = nameW.get().trim();

                if (Enemies.get().add(new Enemy(name))) {
                    nameW.set("");

                    table.clear();
                    initTable(table);
                }
            };

            enterAction = add.action;
        }

        private void initTable(WTable table) {
            for (Enemy enemy : Enemies.get()) {
                table.add(theme.label(enemy.name));

                WMinus remove = table.add(theme.minus()).expandCellX().right().widget();
                remove.action = () -> {
                    Enemies.get().remove(enemy);

                    table.clear();
                    initTable(table);
                };

                table.row();
            }
        }
    }
}
