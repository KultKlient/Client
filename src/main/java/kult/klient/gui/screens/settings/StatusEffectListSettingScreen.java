package kult.klient.gui.screens.settings;

import kult.klient.gui.GuiTheme;
import kult.klient.gui.widgets.WWidget;
import kult.klient.settings.Setting;
import kult.klient.utils.misc.Names;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.util.registry.Registry;

import java.util.List;

public class StatusEffectListSettingScreen extends LeftRightListSettingScreen<StatusEffect> {
    public StatusEffectListSettingScreen(GuiTheme theme, Setting<List<StatusEffect>> setting) {
        super(theme, "Select Effects", setting, setting.get(), Registry.STATUS_EFFECT);
    }

    @Override
    protected WWidget getValueWidget(StatusEffect value) {
        return theme.itemWithLabel(getPotionStack(value), getValueName(value));
    }

    @Override
    protected String getValueName(StatusEffect value) {
        return Names.get(value);
    }

    private ItemStack getPotionStack(StatusEffect effect) {
        ItemStack potion = Items.POTION.getDefaultStack();
        potion.getOrCreateNbt().putInt("CustomPotionColor", PotionUtil.getColor(new Potion(new StatusEffectInstance(effect))));
        return potion;
    }
}
