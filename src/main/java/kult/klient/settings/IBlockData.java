package kult.klient.settings;

import kult.klient.utils.misc.IChangeable;
import kult.klient.utils.misc.ICopyable;
import kult.klient.utils.misc.ISerializable;
import kult.klient.gui.GuiTheme;
import kult.klient.gui.WidgetScreen;
import net.minecraft.block.Block;

public interface IBlockData<T extends ICopyable<T> & ISerializable<T> & IChangeable & IBlockData<T>> {
    WidgetScreen createScreen(GuiTheme theme, Block block, BlockDataSetting<T> setting);
}
