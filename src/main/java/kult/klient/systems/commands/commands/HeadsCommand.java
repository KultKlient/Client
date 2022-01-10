package kult.klient.systems.commands.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import kult.klient.gui.GuiThemes;
import kult.klient.gui.screens.HeadScreen;
import kult.klient.systems.commands.Command;
import kult.klient.utils.Utils;
import net.minecraft.command.CommandSource;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

public class HeadsCommand extends Command {
    public HeadsCommand() {
        super("heads", "Displays heads GUI.", "head");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(ctx -> {
            Utils.screenToOpen = new HeadScreen(GuiThemes.get());
            return SINGLE_SUCCESS;
        });
    }
}
