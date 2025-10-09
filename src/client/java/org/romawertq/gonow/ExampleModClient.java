package org.romawertq.gonow;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class ExampleModClient implements ClientModInitializer {
	public static final String MOD_ID = "gonow";

	@Override
	public void onInitializeClient() {
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher.register(literal("gonow").executes(ExitCommand::execute)));
	}
}