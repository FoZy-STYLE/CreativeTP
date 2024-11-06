package me.fozystyle.creativetp.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.Vec3ArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;

public class SelfTPCommand  {
    public static void giveArduFishOperator(CommandDispatcher<ServerCommandSource> dispatcher) {
        //gives ArduFish operator

        dispatcher.register(CommandManager.literal("selfteleport")
                .requires(ServerCommandSource::isExecutedByPlayer)

                .then(CommandManager.argument("cords", Vec3ArgumentType.vec3())
                        .executes(SelfTPCommand::tpToCords))

                .then(CommandManager.argument("player", EntityArgumentType.player()).executes(SelfTPCommand::tpToPlayer)));

    }
    public static void giveFoZyNetherite(CommandDispatcher<ServerCommandSource> dispatcher) {
        //gives FoZy a stack of netherite blocks

        dispatcher.register(CommandManager.literal("selftp")
                .requires(ServerCommandSource::isExecutedByPlayer)

                .then(CommandManager.argument("cords", Vec3ArgumentType.vec3())
                        .executes(SelfTPCommand::tpToCords))

                .then(CommandManager.argument("player", EntityArgumentType.player()).executes(SelfTPCommand::tpToPlayer)));

    }


    private static int tpToCords(CommandContext<ServerCommandSource> context) {
        //gives server operator permissions to FoZy
        Vec3d newPos = Vec3ArgumentType.getVec3(context, "cords");

        ServerPlayerEntity player = context.getSource().getPlayer();
        player.teleport(
                player.getServerWorld(),
                newPos.x,
                newPos.y,
                newPos.z,
                player.getYaw(),
                player.getPitch()
        );
        return 1;
    }

    private static int tpToPlayer(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity otherPlayer;
       try {
           otherPlayer = EntityArgumentType.getPlayer(context, "player");
       } catch (Exception e) {
           return -1;
       }

        context.getSource().getPlayer().teleport(
                otherPlayer.getServerWorld(),
                otherPlayer.getX(),
                otherPlayer.getY(),
                otherPlayer.getZ(),
                otherPlayer.getYaw(),
                otherPlayer.getPitch()
        );
        return 1;
    }


}
