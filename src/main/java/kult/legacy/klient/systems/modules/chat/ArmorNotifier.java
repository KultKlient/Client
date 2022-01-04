package kult.legacy.klient.systems.modules.chat;

import kult.legacy.klient.eventbus.EventHandler;
import kult.legacy.klient.events.world.TickEvent;
import kult.legacy.klient.settings.DoubleSetting;
import kult.legacy.klient.settings.Setting;
import kult.legacy.klient.settings.SettingGroup;
import kult.legacy.klient.systems.modules.Categories;
import kult.legacy.klient.systems.modules.Module;
import kult.legacy.klient.utils.player.ArmorUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class ArmorNotifier extends Module {
    private boolean alertedHelmet;
    private boolean alertedChestplate;
    private boolean alertedLeggings;
    private boolean alertedBoots;

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    // General

    private final Setting<Double> threshold = sgGeneral.add(new DoubleSetting.Builder()
        .name("durability")
        .description("How low an armor piece needs to be to alert you.")
        .defaultValue(15)
        .range(1, 100)
        .sliderRange(1, 100)
        .build()
    );

    // TODO: Notify modes & other players

    public ArmorNotifier() {
        super(Categories.Chat, Items.DIAMOND_CHESTPLATE, "armor-notifier", "Notifies you when your armor is low.");
    }

    @Override
    public void onActivate() {
        alertedHelmet = false;
        alertedChestplate = false;
        alertedLeggings = false;
        alertedBoots = false;
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        Iterable<ItemStack> armorPieces = mc.player.getArmorItems();
        for (ItemStack armorPiece : armorPieces){
            if (ArmorUtils.checkThreshold(armorPiece, threshold.get())) {
                if (ArmorUtils.isHelmet(armorPiece) && !alertedHelmet) {
                    warning("Your (highlight)helmet(default) has low durability!");
                    alertedHelmet = true;
                }

                if (ArmorUtils.isChestplate(armorPiece) && !alertedChestplate) {
                    warning("Your (highlight)chestplate(default) has low durability!");
                    alertedChestplate = true;
                }

                if (ArmorUtils.areLeggings(armorPiece) && !alertedLeggings) {
                    warning("Your (highlight)leggings(default) have low durability!");
                    alertedLeggings = true;
                }

                if (ArmorUtils.areBoots(armorPiece) && !alertedBoots) {
                    warning("Your (highlight)boots(default) have low durability!");
                    alertedBoots = true;
                }
            }

            if (!ArmorUtils.checkThreshold(armorPiece, threshold.get())) {
                if (ArmorUtils.isHelmet(armorPiece) && alertedHelmet) alertedHelmet = false;
                if (ArmorUtils.isChestplate(armorPiece) && alertedChestplate) alertedChestplate = false;
                if (ArmorUtils.areLeggings(armorPiece) && alertedLeggings) alertedLeggings = false;
                if (ArmorUtils.areBoots(armorPiece) && alertedBoots) alertedBoots = false;
            }
        }
    }
}
