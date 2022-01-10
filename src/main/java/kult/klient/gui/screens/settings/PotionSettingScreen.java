package kult.klient.gui.screens.settings;

import kult.klient.gui.widgets.containers.WTable;
import kult.klient.gui.GuiTheme;
import kult.klient.gui.WindowScreen;
import kult.klient.gui.widgets.pressable.WButton;
import kult.klient.settings.PotionSetting;
import kult.klient.utils.misc.MyPotion;

public class PotionSettingScreen extends WindowScreen {
    private final PotionSetting setting;

    public PotionSettingScreen(GuiTheme theme, PotionSetting setting) {
        super(theme, "Select Potion");

        this.setting = setting;
    }

    @Override
    public void initWidgets() {
        WTable table = add(theme.table()).expandX().widget();

        for (MyPotion potion : MyPotion.values()) {
            table.add(theme.itemWithLabel(potion.potion, potion.potion.getName().getString()));

            WButton select = table.add(theme.button("Select")).widget();
            select.action = () -> {
                setting.set(potion);
                onClose();
            };

            table.row();
        }
    }
}
