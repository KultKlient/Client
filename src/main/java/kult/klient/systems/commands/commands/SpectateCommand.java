package kult.klient.systems.commands.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import kult.klient.KultKlient;
import kult.klient.eventbus.EventHandler;
import kult.klient.events.kultklient.KeyEvent;
import kult.klient.systems.commands.Command;
import kult.klient.systems.commands.arguments.PlayerArgumentType;
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
            KultKlient.EVENT_BUS.subscribe(shiftListener);
            return SINGLE_SUCCESS;
        }));
    }

    private static class StaticListener {
        @EventHandler
        private void onKey(KeyEvent event) {
            if (mc.options.keySneak.matchesKey(event.key, 0) || mc.options.keySneak.matchesMouse(event.key)) {
                mc.setCameraEntity(mc.player);
                event.cancel();
                KultKlient.EVENT_BUS.unsubscribe(this);
            }
        }
    }
}
