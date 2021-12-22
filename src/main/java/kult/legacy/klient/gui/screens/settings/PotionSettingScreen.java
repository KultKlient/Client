package kultklient.legacy.client.gui.screens.settings;

import kultklient.legacy.client.gui.GuiTheme;
import kultklient.legacy.client.gui.WindowScreen;
import kultklient.legacy.client.gui.widgets.containers.WTable;
import kultklient.legacy.client.gui.widgets.pressable.WButton;
import kultklient.legacy.client.settings.PotionSetting;
import kultklient.legacy.client.utils.misc.MyPotion;

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
