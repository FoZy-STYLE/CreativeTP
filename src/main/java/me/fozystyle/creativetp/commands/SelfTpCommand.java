package me.fozystyle.creativetp.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.PosArgument;
import net.minecraft.command.argument.Vec3ArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.TeleportCommand;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Collections;

// Make class final to prevent extension. Utility classes should not be extended
public final class SelfTpCommand {

    // Make no arg constructor private to prevent instantiation for a utility class
    private SelfTpCommand() {}

    // Name method accordingly
    // Include all 3 parameters given by the CommandRegistrationCallback Fabric API to be able to use a method reference
    public static void registerSelfTpCommand(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {

        // Separate creation of command nodes and registration

        LiteralCommandNode<ServerCommandSource> rootNode = CommandManager.literal("selftp")
                .requires(ServerCommandSource::isExecutedByPlayer)
                .build();

        ArgumentCommandNode<ServerCommandSource, PosArgument> positionArgument = CommandManager.argument("location", Vec3ArgumentType.vec3())
                .requires(ServerCommandSource::isExecutedByPlayer)
                .executes(ctx -> {

                    PosArgument targetPosition = Vec3ArgumentType.getPosArgument(ctx, "location");
                    // CommandSource#getPlayerOrThrow will throw and error to the user if the command doesn't have
                    // a corresponding associated player, which is safer than just calling
                    // CommandSource#getPlayer
                    ServerPlayerEntity player = ctx.getSource().getPlayerOrThrow();

                    // Entity#refershPositionAfterTeleport takes care of setting the yaw and pitch and other relevant
                    // fields when teleporting a player. It is the recommended method to use as per the
                    // documentation.
                    // However, we will use vanilla's implementation of teleportation
                    TeleportCommand.execute(ctx.getSource(), Collections.singletonList(player), ctx.getSource().getWorld(), targetPosition, null, null);

                    // Instead of just returning "1" on success, return Command.SINGLE_SUCCESS, which is a static field assigned to 1
                    // but this value could change in the future. Using the field allows the mod to not break
                    // if the success value is ever changed in the future.
                    // It is bad practice to use so called "magic constants" instead of named constants provided by an API.
                    return Command.SINGLE_SUCCESS;
                })
                .build();



        // Using a regular entity argument type allows us to use entity selectors, allowing us to teleport
        // to things like mobs or monsters, not just players
        ArgumentCommandNode<ServerCommandSource, EntitySelector> entityArgument = CommandManager.argument("destination", EntityArgumentType.entity())
                .requires(ServerCommandSource::isExecutedByPlayer)
                .executes(ctx -> {

                    // We do not have to catch an exception here, as an exception is only thrown if the name of the argument
                    // we are looking for doesn't exist, and in this case we can guarantee that it does exist given that we just registered it.
                    // It can also throw if the name of the argument does exist, but its type is not what was expected. We can also make sure
                    // that the types match in this case.
                    Entity targetEntity = EntityArgumentType.getEntity(ctx, "destination");
                    ServerPlayerEntity player = ctx.getSource().getPlayerOrThrow();

                    // We will use vanilla's implementation of the teleport command
                    TeleportCommand.execute(ctx.getSource(), Collections.singleton(player), targetEntity);

                    return Command.SINGLE_SUCCESS;
                })
                .build();

        dispatcher.getRoot().addChild(rootNode);
        rootNode.addChild(positionArgument);
        rootNode.addChild(entityArgument);

    }
}
