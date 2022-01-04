package kult.legacy.klient.systems.commands.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import kult.legacy.klient.KultKlientLegacy;
import kult.legacy.klient.events.kultklientlegacy.KeyEvent;
import kult.legacy.klient.systems.commands.arguments.PlayerArgumentType;
import kult.legacy.klient.systems.commands.Command;
import kult.legacy.klient.eventbus.EventHandler;
import net.minecraft.command.CommandSource;
import net.minecraft.text.LiteralText;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

public class SpectateCommand extends Command {
    private final StaticListener shiftListener = new StaticListener();

    public SpectateCommand() {
        super("spectate", "Allows you to spectate nearby players.");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(literal("reset").executes(context -> {
            mc.setCameraEntity(mc.player);
            return SINGLE_SUCCESS;
        }));

        builder.then(argument("player", PlayerArgumentType.player()).executes(context -> {
            mc.setCameraEntity(PlayerArgumentType.getPlayer(context));
            mc.player.sendMessage(new LiteralText("Sneak to un-spectate."), true);
            KultKlientLegacy.EVENT_BUS.subscribe(shiftListener);
            return SINGLE_SUCCESS;
        }));
    }

    private static class StaticListener {
        @EventHandler
        private void onKey(KeyEvent event) {
            if (mc.options.keySneak.matchesKey(event.key, 0) || mc.options.keySneak.matchesMouse(event.key)) {
                mc.setCameraEntity(mc.player);
                event.cancel();
                KultKlientLegacy.EVENT_BUS.unsubscribe(this);
            }
        }
    }
}
