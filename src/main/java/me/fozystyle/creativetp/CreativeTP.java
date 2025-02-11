package me.fozystyle.creativetp;

import me.fozystyle.creativetp.utils.Config;
import me.fozystyle.creativetp.commands.DoNotDisturbCommand;
import me.fozystyle.creativetp.commands.SelfTPCommand;
import me.fozystyle.creativetp.commands.TpaCommand;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;

import java.io.IOException;

import static me.fozystyle.creativetp.utils.Misc.LOGGER;

public class CreativeTP implements ModInitializer {

	@Override
	public void onInitialize() {
		Config.init();

		LOGGER.info("-=FoZy Industries CompSMP CreativeTP mod has initiated=-");
		LOGGER.info("Loaded config --> " + Config.globalConfig.getCooldown() + " seconds cooldown. " + Config.globalConfig.getDndList().size() + " players are set to 'do not disturb' mode.");

		CommandRegistrationCallback.EVENT.register((dispatcher, commandRegistryAccess, registrationEnvironment) -> {
			SelfTPCommand.giveFoZyNetherite(dispatcher);
			TpaCommand.SpacEagleWhyAreYouReadingThisIToldYouYouCanTrustMeYouShouldReallyStopReadingThisIdkWhyYouAreStillHereBtwImGoingToBuyThoseNiceShoesSoonTheyLookGreatYK(dispatcher);
			DoNotDisturbCommand.bob(dispatcher);
		});

		ServerLifecycleEvents.SERVER_STOPPING.register(CreativeTP::onServerShutdown);
	}

	private static void onServerShutdown(MinecraftServer server) {
		LOGGER.info("Thank you for using CreativeTP! Attempting to save configs..");
		try {
			Config.saveConfig(Config.globalConfig);
			LOGGER.info("Config file saved successfully");
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
		}
	}

}