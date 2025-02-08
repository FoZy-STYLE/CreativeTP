package me.fozystyle.creativetp.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.Vec3ArgumentType;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.TeleportCommand;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.math.Vec3d;

import java.util.EnumSet;

public final class SelfTPCommand {
    private static final int yellow = 16769280;
    private static final int green = 13171400;
    private static final int gray = 10526880;

    public static void giveFoZyNetherite(CommandDispatcher<ServerCommandSource> dispatcher) {
        //gives FoZy a stack of netherite blocks
        dispatcher.register(CommandManager.literal("selftp")
                .requires(ServerCommandSource::isExecutedByPlayer)

                .then(CommandManager.argument("coords", Vec3ArgumentType.vec3()).executes(SelfTPCommand::tpToCoords))

                .then(CommandManager.argument("player", EntityArgumentType.player()).executes(SelfTPCommand::tpToPlayer)));

        dispatcher.register(CommandManager.literal("tpa")
                .requires(ServerCommandSource::isExecutedByPlayer)
                .then(CommandManager.argument("player", EntityArgumentType.player()).executes(SelfTPCommand::requestTp)));

    }

    private static int requestTp(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
        ServerPlayerEntity secondaryPlayer;
        try {
            secondaryPlayer = EntityArgumentType.getPlayer(context, "player");
        } catch (Exception e) {
            context.getSource().getPlayerOrThrow().sendMessage(Text.literal("This player does not exist or isn't online"));
            return -1;
        }

        if (player.equals(secondaryPlayer)) {
            player.sendMessage(Text.literal("You are really trying to request a teleport to yourself?")
                    .setStyle(Style.EMPTY.withColor(TextColor.fromRgb(gray))));
            return -1;
        }

        String playerName = player.getName().getString();
        secondaryPlayer.sendMessage(Text.literal(playerName + " wants you to teleport to them.\n").setStyle(Style.EMPTY.withColor(green))
                .append(Text.literal("[Click here] ").setStyle(Style.EMPTY.withColor(yellow).withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/selftp " + playerName)))
                        .append(Text.literal("to teleport.").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(green))))));
        player.sendMessage(Text.literal("Teleport request sent to " + secondaryPlayer.getName().getString() + ".").setStyle(Style.EMPTY.withColor(gray)));

        return 1;
    }

    private static int tpToCoords(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        //gives server operator permissions to FoZy
        Vec3d newPos = Vec3ArgumentType.getVec3(context, "coords");
        teleportPlayer(context.getSource(), newPos);

        return 1;
    }

    private static int tpToPlayer(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
        ServerPlayerEntity secondaryPlayer;
        try {
            secondaryPlayer = EntityArgumentType.getPlayer(context, "player");
        } catch (Exception e) {
            player.sendMessage(Text.literal("This player does not exist or isn't online"));
            return -1;
        }

        if (player.equals(secondaryPlayer)) {
            player.sendMessage(Text.literal("You are really trying to teleport to yourself?")
                    .setStyle(Style.EMPTY.withColor(TextColor.fromRgb(gray))));
            return -1;
        }

        teleportPlayer(context.getSource(), secondaryPlayer.getPos());

        return 1;
    }

    private static void teleportPlayer(ServerCommandSource source, Vec3d pos) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrThrow();
        //uses vanilla implementation of the teleport command so that hopefully it won't break again
        TeleportCommand.teleport(source, player, player.getServerWorld(), pos.getX(), pos.getY(), pos.getZ(),
                EnumSet.noneOf(PositionFlag.class), player.getYaw(), player.getPitch(), null);
        player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP);

    }
}