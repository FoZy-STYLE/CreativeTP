package me.fozystyle.creativetp.utils;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.TeleportCommand;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumSet;

public final class Misc {
    public static final int YELLOW = 16769280;
    public static final int GREEN = 13171400;
    public static final int GRAY = 10526880;
    public static final Logger LOGGER = LoggerFactory.getLogger("CreativeTP");


    public static void teleportPlayer(ServerCommandSource source, Vec3d pos) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrThrow();
        //uses vanilla implementation of the teleport command so that hopefully it won't break again
        TeleportCommand.teleport(source, player, player.getServerWorld(), pos.getX(), pos.getY(), pos.getZ(),
                EnumSet.noneOf(PositionFlag.class), player.getYaw(), player.getPitch(), null);
        player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP);

    }

    public static void LogException(String s, Exception e) {
        LOGGER.error(s + "\n" + e.getMessage());
    }

}

