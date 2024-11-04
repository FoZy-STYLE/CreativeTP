package me.fozystyle.creativetp.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class SelfTPCommand  {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {


        dispatcher.register(CommandManager.literal("selftp")
                .then(CommandManager.argument("z",StringArgumentType.string())
                        .then(CommandManager.argument("y", StringArgumentType.string())
                                .then(CommandManager.argument("z", StringArgumentType.string())
                                    .executes(context -> 1)))));

    }


    private static int run(CommandContext<ServerCommandSource> context) {
        System.out.println(StringArgumentType.getString(context, "x"));
        System.out.println(StringArgumentType.getString(context, "y"));
        System.out.println(StringArgumentType.getString(context, "z"));

        BlockPos playerPos;
        int x = 0;
        int y = 0;
        int z = 0;
        if ((playerPos = context.getSource().getPlayer().getBlockPos()) == null) {
            playerPos = new BlockPos(0, 0, 0);
        }
        try {
            x = parseStringArg(StringArgumentType.getString(context, "x"), playerPos.getX());
            y = parseStringArg(StringArgumentType.getString(context, "y"), playerPos.getY());
            z = parseStringArg(StringArgumentType.getString(context, "z"), playerPos.getZ());
        } catch (Exception e) {
            context.getSource().sendFeedback(() -> Text.literal("Incorrect format. Example: /selftp ~5 ~ 100"), false);
            return -1;
        }
        context.getSource().getPlayer().teleport(x, y, z, false);
        return 1;
    }

    private static int parseStringArg(String axisCord, int playerAxisCord) throws Exception {
        //converts the string argument provided by the player into an integer
        int parsedInt = 0;
        try {
            parsedInt = Integer.parseInt(axisCord);
        } catch (Exception e) {
            if (axisCord.contains("~")) {
                try {
                    parsedInt = Integer.parseInt(axisCord.replaceFirst("~", "")) + playerAxisCord;

                } catch (Exception ex) {
                    throw new Exception("Unable to parse string " + axisCord);
                }
            }
        }
        return parsedInt;
    }


}
