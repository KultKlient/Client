package kult.klient.gui.tabs.builtin;

import kult.klient.gui.widgets.containers.WHorizontalList;
import kult.klient.gui.widgets.containers.WSection;
import kult.klient.gui.widgets.containers.WTable;
import kult.klient.gui.widgets.input.WTextBox;
import kult.klient.gui.widgets.pressable.WMinus;
import kult.klient.gui.widgets.pressable.WPlus;
import kult.klient.utils.render.color.SettingColor;
import kult.klient.gui.GuiTheme;
import kult.klient.gui.tabs.Tab;
import kult.klient.gui.tabs.TabScreen;
import kult.klient.gui.tabs.WindowTabScreen;
import kult.klient.settings.BoolSetting;
import kult.klient.settings.ColorSetting;
import kult.klient.settings.SettingGroup;
import kult.klient.settings.Settings;
import kult.klient.systems.friends.Friend;
import kult.klient.systems.friends.Friends;
import kult.klient.utils.misc.NbtUtils;
import net.minecraft.client.gui.screen.Screen;

public class FriendsTab extends Tab {

    public FriendsTab() {
        super("Friends");
    }

    @Override
    public TabScreen createScreen(GuiTheme theme) {
        return new FriendsScreen(theme, this);
    }

    @Override
    public boolean isScreen(Screen screen) {
        return screen instanceof FriendsScreen;
    }

    public static class FriendsScreen extends WindowTabScreen {
        private final Settings settings = new Settings();

        public FriendsScreen(GuiTheme theme, Tab tab) {
            super(theme, tab);

            SettingGroup sgGeneral = settings.getDefaultGroup();

            sgGeneral.add(new ColorSetting.Builder()
                .name("color")
                .description("The color used to show friends.")
                .defaultValue(new SettingColor(0, 255, 180))
                .onChanged(Friends.get().color::set)
                .onModuleActivated(colorSetting -> colorSetting.set(Friends.get().color))
                .build()
            );

            sgGeneral.add(new BoolSetting.Builder()
                .name("attack")
                .description("Whether to attack friends.")
                .defaultValue(false)
                .onChanged(aBoolean -> Friends.get().attack = aBoolean)
                .onModuleActivated(booleanSetting -> booleanSetting.set(Friends.get().attack))
                .build()
            );

            settings.onActivated();
        }

        @Override
        public void initWidgets() {
            // Settings
            add(theme.settings(settings)).expandX();

            // Friends
            WSection friends = add(theme.section("Friends")).expandX().widget();
            WTable table = friends.add(theme.table()).expandX().widget();

            initTable(table);

            // New
            WHorizontalList list = friends.add(theme.horizontalList()).expandX().widget();

            WTextBox nameW = list.add(theme.textBox("")).minWidth(400).expandX().widget();
            nameW.setFocused(true);

            WPlus add = list.add(theme.plus()).widget();
            add.action = () -> {
                String name = nameW.get().trim();

                if (Friends.get().add(new Friend(name))) {
                    nameW.set("");

                    table.clear();
                    initTable(table);
                }
            };

            enterAction = add.action;
        }

        private void initTable(WTable table) {
            for (Friend friend : Friends.get()) {
                table.add(theme.label(friend.name));

                WMinus remove = table.add(theme.minus()).expandCellX().right().widget();
                remove.action = () -> {
                    Friends.get().remove(friend);

                    table.clear();
                    initTable(table);
                };

                table.row();
            }
        }

        @Override
        public boolean toClipboard() {
            return NbtUtils.toClipboard(Friends.get());
        }

        @Override
        public boolean fromClipboard() {
            return NbtUtils.fromClipboard(Friends.get());
        }
    }
}
