package kult.legacy.klient.systems.modules.combat;

import kult.legacy.klient.events.kultklientlegacy.MouseButtonEvent;
import kult.legacy.klient.events.world.TickEvent;
import kult.legacy.klient.settings.BoolSetting;
import kult.legacy.klient.settings.EnumSetting;
import kult.legacy.klient.settings.Setting;
import kult.legacy.klient.settings.SettingGroup;
import kult.legacy.klient.systems.modules.Categories;
import kult.legacy.klient.systems.modules.Module;
import kult.legacy.klient.systems.modules.Modules;
import kult.legacy.klient.utils.misc.input.KeyAction;
import kult.legacy.klient.utils.player.FindItemResult;
import kult.legacy.klient.utils.player.InvUtils;
import kult.legacy.klient.eventbus.EventHandler;
import kult.legacy.klient.utils.player.PlayerUtils;
import net.minecraft.item.*;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_RIGHT;

public class Offhand extends Module {
    private final AutoTotem autoTotem = Modules.get().get(AutoTotem.class);

    private boolean isClicking;
    private boolean sentMessage;

    private Item currentItem;

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    // General

    private final Setting<Item> item = sgGeneral.add(new EnumSetting.Builder<Item>()
        .name("item")
        .description("Which item to hold in your offhand.")
        .defaultValue(Item.Crystal)
        .build()
    );

    private final Setting<Boolean> hotbar = sgGeneral.add(new BoolSetting.Builder()
        .name("hotbar")
        .description("Whether to use items from your hotbar.")
        .defaultValue(false)
        .build()
    );

    private final Setting<Boolean> toggleNotFound = sgGeneral.add(new BoolSetting.Builder()
        .name("toggle-not-found")
        .description("Toggles when you dont have the item you chose.")
        .defaultValue(false)
        .build()
    );

    private final Setting<Boolean> rightClick = sgGeneral.add(new BoolSetting.Builder()
        .name("right-click")
        .description("Only holds the item in your offhand when you are holding right click.")
        .defaultValue(false)
        .build()
    );

    private final Setting<Boolean> swordGap = sgGeneral.add(new BoolSetting.Builder()
        .name("sword-gap")
        .description("Holds an Enchanted Golden Apple when you are holding a sword.")
        .defaultValue(true)
        .build()
    );

    private final Setting<Boolean> crystalCA = sgGeneral.add(new BoolSetting.Builder()
        .name("crystal-on-ca")
        .description("Holds a crystal when you have Crystal Aura enabled.")
        .defaultValue(true)
        .build()
    );

    private final Setting<Boolean> crystalMine = sgGeneral.add(new BoolSetting.Builder()
        .name("crystal-on-mine")
        .description("Holds a crystal when you are mining.")
        .defaultValue(false)
        .build()
    );

    public Offhand() {
        super(Categories.Combat, Items.ENCHANTED_GOLDEN_APPLE, "offhand", "Allows you to hold specified items in your offhand.");
    }

    @Override
    public void onActivate() {
        sentMessage = false;
        isClicking = false;
        currentItem = item.get();
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        if (autoTotem.mode.get() == AutoTotem.Mode.Strict && autoTotem.isActive()) {
            info("(highlight)%s(default) does not work with (highlight)%s(default) set to (highlight)%s(default) mode, disabling...", title, autoTotem.title, "Strict");
            toggle();
            return;
        }

        if (autoTotem.mode.get() != AutoTotem.Mode.Strict && autoTotem.isActive() && PlayerUtils.getTotalHealth() - PlayerUtils.possibleHealthReductions(autoTotem.explosion.get(), autoTotem.fall.get()) <= autoTotem.health.get()) return;

        // Sword Gap
        if ((mc.player.getMainHandStack().getItem() instanceof SwordItem || mc.player.getMainHandStack().getItem() instanceof AxeItem) && swordGap.get()) currentItem = Item.EGap;

            // CA and Mining
        else if ((Modules.get().isActive(CrystalAura.class) && crystalCA.get()) || mc.interactionManager.isBreakingBlock() && crystalMine.get()) currentItem = Item.Crystal;

        else currentItem = item.get();

        // Checking offhand item
        if (mc.player.getOffHandStack().getItem() != currentItem.item) {
            FindItemResult item = InvUtils.find(itemStack -> itemStack.getItem() == currentItem.item, hotbar.get() ? 0 : 9, 35);

            // No offhand item
            if (!item.found()) {
                if (!sentMessage) {
                    warning("Chosen item not found" + (toggleNotFound.get() ? ", disabling..." : "."));
                    if (toggleNotFound.get()) {
                        toggle();
                        return;
                    }
                    sentMessage = true;
                }
            }

            // Swap to offhand
            else if ((isClicking || !rightClick.get()) && !autoTotem.isLocked() && !item.isOffhand()) {
                InvUtils.move().from(item.getSlot()).toOffhand();
                sentMessage = false;
            }
        }

        // If not clicking, set to totem if auto totem is on
        else if (!isClicking && rightClick.get()) {
            if (autoTotem.isActive()) {
                FindItemResult totem = InvUtils.find(itemStack -> itemStack.getItem() == Items.TOTEM_OF_UNDYING, hotbar.get() ? 0 : 9, 35);
                if (totem.found() && !totem.isOffhand()) InvUtils.move().from(totem.getSlot()).toOffhand();
            } else {
                FindItemResult empty = InvUtils.find(ItemStack::isEmpty, hotbar.get() ? 0 : 9, 35);
                if (empty.found()) InvUtils.move().fromOffhand().to(empty.getSlot());
            }
        }
    }

    @EventHandler
    private void onMouseButton(MouseButtonEvent event) {
        isClicking = mc.currentScreen == null && !autoTotem.isLocked() && !usableItem() && !mc.player.isUsingItem() && event.action == KeyAction.Press && event.button == GLFW_MOUSE_BUTTON_RIGHT;
    }

    private boolean usableItem() {
        return mc.player.getMainHandStack().getItem() == Items.BOW || mc.player.getMainHandStack().getItem() == Items.TRIDENT || mc.player.getMainHandStack().getItem() == Items.CROSSBOW || mc.player.getMainHandStack().getItem().isFood();
    }

    @Override
    public String getInfoString() {
        return item.get().name();
    }

    public enum Item {
        EGap(Items.ENCHANTED_GOLDEN_APPLE),
        Gap(Items.GOLDEN_APPLE),
        Crystal(Items.END_CRYSTAL),
        Shield(Items.SHIELD);

        net.minecraft.item.Item item;

        Item(net.minecraft.item.Item item) {
            this.item = item;
        }
    }
}
