package kultklient.legacy.client.settings;

import kultklient.legacy.client.gui.GuiTheme;
import kultklient.legacy.client.gui.WidgetScreen;
import kultklient.legacy.client.utils.misc.IChangeable;
import kultklient.legacy.client.utils.misc.ICopyable;
import kultklient.legacy.client.utils.misc.ISerializable;
import net.minecraft.block.Block;

public interface IBlockData<T extends ICopyable<T> & ISerializable<T> & IChangeable & IBlockData<T>> {
    WidgetScreen createScreen(GuiTheme theme, Block block, BlockDataSetting<T> setting);
}
