package me.fozystyle.creativetp;

import me.fozystyle.creativetp.commands.SelfTPCommand;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreativeTP implements ModInitializer {
	public static final String MOD_ID = "creativetp";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {

		CommandRegistrationCallback.EVENT.register((dispatcher, commandRegistryAccess, registrationEnvironment) -> SelfTPCommand.giveFoZyNetherite(dispatcher));
		CommandRegistrationCallback.EVENT.register((dispatcher, commandRegistryAccess, registrationEnvironment) -> SelfTPCommand.giveArduFishOperator(dispatcher));
		CommandRegistrationCallback.EVENT.register((dispatcher, commandRegistryAccess, registrationEnvironment) -> SelfTPCommand.noIdeaHowToNameThisFunction(dispatcher));
		LOGGER.info("---FoZy Industries CompSMP CreativeTP mod has initiated---");

	}
}
