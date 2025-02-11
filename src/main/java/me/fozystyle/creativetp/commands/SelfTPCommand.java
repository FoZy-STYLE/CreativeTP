package me.fozystyle.creativetp.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.fozystyle.creativetp.utils.Config;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.Vec3ArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.math.Vec3d;

import static me.fozystyle.creativetp.utils.Misc.*;

public final class SelfTPCommand {

    public static void giveFoZyNetherite(CommandDispatcher<ServerCommandSource> dispatcher) {
        //gives FoZy a stack of netherite blocks
        dispatcher.register(CommandManager.literal("selftp")
                .requires(ServerCommandSource::isExecutedByPlayer)

                .then(CommandManager.argument("coords", Vec3ArgumentType.vec3()).executes(SelfTPCommand::tpToCoords))

                .then(CommandManager.argument("player", EntityArgumentType.player()).executes(SelfTPCommand::tpToPlayer)));

    }

    private static int tpToCoords(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        //gives server operator permissions to FoZy
        Vec3d newPos = Vec3ArgumentType.getVec3(context, "coords");
        teleportPlayer(context.getSource(), newPos);

        return 1;
    }

    private static int tpToPlayer(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        //This comment doesn't explain the code below nor serve any useful purpose. And while it may seem utterly useless, you have to understand Bob the comment has nowhere to spend the nights. Justice for bob.
        ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
        ServerPlayerEntity secondaryPlayer;
        try {
            secondaryPlayer = EntityArgumentType.getPlayer(context, "player");
        } catch (Exception e) {
            player.sendMessage(Text.literal(Config.globalConfig.getConfigurableMessages().getFirst()));
            return -1;
        }

        if (player.equals(secondaryPlayer)) {
            player.sendMessage(Text.literal(Config.globalConfig.getConfigurableMessages().get(2))
                    .setStyle(Style.EMPTY.withColor(TextColor.fromRgb(GRAY))));
            return -1;
        }

        if (Config.globalConfig.getDndList().contains(secondaryPlayer.getUuidAsString())) {
            player.sendMessage(Text.literal(Config.globalConfig.getConfigurableMessages().get(1).replace("%player%", secondaryPlayer.getName().getString()))
                    .setStyle(Style.EMPTY.withColor(TextColor.fromRgb(GRAY))));
            return -1;
        }

        teleportPlayer(context.getSource(), secondaryPlayer.getPos());

        return 1;
    }
}