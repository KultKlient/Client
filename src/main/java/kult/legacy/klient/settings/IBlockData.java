package kult.legacy.klient.settings;

import kult.legacy.klient.gui.GuiTheme;
import kult.legacy.klient.gui.WidgetScreen;
import kult.legacy.klient.utils.misc.IChangeable;
import kult.legacy.klient.utils.misc.ICopyable;
import kult.legacy.klient.utils.misc.ISerializable;
import net.minecraft.block.Block;

public interface IBlockData<T extends ICopyable<T> & ISerializable<T> & IChangeable & IBlockData<T>> {
    WidgetScreen createScreen(GuiTheme theme, Block block, BlockDataSetting<T> setting);
}
