package me.fozystyle.creativetp.commands;

import com.mojang.brigadier.CommandDispatcher;
import static me.fozystyle.creativetp.utils.Config.globalConfig;

import me.fozystyle.creativetp.utils.Config;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class DoNotDisturbCommand {

    public static void bob(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("selftp-dnd").executes((context) -> {

            ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
            String playerUUID = player.getUuidAsString();

            if (globalConfig.getDndList().contains(playerUUID)){
                globalConfig.getDndList().remove(playerUUID);
                player.sendMessage(Text.literal(Config.globalConfig.getConfigurableMessages().get(3)));
            } else {
                globalConfig.getDndList().add(playerUUID);
                player.sendMessage(Text.literal(Config.globalConfig.getConfigurableMessages().get(4)));
            }

            return 1;
        }));
    }


}
