package me.fozystyle.creativetp.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.fozystyle.creativetp.utils.BotheredList;
import me.fozystyle.creativetp.utils.Config;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;

import java.util.concurrent.ConcurrentHashMap;

import static me.fozystyle.creativetp.utils.Config.globalConfig;
import static me.fozystyle.creativetp.utils.Misc.*;

public class TpaCommand {

    public static final ConcurrentHashMap<String, BotheredList> cooldownMap = new ConcurrentHashMap<>();


    public static void SpacEagleWhyAreYouReadingThisIToldYouYouCanTrustMeYouShouldReallyStopReadingThisIdkWhyYouAreStillHereBtwImGoingToBuyThoseNiceShoesSoonTheyLookGreatYK(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("tpa")
                .requires(ServerCommandSource::isExecutedByPlayer)
                .then(CommandManager.argument("player", EntityArgumentType.player()).executes(TpaCommand::requestTp)));
    }

    private static int requestTp(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
        ServerPlayerEntity secondaryPlayer;
        try {
            secondaryPlayer = EntityArgumentType.getPlayer(context, "player");
        } catch (Exception e) {
            context.getSource().getPlayerOrThrow().sendMessage(Text.literal(Config.globalConfig.getConfigurableMessages().getFirst()));
            return -1;
        }

        if (player.equals(secondaryPlayer)) {
            player.sendMessage(Text.literal(Config.globalConfig.getConfigurableMessages().get(5))
                    .setStyle(Style.EMPTY.withColor(TextColor.fromRgb(GRAY))));
            return -1;
        }

        String playerName = player.getName().getString();
        String secondaryPlayerName = secondaryPlayer.getName().getString();


        if (globalConfig.getDndList().contains(secondaryPlayer.getUuidAsString())) {
            player.sendMessage(Text.literal(Config.globalConfig.getConfigurableMessages().get(1).replace("%player%", secondaryPlayerName))
                    .setStyle(Style.EMPTY.withColor(TextColor.fromRgb(GRAY))));
            return -1;
        }
        if (globalConfig.getDndList().contains(player.getUuidAsString())) {
            player.sendMessage(Text.literal(Config.globalConfig.getConfigurableMessages().get(6))
                    .setStyle(Style.EMPTY.withColor(TextColor.fromRgb(GRAY))));
            return -1;
        }

        String playerUUID = player.getUuidAsString();
        String secondaryPlayerUUID = secondaryPlayer.getUuidAsString();

        if (!handleCooldown(playerUUID, secondaryPlayerUUID)) {
            player.sendMessage(Text.literal(Config.globalConfig.getConfigurableMessages().get(7).replace("%player%", secondaryPlayerName).replace("%cooldown%", "" + globalConfig.getCooldown())));
            return -1;
        }

        secondaryPlayer.sendMessage(Text.literal(Config.globalConfig.getConfigurableMessages().get(8).replace("%player%", playerName) + "\n").setStyle(Style.EMPTY.withColor(GREEN))
                .append(Text.literal(Config.globalConfig.getConfigurableMessages().get(9) + " ").setStyle(Style.EMPTY.withColor(YELLOW).withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/selftp " + playerName)))
                        .append(Text.literal(Config.globalConfig.getConfigurableMessages().get(10)).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(GREEN))))));
        player.sendMessage(Text.literal(Config.globalConfig.getConfigurableMessages().get(11).replace("%player%", secondaryPlayerName)).setStyle(Style.EMPTY.withColor(GRAY)));

        return 1;
    }

    // ------ cooldown mechanism --------
    //true - allowed to execute the command, false - cooldown is still there
    static boolean handleCooldown(String playerUUID, String secondaryPlayerUUID) {
        if (!cooldownMap.containsKey(playerUUID)) {
            cooldownMap.put(playerUUID, new BotheredList(secondaryPlayerUUID));
            return true;
        }

        BotheredList blist = cooldownMap.get(playerUUID);

        if (!blist.hasPlayer(secondaryPlayerUUID)) {
            blist.addBotheredPlayer(secondaryPlayerUUID);
            return true;
        }

         if (blist.isBothered(secondaryPlayerUUID))
                return false;

         blist.resetPlayerCooldown(secondaryPlayerUUID);
         return true;
    }

}
